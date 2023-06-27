package com.hun.api.sys.controller;

import com.hun.bean.app.APPUserInfoVO;
import com.hun.bean.app.StudentGradeVO;
import com.hun.bean.app.StudentScheduleVO;
import com.hun.bean.entity.base.Student;
import com.hun.common.response.BaseResponse;
import com.hun.security.common.bo.UserInfoInTokenBO;
import com.hun.security.common.util.AuthUserContext;
import com.hun.service.service.base.GradeService;
import com.hun.service.service.expand.StudentScheduleService;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Data
@RestController
@RequestMapping("/api/grade")
public class GradeController {
    @Resource
    private GradeService gradeService;
    @Resource
    private StudentScheduleService studentScheduleService;

    @GetMapping("/student/grade")
    public BaseResponse<List<StudentGradeVO>> getStudentGrade() {
        UserInfoInTokenBO bo = AuthUserContext.get();
        Long studentId = Long.valueOf(bo.getUserId());
        List<StudentGradeVO> result = gradeService.getStudentGrades(studentId);
        return BaseResponse.ok(result);
    }


    @GetMapping("/student/schedule")
    public BaseResponse<List<StudentScheduleVO>> getSchedule(@RequestParam Long gradeId) {
        UserInfoInTokenBO bo = AuthUserContext.get();
        Long studentId = Long.valueOf(bo.getUserId());
        return BaseResponse.ok(studentScheduleService.getStudentSchedules(studentId, gradeId));
    }
}
