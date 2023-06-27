package com.hun.service.service.base;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.app.CategoryCourseBO;
import com.hun.bean.app.CategoryVO;
import com.hun.bean.dto.base.course.QueryCoursePageDto;
import com.hun.bean.dto.base.course.SaveCourseDto;
import com.hun.bean.entity.base.Course;
import com.hun.bean.vo.CourseVO;

import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【course_dev】的数据库操作Service
* @createDate 2023-03-25 00:59:30
*/
public interface CourseService {
    /**
     * 保存一个课程
     * @param saveCourseDto
     */
    void saveCourse(SaveCourseDto saveCourseDto);

    /**
     * 通过id获得一个课程
     */
    Course getOneCourseById(Long id);


    /**
     * 通过页码获得课程
     * @param queryCoursePageDto e
     * @return e
     */
    Page<Course> getCoursePage(QueryCoursePageDto queryCoursePageDto);

    /**
     * course_now_teacher_count
     * 通过课程id使拥有老师数增加
     */
    void incrTeacherCountByCourseId(Long courseId, int step);
    /**
     * course_now_teacher_count
     * 通过课程id使拥有班级数增加
     */
    void incrGradeCountByCourseId(Long courseId, int step);


    /**
     * 获得所有课程的课程id和课程名
     */
    List<CourseVO>  getCourseList();

    /**
     * 获得拥有班级数大于0的课程
     */
    List<CategoryVO>  getCategory() throws InterruptedException;


    /**
     * 移除缓存
     */
    void rmCategoryCache();

}
