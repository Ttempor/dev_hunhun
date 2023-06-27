package com.hun.api.sys.controller.expand;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.dto.expand.studentgradestop.QueryStudentStopDto;
import com.hun.bean.dto.expand.studentschedule.StudentScheduleDto;
import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.base.Student;
import com.hun.bean.entity.expand.*;
import com.hun.bean.enums.ChargeMethodCategory;
import com.hun.bean.enums.GradeState;
import com.hun.bean.validation.annotation.Decimal;
import com.hun.common.exception.BusinessException;
import com.hun.common.response.BaseResponse;
import com.hun.service.service.base.CourseService;
import com.hun.service.service.base.GradeService;
import com.hun.service.service.base.StudentService;
import com.hun.service.service.expand.GradeInfoService;
import com.hun.service.service.expand.StudentGradeStopService;
import com.hun.service.service.expand.StudentScheduleService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 停课
 */
@RestController
@RequestMapping("/api/studentGradeStop")
@CrossOrigin
@Validated
public class StudentGradeStopController {

    @Resource
    private StudentGradeStopService studentGradeStopService;
    @Resource
    private GradeService gradeService;
    @Resource
    private GradeInfoService gradeInfoService;
    @Resource
    private StudentScheduleService studentScheduleService;
    @Resource
    private StudentService studentService;
    @Resource
    private CourseService courseService;
    @Resource
    private MapperFacade mapperFacade;


    /**
     * 停课的条件查询
     */
    @GetMapping()
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<Page<StudentGradeStop>> getPage(@Validated QueryStudentStopDto queryMissSchedulePageDto) {
        return BaseResponse.ok(studentGradeStopService.getPage(queryMissSchedulePageDto));
    }


    /**
     * 停课
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> studentStopGrade(@Validated @RequestBody StudentScheduleDto studentScheduleDto) {
        Long gradeId = studentScheduleDto.getGradeId();
        Long studentId = studentScheduleDto.getStudentId();
        //不为上课中,不为已经结课,套餐不可停课
        GradeInfo gradeInfo = gradeInfoService.getOneByGradeIdAndStudentId(gradeId, studentId);
        if (gradeInfo == null) {
            throw new BusinessException("班级中学生不存在");
        }
        //只有处于开课中,或待排课才能停课
        if (!(GradeState.isRun(gradeInfo.getGradeState()) || GradeState.isWait(gradeInfo.getGradeState()))) {
            throw new BusinessException("开课中或待排课才能退课");
        }
        if (ChargeMethodCategory.isCombination(gradeInfo.getChargeMethodId())) {
            throw new BusinessException("套餐不可退课");
        }
        //把学生从班级里删除
        gradeInfoService.deleteGradeInfo(gradeId, studentId);
        //把学生的排课删除, 已上的课不删除
        studentScheduleService.deleteStudentSchedule(studentId, gradeId);
        //班级学生人数-1
        gradeService.incrGradeNowStudentCountByGradeId(gradeId, -1);
        //学生班级数-1
        studentService.incrGradeCountByStudentId(studentId, -1);

        //添加到停课表中
        StudentGradeStop studentGradeStop = mapperFacade.map(gradeInfo, StudentGradeStop.class);
        studentGradeStop.setStopDatetime(LocalDateTime.now());
        studentGradeStop.setStopTimestamp(System.currentTimeMillis());
        studentGradeStop.setStopReason(studentScheduleDto.getStopReason());
        studentGradeStop.setDepositLockBalance(new BigDecimal("0.00"));
        studentGradeStopService.save(studentGradeStop);
        courseService.rmCategoryCache();
        return BaseResponse.ok("成功");
    }

    /**
     * 解锁停课余额
     */
    @PutMapping("/tk")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> deleteStudentStopGrade(@RequestParam Long stopId) {
        StudentGradeStop studentGradeStop = studentGradeStopService.getById(stopId);
        //将剩余锁定余额解锁
        studentService.lockBalance(studentGradeStop.getStudentId(),
                studentGradeStop.getLockSpendBalance().add(studentGradeStop.getDepositLockBalance()).negate());
        //删除停课id
        studentGradeStopService.removeById(stopId);
        return BaseResponse.ok("成功");
    }
    /**
     * 充值
     */
    @PutMapping("/deposit")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> depositStudentStopGrade(@RequestParam Long stopId,
                                                        @Decimal @RequestParam BigDecimal balance) {
        //获得停课
        StudentGradeStop studentGradeStop = studentGradeStopService.getById(stopId);
        //学生总余额增加
        studentService.deposit(studentGradeStop.getStudentId(), balance);
        //学生锁额增加
        studentService.lockBalance(studentGradeStop.getStudentId(), balance);
        //学生班级充值锁额增加
        studentGradeStopService.stopGradeDeposit(stopId, balance);
        return BaseResponse.ok("成功");
    }

    /**
     * 添加一个学生到班级中
     */
    @PutMapping("/student")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> SaveStudentToGrade(@RequestParam Long stopId, @RequestParam Long gradeId) {
        //班级校验
        Grade grade = gradeService.getGradeAndCheckJoin(gradeId);
        //获得停课
        StudentGradeStop studentGradeStop = studentGradeStopService.getById(stopId);
        //学生校验
        Student student = gradeService.getStudentAndCheckNotInGrade(studentGradeStop.getStudentId(), grade);
        //学生余额校验
        if (studentGradeStop.getLockSpendBalance().add(studentGradeStop.getDepositLockBalance())
                .subtract(grade.getPrice()).compareTo(new BigDecimal("0.00")) < 0) {
            throw new BusinessException("余额不足");
        }
        //添加到班级中
        gradeService.saveStudentToGrade(student, grade);
        //不需要锁定余额
        //先扣充值余额,再扣剩余余额 700 600 100 200
        BigDecimal balance = grade.getPrice().subtract(studentGradeStop.getDepositLockBalance());
        if (balance.compareTo(new BigDecimal("0.00")) <= 0) {
            //减少充值余额
            studentGradeStopService.stopGradeDeposit(stopId, grade.getPrice().negate());
        } else {
            //减少充值余额
            studentGradeStopService.stopGradeDeposit(stopId, studentGradeStop.getDepositLockBalance().negate());
            //减少剩余余额
            studentGradeStopService.plusLockSpendBalance(stopId, balance.negate());
            //如果剩余余额为0
            if (balance.subtract(studentGradeStop.getLockSpendBalance())
                    .compareTo(new BigDecimal("0.00")) == 0) {
                //余额为0, 删除停课id
                studentGradeStopService.removeById(stopId);
            }
        }
        //批量导入排课
        studentScheduleService.save(gradeId, student.getStudentId(), student.getStudentName());
        courseService.rmCategoryCache();
        return BaseResponse.ok("保存成功");
    }
}
