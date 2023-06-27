package com.hun.service.service.base.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.dto.base.teacher.QueryTeacherPageDto;
import com.hun.bean.dto.base.teacher.SaveTeacherDto;
import com.hun.bean.dto.base.teacher.UpdateTeacherByTeacherIdDto;
import com.hun.bean.entity.base.Employee;
import com.hun.bean.entity.base.Teacher;
import com.hun.bean.vo.TeacherVO;
import com.hun.common.exception.BusinessException;
import com.hun.service.service.base.TeacherService;
import com.hun.service.mapper.base.TeacherMapper;
import com.hun.service.service.base.EmployeeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【teacher_dev】的数据库操作Service实现
* @createDate 2023-03-25 23:25:09
*/
@Service
public class TeacherServiceImpl implements TeacherService {
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private EmployeeService employeeService;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Value("${default-page-size}")
    private long pageSize;


    /**
     * 通过页码获得老师
     * @param queryTeacherPageDto e
     * @return e e
     */
    @Override
    public Page<Teacher> getTeacherByPage(QueryTeacherPageDto queryTeacherPageDto) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        //是否要附上老师id条件
        if (queryTeacherPageDto.getTeacherId() != null) {
            queryWrapper.eq("teacher_id", queryTeacherPageDto.getTeacherId());
        }
        //是否要附上员工id条件
        if (queryTeacherPageDto.getEmployeeId() != null) {
            queryWrapper.eq("employee_id", queryTeacherPageDto.getEmployeeId());
        }
        //是否要附上老师姓名条件
        if (queryTeacherPageDto.getEmployeeName() != null) {
            queryWrapper.like("employee_name", queryTeacherPageDto.getEmployeeName().trim());
        }
        Page<Teacher> page = Page.of(queryTeacherPageDto.getP(), pageSize);
        return teacherMapper.selectPage(page, queryWrapper);
    }


    /**
     * 保存老师
     * @param saveTeacherDto e
     */
    @Override
    public void saveTeacher(SaveTeacherDto saveTeacherDto) {
        Employee employee = employeeService.getOneEmployeeById(saveTeacherDto.getEmployeeId());
        if (employee == null) {
            throw new BusinessException("员工id错误");
        }
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id",employee.getEmployeeId());
        if (teacherMapper.exists(queryWrapper)) {
            throw new BusinessException("员工已经是老师");
        }
        //电话号码已校验
        Teacher teacher = new Teacher();
        teacher.setEmployeeId(employee.getEmployeeId());
        teacher.setEmployeeName(employee.getEmployeeName().trim());
        teacher.setEmployeeSex(employee.getEmployeeSex());
        teacher.setEmployeePhone(employee.getEmployeePhone().trim());
        teacher.setGradeCount(0);
        teacher.setIsDelete(false);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                //插入老师
                teacherMapper.insert(teacher);
                //员工岗位数+1
                employeeService.incrPostByEmployeeId(employee.getEmployeeId(), 1);
            }
        });
    }
    /**
     * 通过老师id获得一个老师
     */
    @Override
    public Teacher getOneTeacherByTeacherId(Long id) {
        return teacherMapper.selectById(id);
    }

    /**
     * 通过员工id获得一个老师
     */
    @Override
    public Teacher getOneTeacherByEmployeeId(Long id) {
        return teacherMapper.selectOneByEmployeeId(id);
    }
    /**
     * 通过姓名获得多个老师, 老师姓名可能重名
     */
    @Override
    public List<Teacher> getListTeacherByName(String name) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_name", name);
        return teacherMapper.selectList(queryWrapper);
    }

    /**
     * 通过老师id使老师拥有班级数增加
     */
    @Override
    public void incrGradeCountByTeacherId(Long teacherId, int step) {
        teacherMapper.incrGradeCountByTeacherId(teacherId, step);
    }


    /**
     * 通过员工id修改老师信息
     */
    public void updateTeacherByTeacherId(UpdateTeacherByTeacherIdDto updateTeacherDto, boolean trim) {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(updateTeacherDto.getTeacherId());
        if (trim) {
            teacher.setEmployeeName(updateTeacherDto.getEmployeeName().trim());
            teacher.setEmployeePhone(updateTeacherDto.getEmployeePhone().trim());
        } else {
            teacher.setEmployeeName(updateTeacherDto.getEmployeeName());
            teacher.setEmployeePhone(updateTeacherDto.getEmployeePhone());
        }
        teacher.setEmployeeSex(updateTeacherDto.getEmployeeSex());
        teacherMapper.updateById(teacher);
    }

    /**
     * 获得所有老师的老师id和员工名
     */
    @Override
    public List<TeacherVO> getTeacherList() {
        return teacherMapper.selectTeacherIdAndEmployeeName();
    }


    /**
     * 通过老师id查询一个老师
     * @return e
     */
    @Override
    public Teacher getTeacherOne(Long teacherId) {
        return teacherMapper.selectById(teacherId);
    }
}







