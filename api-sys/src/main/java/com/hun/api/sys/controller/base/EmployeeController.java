package com.hun.api.sys.controller.base;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.dto.base.employee.QueryEmployeePageDto;
import com.hun.bean.dto.base.employee.SaveEmployeeDto;
import com.hun.bean.dto.base.employee.UpdateEmployeeDto;
import com.hun.bean.vo.EmployeeVO;
import com.hun.common.response.BaseResponse;
import com.hun.service.service.base.EmployeeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 员工
 */
@RestController
@RequestMapping("/api/employee")
@CrossOrigin
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;


    /**
     * 员工的条件分页
     * @param queryEmployeePageDto e
     */
    @GetMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<Page<EmployeeVO>> getEmployeePage(@Validated QueryEmployeePageDto queryEmployeePageDto) {
        return BaseResponse.ok(employeeService.getEmployeesByPage(queryEmployeePageDto));
    }

    /**
     * 添加一个员工
     * @param saveEmployeeDto e
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> saveOneEmployee(@Validated @RequestBody SaveEmployeeDto saveEmployeeDto) {
        employeeService.saveEmployee(saveEmployeeDto);
        return BaseResponse.ok("保存成功");
    }


    /**
     * 修改一个员工
     * @param updateEmployeeDto e
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> updateOneEmployee(@Validated @RequestBody UpdateEmployeeDto updateEmployeeDto) {
        employeeService.updateOneEmployee(updateEmployeeDto);
        return BaseResponse.ok("保存成功");
    }






}
