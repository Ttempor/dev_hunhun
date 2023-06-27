package com.hun.service.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.dto.base.employee.QueryEmployeePageDto;
import com.hun.bean.dto.base.employee.SaveEmployeeDto;
import com.hun.bean.dto.base.employee.UpdateEmployeeDto;
import com.hun.bean.entity.base.Employee;
import com.hun.bean.vo.EmployeeVO;

/**
* @author 性能小钢炮
* @description 针对表【employee_dev】的数据库操作Service
* @createDate 2023-03-25 23:25:01
*/
public interface EmployeeService {


    /**
     * 分页获得员工
     * @param queryEmployeePageDto e
     * @return e
     */
    Page<EmployeeVO> getEmployeesByPage(QueryEmployeePageDto queryEmployeePageDto);
    /**
     * 保存一个员工
     * @param saveEmployeeDto e
     */
    void saveEmployee(SaveEmployeeDto saveEmployeeDto);


    /**
     * 修改一个员工
     * @param updateEmployeeDto e
     */
    void updateOneEmployee(UpdateEmployeeDto updateEmployeeDto);

    /**
     * 通过id获得一个员工
     */
    Employee getOneEmployeeById(Long id);


    /**
     * 通过员工id使员工岗位数+1
     */
    void incrPostByEmployeeId(Long employeeId, int step);
}
