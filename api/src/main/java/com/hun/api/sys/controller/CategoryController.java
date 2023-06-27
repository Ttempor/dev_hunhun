package com.hun.api.sys.controller;

import com.hun.bean.app.AppGradeVO;
import com.hun.bean.app.CategoryCourseBO;
import com.hun.bean.app.CategoryVO;
import com.hun.bean.app.GradeScheduleBO;
import com.hun.common.response.BaseResponse;
import com.hun.service.service.base.CourseService;
import com.hun.service.service.base.GradeService;
import com.hun.service.service.base.ScheduleService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * 分类接口
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Resource
    private GradeService gradeService;
    @Resource
    private CourseService courseService;
    @Resource
    private ScheduleService scheduleService;

    @Resource(name = "threadPool")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @GetMapping()
    public BaseResponse<List<CategoryVO>> getCategory() throws InterruptedException {
        return BaseResponse.ok(courseService.getCategory());
    }

    @GetMapping("/grade")
    public BaseResponse<AppGradeVO> getCategoryGrade(@RequestParam Long gradeId) {
        AppGradeVO categoryGrade = gradeService.getCategoryGrade(gradeId);
        List<GradeScheduleBO> schedules = scheduleService.getAppSchedules(gradeId);
        BigDecimal bigDecimal = new BigDecimal("100");
        categoryGrade.setSchedules(schedules);
        categoryGrade.setChargeExpression(categoryGrade.getChargeExpression().multiply(bigDecimal));
        categoryGrade.setPrice(categoryGrade.getPrice().multiply(bigDecimal));
        return BaseResponse.ok(categoryGrade);
    }
}
