package com.hun.security.api.controller;


import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hun.bean.entity.base.Student;
import com.hun.bean.model.User;
import com.hun.bean.entity.base.Employee;
import com.hun.common.exception.BusinessException;
import com.hun.common.response.BaseResponse;
import com.hun.common.util.WXUtil;
import com.hun.security.common.bo.UserInfoInTokenBO;
import com.hun.security.common.dto.AuthenticationDTO;
import com.hun.security.common.enums.SysTypeEnum;
import com.hun.security.common.manager.PasswordCheckManager;
import com.hun.security.common.manager.PasswordManager;
import com.hun.security.common.manager.TokenStore;
import com.hun.security.common.vo.TokenInfoVO;
import com.hun.service.mapper.base.EmployeeMapper;
import com.hun.service.mapper.base.StudentMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.servlet.http.HttpUtils;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/api")
@Log4j2
@CrossOrigin
public class LoginController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private PasswordCheckManager passwordCheckManager;

    @Autowired
    private PasswordManager passwordManager;

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;

    @PostMapping("/login")
    public BaseResponse<TokenInfoVO> login(
            @Valid @RequestBody AuthenticationDTO authenticationDTO) {
        //获得用户名
        //员工登录是员工id 用户名登录是手机号码 管理员登录是admin
        //获得用户,未获得则抛出异常
        User user = getUser(authenticationDTO);


        //token业务对象
        UserInfoInTokenBO userInfoInToken = new UserInfoInTokenBO();

        log.info("authenticationDTO:{}", authenticationDTO);
        if (SysTypeEnum.isEmployee(user)) {
            //是员工则直接比对密码
            //校验密码,出错会抛异常，密码错误
            passwordCheckManager.checkPassword(SysTypeEnum.Employee,
                    authenticationDTO.getUserName(), authenticationDTO.getPassWord(), user.getPassword());
            userInfoInToken.setPerms(Set.of("employee"));
        } else if ((SysTypeEnum.isADMIN(user))) {
            //设置权限
            userInfoInToken.setPerms(Set.of("admin"));
        } else if ((SysTypeEnum.isORDINARY(user))) {
            //设置权限
            userInfoInToken.setPerms(Set.of("student"));
        } else {
            //密码加密
//        String decryptPassword = passwordManager.decryptPassword(authenticationDTO.getPassWord());
            throw new BusinessException("登录异常");
        }

        //用户id
        userInfoInToken.setUserId(user.getUserId());
        //用户类型
        userInfoInToken.setSysType(user.getType());

        // 获得accessToken和refreshToken并加密返回
        TokenInfoVO tokenInfoVO = tokenStore.storeAndGetVo(userInfoInToken);
        tokenInfoVO.setType(SysTypeEnum.parse(user));
        tokenInfoVO.setId(user.getUserId());
        return BaseResponse.ok(tokenInfoVO);
    }

    private User getUser(AuthenticationDTO authenticationDTO) {
        String username = authenticationDTO.getUserName();
        User user = new User();
        if (NumberUtil.isInteger(username) && username.length() < 10) {
            //员工登录 用户名是整形并且字符串长度在10以下被视为员工登录并且不尝试其余登录方式
            //员工id+密码登陆
            Employee employee = employeeMapper.selectById(username);
            if (employee != null) {
                user.setUserId("employee[" + username + "]:");
                user.setPassword(employee.getPassword());
                user.setType(SysTypeEnum.Employee.value());
                return user;
            }
        } else if (username.equals(adminUsername) && adminPassword.equals(authenticationDTO.getPassWord())) {
            //管理员登录
            //管理员账号+密码登录
            user.setUserId("admin:");
            user.setPassword(authenticationDTO.getPassWord());
            user.setType(SysTypeEnum.ADMIN.value());
            return user;
        } else {
            //用户登录 微信登录 username就是code
            Student student = vxLogin(authenticationDTO.getPassWord());
            user.setUserId(String.valueOf(student.getStudentId()));
            user.setPassword("");
            user.setType(SysTypeEnum.ORDINARY.value());
            return user;
        }
        throw new BusinessException("账号或密码不正确");
    }

    private Student vxLogin(String code) {
        //通过code登录
        Map<String, String> map = WXUtil.wxCode(code);
        String openid = map.get("openid");
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("open_id", openid);
        Student student = studentMapper.selectOne(queryWrapper);
        if (student == null) {
            //新建用户
            BigDecimal bigDecimal = new BigDecimal("0.00");
            student = new Student();
            student.setBalance(bigDecimal);
            student.setLockBalance(bigDecimal);
            student.setSpend(bigDecimal);
            student.setGradeCount(0);
            student.setOpenId(openid);
            studentMapper.insert(student);
        }
        return student;
    }


}
