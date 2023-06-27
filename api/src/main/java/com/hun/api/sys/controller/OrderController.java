package com.hun.api.sys.controller;

import com.hun.bean.bo.BuyScheduleBO;
import com.hun.bean.dto.base.grade.BuyScheduleDto;
import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.base.Schedule;
import com.hun.bean.entity.expand.StudentSchedule;
import com.hun.bean.enums.ChargeMethodCategory;
import com.hun.common.exception.BusinessException;
import com.hun.common.response.BaseResponse;
import com.hun.common.util.RedisUtil;
import com.hun.common.util.WXUtil;
import com.hun.security.common.bo.UserInfoInTokenBO;
import com.hun.security.common.util.AuthUserContext;
import com.hun.service.service.base.CourseService;
import com.hun.service.service.base.GradeService;
import com.hun.service.service.base.ScheduleService;
import com.hun.service.service.base.StudentService;
import com.hun.service.service.expand.GradeInfoService;
import com.hun.service.service.expand.StudentScheduleService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.hibernate.validator.constraints.Length;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 订单
 */
@Slf4j
@Data
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Value("${redis-lock.LOCK_GRADE_KEY}")
    private String LOCK_GRADE_KEY;
    @Value("${redis-lock.LOCK_STUDENT_KEY}")
    private String LOCK_STUDENT_KEY;
    @Resource
    private StudentService studentService;
    @Resource
    private GradeService gradeService;
    @Resource
    private CourseService courseService;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private GradeInfoService gradeInfoService;
    @Resource
    private StudentScheduleService studentScheduleService;
    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private ScheduleService scheduleService;

    @PostMapping()
    public BaseResponse<String> buy(@Validated @RequestBody BuyScheduleDto dto) throws InterruptedException {
        UserInfoInTokenBO bo = AuthUserContext.get();
        Long studentId = Long.valueOf(bo.getUserId());
        dto.setStudentId(studentId);
        Long gradeId = dto.getGradeId();
        log.info("{}", dto);
        RLock gradeLock = redissonClient.getLock(LOCK_GRADE_KEY + gradeId);
        RLock studentLock = redissonClient.getLock(LOCK_STUDENT_KEY + studentId);
        //给班级上锁
        if (!gradeLock.tryLock(-1, 15000, TimeUnit.MILLISECONDS)) {
            throw new BusinessException("班级系统被锁定,稍后再试");
        }
        //给学生上锁
        if (!studentLock.tryLock(-1, 15000, TimeUnit.MILLISECONDS)) {
            //解锁
            gradeLock.unlock();
            throw new BusinessException("学生系统被锁定,稍后再试");
        }
        try {
            List<Long> scheduleIds = dto.getScheduleIds();
            //业务校验
            BuyScheduleBO buyScheduleBO = gradeService.buySchedulePreCheck(gradeId, scheduleIds, studentId);

            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    //校验学生是否在班级中, 不在则加入到班级中
                    gradeInfoService.buySchedulePreCheckAndHandle(gradeId, studentId, buyScheduleBO.getStudent(),
                            buyScheduleBO.getPrice(), buyScheduleBO.getTotalPeriod(), buyScheduleBO.getGrade());

                    //增加学生锁定余额
                    studentService.lockBalance(studentId, buyScheduleBO.getPrice());

                    StudentSchedule studentSchedule = new StudentSchedule();
                    studentSchedule.setStudentId(studentId);
                    studentSchedule.setStudentName(buyScheduleBO.getStudent().getStudentName());
                    //studentSchedule中添加学生
                    for (Schedule schedule : buyScheduleBO.getSchedules()) {
                        mapperFacade.map(schedule, studentSchedule);
                        studentSchedule.setStudentScheduleId(null);
                        studentSchedule.setSourceScheduleId(schedule.getScheduleId());
                        studentSchedule.setSourceGradeId(gradeId);
                        //保存学生到班级的排课中
                        studentScheduleService.save(studentSchedule);
                        //排课人数+1
                        scheduleService.plusNowStudentCount(schedule.getScheduleId(), 1);
                    }
                    courseService.rmCategoryCache();
                }
            });
        } finally {
            //解锁
            studentLock.unlock();
            gradeLock.unlock();
        }
        return BaseResponse.ok("保存成功");
    }
}
