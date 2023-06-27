package com.hun.service.mapper.base;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hun.bean.app.CategoryCourseBO;
import com.hun.bean.entity.base.Course;
import com.hun.bean.vo.CourseVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【course_dev】的数据库操作Mapper
* @createDate 2023-03-25 00:59:30
* @Entity com.dev.hunhun.entity.base.Course
*/
public interface CourseMapper extends BaseMapper<Course> {
    /**
     * course_now_teacher_count
     * 通过课程id使拥有老师数增加
     */
    @Update("UPDATE `hun_base`.`course_dev` SET `course_now_grade_count` = `course_now_grade_count` + #{step} WHERE `course_id` = #{courseId}")
    void incrTeacherCountByCourseId(@Param("courseId")Long courseId, @Param("step") int step);
    /**
     * course_now_teacher_count
     * 通过课程id使拥有班级数增加
     */
    @Update("UPDATE `hun_base`.`course_dev` SET `course_now_teacher_count` = `course_now_teacher_count` + #{step} WHERE `course_id` = #{courseId}")
    void incrGradeCountByCourseId(@Param("courseId") Long courseId, @Param("step") int step);


    /**
     * 查出所有课程的课程id和课程名
     * @return e
     */
    @Select("select course_id, course_name, course_total_period from `hun_base`.`course_dev`")
    List<CourseVO> selectCourseIdAndCourseName();


    List<CategoryCourseBO> getCategory();
}




