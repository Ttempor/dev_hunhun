package com.hun.service.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hun.bean.entity.base.Teacher;
import com.hun.bean.vo.TeacherVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【teacher_dev】的数据库操作Mapper
* @createDate 2023-03-25 23:25:09
* @Entity com.dev.hunhun.entity.base.Teacher
*/
public interface TeacherMapper extends BaseMapper<Teacher> {
    /**
     * 通过老师id使老师拥有班级数增加
     */
    @Update("UPDATE `hun_base`.`teacher_dev` SET `grade_count` = `grade_count` + #{step} " +
            "WHERE `teacher_id` = #{teacherId}")
    void incrGradeCountByTeacherId(@Param("teacherId") Long teacherId, @Param("step") int step);

    /**
     * 通过员工id查询老师
     * @param employeeId e
     * @return e
     */
    Teacher selectOneByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 查出所有老师的老师id和员工名
     * @return e
     */
    @Select("select teacher_id, employee_name from `hun_base`.`teacher_dev`")
    List<TeacherVO> selectTeacherIdAndEmployeeName();
}




