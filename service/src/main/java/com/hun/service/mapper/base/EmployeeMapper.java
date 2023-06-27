package com.hun.service.mapper.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.entity.base.Employee;
import com.hun.bean.vo.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
* @author 性能小钢炮
* @description 针对表【employee_dev】的数据库操作Mapper
* @createDate 2023-03-25 23:25:01
* @Entity com.dev.hunhun.entity.base.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    /**
     * 通过id增加员工岗位数
     * @param employeeId
     * @param step
     */
    @Update("UPDATE `hun_base`.`employee_dev` SET `post_count` = `post_count` + #{step} WHERE `employee_id` = #{employeeId} and `is_delete` = 0")
    void incrPostByEmployeeId(@Param("employeeId")Long employeeId, @Param("step") int step);


    /**
     * 联查分页
     * @param employeeVOPage e
     * @return e
     */
    @Select("select e.employee_id,e.employee_name,e.employee_sex,e.employee_phone,e.post_count,e.password,t.teacher_id " +
            "from hun_base.employee_dev e " +
            "left join hun_base.teacher_dev t " +
            "on e.employee_id = t.employee_id and t.is_delete = 0 " +
            "${ew.customSqlSegment}")
    Page<EmployeeVO> selectPageWithJoin(Page<EmployeeVO> employeeVOPage, @Param(Constants.WRAPPER) QueryWrapper<EmployeeVO> queryWrapper);

}




