package com.hun.api.sys.controller;


import com.hun.bean.app.APPUserInfoVO;
import com.hun.bean.app.StudentGradeVO;
import com.hun.bean.entity.base.Student;
import com.hun.bean.validation.annotation.Sex;
import com.hun.common.response.BaseResponse;
import com.hun.common.util.WXUtil;
import com.hun.security.common.bo.UserInfoInTokenBO;
import com.hun.security.common.util.AuthUserContext;
import com.hun.service.service.base.StudentService;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    @Resource
    private StudentService studentService;

    @GetMapping
    public BaseResponse<APPUserInfoVO> getInfo() {
        UserInfoInTokenBO bo = AuthUserContext.get();
        Student student = studentService.getOneStudentById(Long.valueOf(bo.getUserId()));
        APPUserInfoVO appUserInfoVO = new APPUserInfoVO();
        appUserInfoVO.setStudentId(student.getStudentId());
        appUserInfoVO.setName(student.getStudentName());
        appUserInfoVO.setSex(student.getStudentSex());
        appUserInfoVO.setPhone(student.getStudentPhone());
        BigDecimal decimal = new BigDecimal("100.00");
        appUserInfoVO.setBalance(student.getBalance().multiply(decimal));
        appUserInfoVO.setLockBalance(student.getLockBalance().multiply(decimal));
        return BaseResponse.ok(appUserInfoVO);
    }


    @PutMapping("name")
    public BaseResponse<Void> updateName(@Validated @NotBlank @Length(min = 1, max = 5) String name) {
        UserInfoInTokenBO bo = AuthUserContext.get();
        Long studentId = Long.valueOf(bo.getUserId());
        //校验是否已经填写姓名, 禁止重复填写, 否则抛出异常
        studentService.validatedBindName(studentId);
        studentService.updateNameById(studentId, name);
        return BaseResponse.ok();
    }

    @PutMapping("sex")
    public BaseResponse<Void> updateSex(@Validated @Sex String sex) {
        UserInfoInTokenBO bo = AuthUserContext.get();
        Long studentId = Long.valueOf(bo.getUserId());
        //校验是否已经选择性别, 禁止重复填写, 否则抛出异常
        studentService.validatedBindSex(studentId);
        studentService.updateSexById(studentId, sex);
        return BaseResponse.ok();
    }

    @PostMapping("phone")
    public BaseResponse<String> bindPhone(@Validated @NotBlank @Length(min = 64, max = 64) String code) {
        UserInfoInTokenBO bo = AuthUserContext.get();
        Long studentId = Long.valueOf(bo.getUserId());
        //校验是否已经绑定号码, 已经绑定则抛出异常
        studentService.validatedBindPhone(studentId);
        //获得微信访问令牌
        String accessToken = WXUtil.getAccessToken();
        //获得手机号码
        String phoneNumber = WXUtil.getPhoneNumber(accessToken, code);
        //保存到数据库中
        studentService.updatePhoneById(Long.valueOf(bo.getUserId()), phoneNumber);
        return BaseResponse.ok(phoneNumber);
    }

}
