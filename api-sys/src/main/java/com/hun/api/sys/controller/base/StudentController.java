package com.hun.api.sys.controller.base;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.dto.base.student.*;
import com.hun.bean.entity.base.Student;
import com.hun.bean.entity.expand.StudentGradeStop;
import com.hun.common.exception.BusinessException;
import com.hun.common.response.BaseResponse;
import com.hun.service.service.base.StudentService;
import com.hun.service.service.expand.StudentGradeStopService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 学生
 */
@RestController
@RequestMapping("/api/student")
@CrossOrigin
public class StudentController {
    @Resource
    private StudentService studentService;
    @Resource
    private StudentGradeStopService studentGradeStopService;

    /**
     * 添加一个学生
     * @param saveStudentDto e
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> saveOneStudent(@Validated @RequestBody SaveStudentDto saveStudentDto) {
        studentService.saveStudent(saveStudentDto);
        return BaseResponse.ok("保存成功");
    }

    /**
     * 学生的条件分页
     * @param queryStudentPageDto e
     */
    @GetMapping()
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<Page<Student>> getStudentPage(@Validated QueryStudentPageDto queryStudentPageDto) {
        return BaseResponse.ok(studentService.getStudentsByPage(queryStudentPageDto));
    }

    /**
     * 修改一个学生
     * @param updateStudentDto e
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> updateOneStudent(@Validated @RequestBody UpdateStudentDto updateStudentDto) {
        studentService.updateOneStudent(updateStudentDto);
        return BaseResponse.ok("保存成功");
    }



    /**
     * 充值余额
     */
    @PutMapping("/deposit")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> deposit(@Validated @RequestBody DepositDto depositDto) {
        studentService.deposit(depositDto.getStudentId(), depositDto.getBalance());
        return BaseResponse.ok("充值成功");
    }

    /**
     * 为学生停课的班级充值余额
     */
    @PutMapping("/deposit/stopGrade")
    @PreAuthorize("@pms.hasPermission('admin,重复了')")
    public BaseResponse<String> depositGradeStopGrade(@Validated @RequestBody DepositStopGradeDto dto) {
        //学生停课班级存在
        StudentGradeStop gradeStop = studentGradeStopService.getOne(dto.getGradeId(), dto.getStudentId());
        if (gradeStop == null) {
            throw new BusinessException("学生没有停课");
        }
        //学生总余额增加
        studentService.deposit(dto.getStudentId(), dto.getBalance());
        //学生锁额增加
        studentService.lockBalance(dto.getStudentId(), dto.getBalance());
        //学生班级充值锁额增加
//        studentGradeStopService.lockBalanceDeposit(dto.getGradeId(), dto.getStudentId(), dto.getBalance());
        return BaseResponse.ok("充值成功");
    }
    /**
     * 为学生停课班级解锁余额
     */
    @PutMapping("/deposit/stopGrade/unlock")
    @PreAuthorize("@pms.hasPermission('admin,重复了')")
    public BaseResponse<String> depositGradeUnlock(@Validated @RequestBody DepositStopGradeDto dto) {
        //学生停课班级存在
        StudentGradeStop gradeStop = studentGradeStopService.getOne(dto.getGradeId(), dto.getStudentId());
        if (gradeStop == null) {
            throw new BusinessException("学生没有停课");
        }
        //删除学生停课班级
//        studentGradeStopService.remove(dto.getGradeId(), dto.getStudentId());
        //解锁学生锁额
        studentService.unLockBalance(dto.getStudentId(), gradeStop.getLockSpendBalance().add(gradeStop.getDepositLockBalance()));
        return BaseResponse.ok("充值成功");
    }
}
