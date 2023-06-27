package com.hun.service.service.base.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.dto.base.employee.QueryEmployeePageDto;
import com.hun.bean.dto.base.employee.SaveEmployeeDto;
import com.hun.bean.dto.base.employee.UpdateEmployeeDto;
import com.hun.bean.dto.base.teacher.UpdateTeacherByTeacherIdDto;
import com.hun.bean.entity.base.Employee;
import com.hun.bean.entity.base.Teacher;
import com.hun.bean.vo.EmployeeVO;
import com.hun.common.exception.BusinessException;
import com.hun.service.mapper.base.EmployeeMapper;
import com.hun.service.service.base.EmployeeService;
import com.hun.service.service.base.TeacherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * @author 性能小钢炮
 * @description 针对表【employee_dev】的数据库操作Service实现
 * @createDate 2023-03-25 23:25:01
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Resource
    private EmployeeMapper employeeMapper;
    @Resource
    private TeacherService teacherService;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Value("${default-page-size}")
    private long pageSize;
    /**
     * 条件分页获得员工,自定义联查
     * @param queryEmployeePageDto e
     * @return e
     */
    public Page<EmployeeVO> getEmployeesByPage(QueryEmployeePageDto queryEmployeePageDto) {
        QueryWrapper<EmployeeVO> queryWrapper = new QueryWrapper<>();
        //员工不是被删除状态
        queryWrapper.eq("e.is_delete", false);
        //是否要附上员工id条件
        if (queryEmployeePageDto.getEmployeeId() != null) {
            queryWrapper.eq("e.employee_id", queryEmployeePageDto.getEmployeeId());
        }
        //是否要附上员工姓名条件
        if (queryEmployeePageDto.getEmployeeName() != null) {
            queryWrapper.like("e.employee_name", queryEmployeePageDto.getEmployeeName().trim());
        }
        //开始分页
        Page<EmployeeVO> page = Page.of(queryEmployeePageDto.getP(), pageSize);
        //执行查询
        return employeeMapper.selectPageWithJoin(page, queryWrapper);
    }

    /**
     * 保存一个员工
     *
     * @param saveEmployeeDto e
     */
    public void saveEmployee(SaveEmployeeDto saveEmployeeDto) {
        Employee employee = new Employee();
        employee.setEmployeeName(saveEmployeeDto.getEmployeeName().trim());
        employee.setEmployeeSex(saveEmployeeDto.getEmployeeSex());
        employee.setEmployeePhone(saveEmployeeDto.getEmployeePhone().trim());
        employee.setPostCount(0);
        employee.setPassword("111");
        employee.setIsDelete(false);

        employeeMapper.insert(employee);
    }


    /**
     * 修改一个员工
     *
     * @param updateEmployeeDto e
     */
    public void updateOneEmployee(UpdateEmployeeDto updateEmployeeDto) {
        Employee employee = employeeMapper.selectById(updateEmployeeDto.getEmployeeId());
        if (employee == null) {
            throw new BusinessException("员工id错误");
        }
        String employeeName = updateEmployeeDto.getEmployeeName().trim();
        String employeePhone = updateEmployeeDto.getEmployeePhone().trim();
        String password = updateEmployeeDto.getPassword().trim();
        employee.setEmployeeName(employeeName);
        employee.setEmployeeSex(updateEmployeeDto.getEmployeeSex());
        employee.setEmployeePhone(employeePhone);
        employee.setPassword(password);
        //通过员工id查询老师
        Teacher teacher = teacherService.getOneTeacherByEmployeeId(updateEmployeeDto.getEmployeeId());
        UpdateTeacherByTeacherIdDto updateTeacherDto = null;
        if (teacher != null) {
            //该员工同时是老师,创建修改dto,同步修改老师信息
            updateTeacherDto = new UpdateTeacherByTeacherIdDto();
            updateTeacherDto.setTeacherId(teacher.getTeacherId());
            updateTeacherDto.setEmployeeName(employeeName);
            updateTeacherDto.setEmployeeSex(updateEmployeeDto.getEmployeeSex());
            updateTeacherDto.setEmployeePhone(employeePhone);
        }
        //用于在事务中使用
        UpdateTeacherByTeacherIdDto finalUpdateTeacherDto = updateTeacherDto;
        //同时修改到teacher表
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                if (finalUpdateTeacherDto != null) {
                    //员工同时是老师, 通过老师id更新老师
                    teacherService.updateTeacherByTeacherId(finalUpdateTeacherDto, false);
                }
                //通过员工id更新员工
                employeeMapper.updateById(employee);
            }
        });
    }


    /**
     * 通过id获得一个员工
     */
    @Override
    public Employee getOneEmployeeById(Long id) {
        return employeeMapper.selectById(id);
    }

    /**
     * 通过员工id使员工岗位数+1
     */
    public void incrPostByEmployeeId(Long employeeId, int step) {
        employeeMapper.incrPostByEmployeeId(employeeId, step);
    }
}





