package com.hun.api.sys.controller.base;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.bo.BuyScheduleBO;
import com.hun.bean.dto.base.grade.*;
import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.base.Schedule;
import com.hun.bean.entity.base.Student;
import com.hun.bean.entity.expand.GradeInfo;
import com.hun.bean.entity.expand.StudentSchedule;
import com.hun.bean.enums.ScheduleState;
import com.hun.bean.validation.annotation.Id;
import com.hun.bean.vo.GradeVO;
import com.hun.common.exception.BusinessException;
import com.hun.common.response.BaseResponse;
import com.hun.common.util.RedisUtil;
import com.hun.service.service.base.CourseService;
import com.hun.service.service.base.GradeService;
import com.hun.service.service.base.ScheduleService;
import com.hun.service.service.base.StudentService;
import com.hun.service.service.expand.GradeInfoService;
import com.hun.service.service.expand.StudentScheduleService;
import ma.glasnost.orika.MapperFacade;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 班级
 */
@RestController
@RequestMapping(value = "/api/grade")
@CrossOrigin
public class GradeController {
    @Value("${redis-lock.LOCK_GRADE_KEY}")
    private String LOCK_GRADE_KEY;
    @Value("${redis-lock.LOCK_STUDENT_KEY}")
    private String LOCK_STUDENT_KEY;
    @Resource
    private GradeService gradeService;
    @Resource
    private GradeInfoService gradeInfoService;
    @Resource
    private CourseService courseService;
    @Resource
    private StudentService studentService;
    @Resource
    private StudentScheduleService studentScheduleService;
    @Resource
    private ScheduleService scheduleService;
    @Resource
    private MapperFacade mapperFacade;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private RedissonClient redissonClient;


    /**
     * 班级的条件分页
     *
     * @param queryGradePageDto e
     */
    @GetMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<Page<Grade>> getGradePage(@Validated QueryGradePageDto queryGradePageDto) {
        return BaseResponse.ok(gradeService.getGradePage(queryGradePageDto));
    }


    /**
     * 通过状态查询所有班级id和名称和状态
     */
    @GetMapping("/list/state")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<List<GradeVO>> getGradeByState(@Validated QueryByStateDto queryByStateDto) {
        return BaseResponse.ok(gradeService.getGradeByState(queryByStateDto));
    }

    /**
     * 添加一个班级
     *
     * @param saveGradeDto e
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> saveOneGrade(@Validated @RequestBody SaveGradeDto saveGradeDto) {
        gradeService.saveGrade(saveGradeDto);
        courseService.rmCategoryCache();
        return BaseResponse.ok("保存成功");
    }


    /**
     * 修改一个班级
     *
     * @param updateGradeDto e
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> updateOneGrade(@Validated @RequestBody UpdateGradeDto updateGradeDto) {
        gradeService.updateOneGrade(updateGradeDto);
        courseService.rmCategoryCache();
        return BaseResponse.ok("保存成功");
    }


    /**
     * 获得所有待排课和已开课且人数未满的班级
     */
    @GetMapping("/list")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<List<GradeVO>> getGradeList() {
        return BaseResponse.ok(gradeService.getGradeList());
    }

    /**
     * 通过班级id查询一个班级
     */
    @GetMapping("/one")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<Grade> getOneGradeByGradeId(Long gradeId) {
        return BaseResponse.ok(gradeService.getOneGradeByGradeId(gradeId));
    }

    /**
     * 通过老师id获得上课中的班级列表
     */
    @GetMapping("/teacher")
//    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<List<Grade>> getIngGradeListByTeacherId(Long teacherId) {
        return BaseResponse.ok(gradeService.getIngGradeListByTeacherId(teacherId));
    }

    /**
     * 通过老师id获得老师的未结课的班级列表
     */
    @GetMapping("/teacher/all")
//    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<List<Grade>> getNotEndGradeListByTeacherId(Long teacherId) {
        return BaseResponse.ok(gradeService.getNotEndGradeListByTeacherId(teacherId));
    }


    /**
     * 添加一个学生到班级中, 按课时购买, 套餐无法使用
     */
    @PostMapping("/buy")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> SaveStudentToGrade(@Validated @RequestBody BuyScheduleDto dto) throws Exception {
        Long gradeId = dto.getGradeId();
        Long studentId = dto.getStudentId();
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
            gradeLock.unlock();
            studentLock.unlock();
        }
        return BaseResponse.ok("保存成功");
    }

}

/**
 * 添加一个学生到班级中
 *
 * @param e
 * @PostMapping("/student")
 * @PreAuthorize("@pms.hasPermission('admin')") public BaseResponse<String> SaveStudentToGrade(@Validated @RequestBody SaveStudentToGradeDto saveStudentToGradeDto) {
 * //班级校验
 * Grade grade = gradeService.getGradeAndCheckJoin(saveStudentToGradeDto.getGradeId());
 * //学生校验
 * Student student = gradeService.getStudentAndCheckNotInGrade(saveStudentToGradeDto.getStudentId(), grade);
 * //点名总价会减少
 * //学生余额减去锁定余额减去支付余额是否足够
 * if (student.getBalance().subtract(student.getLockBalance()).
 * subtract(grade.getPrice()).compareTo(new BigDecimal("0.00")) < 0) {
 * throw new BusinessException("余额不足");
 * }
 * //添加到班级中
 * gradeService.saveStudentToGrade(student, grade);
 * //锁定余额
 * studentService.lockBalance(student.getStudentId(), grade.getPrice());
 * //批量导入排课
 * studentScheduleService.save(grade.getGradeId(), student.getStudentId(), student.getStudentName());
 * return BaseResponse.ok("保存成功");
 * }
 */