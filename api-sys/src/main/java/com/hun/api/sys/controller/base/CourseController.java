package com.hun.api.sys.controller.base;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.dto.base.course.QueryCoursePageDto;
import com.hun.bean.dto.base.course.SaveCourseDto;
import com.hun.bean.entity.base.Course;
import com.hun.bean.vo.CourseVO;
import com.hun.common.response.BaseResponse;
import com.hun.service.service.base.CourseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课程
 * @author 性能小钢炮
 */
@RestController
@RequestMapping("/api/course")
@CrossOrigin
public class CourseController {
    @Resource
    private CourseService courseService;
    /**
     * 添加一个课程
     * @param saveCourseDto s
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> saveCourse(@Validated @RequestBody SaveCourseDto saveCourseDto) {
        courseService.saveCourse(saveCourseDto);
        courseService.rmCategoryCache();
        return BaseResponse.ok("保存成功");
    }

    /**
     * 课程的条件分页
     * @param queryStudentPageDto e
     */
    @GetMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<Page<Course>> getCoursePage(@Validated QueryCoursePageDto queryStudentPageDto) {
        return BaseResponse.ok(courseService.getCoursePage(queryStudentPageDto));
    }

    /**
     * 获得所有课程的课程id和课程名
     */
    @GetMapping("/list")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<List<CourseVO>> getCourseList() {
        return BaseResponse.ok(courseService.getCourseList());
    }

}
