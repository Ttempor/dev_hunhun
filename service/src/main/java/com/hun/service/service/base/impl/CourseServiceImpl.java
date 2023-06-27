package com.hun.service.service.base.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.app.CategoryCourseBO;
import com.hun.bean.app.CategoryVO;
import com.hun.bean.dto.base.course.QueryCoursePageDto;
import com.hun.bean.dto.base.course.SaveCourseDto;
import com.hun.bean.entity.base.ChargeMethod;
import com.hun.bean.entity.base.Course;
import com.hun.bean.vo.CourseVO;
import com.hun.common.exception.BusinessException;
import com.hun.service.mapper.base.GradeMapper;
import com.hun.service.service.base.ChargeMethodService;
import com.hun.service.service.base.CourseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
* @author 性能小钢炮
* @description 针对表【course_dev】的数据库操作Service实现
* @createDate 2023-03-25 00:59:30
*/
@Service
public class CourseServiceImpl implements CourseService {
    @Resource
    private com.hun.service.mapper.base.CourseMapper CourseMapper;
    @Resource
    private ChargeMethodService chargeMethodService;
    @Resource(name = "threadPool")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private GradeMapper gradeMapper;
    @Value("${default-page-size}")
    private long pageSize;

    /**
     * 保存学生
     * @param saveCourseDto e
     */
    @Override
    public void saveCourse(SaveCourseDto saveCourseDto) {
        Long chargeMethodId = saveCourseDto.getChargeMethodId();
        //通过收费方式id验证收费方式是否存在
        ChargeMethod chargeMethod = chargeMethodService.getOneChargeMethodById(chargeMethodId);
        if (chargeMethod == null) {
            throw new BusinessException("收费方式id错误");
        }
        Course course = new Course();
        course.setCourseName(saveCourseDto.getCourseName().trim());
        course.setChargeMethodId(chargeMethodId);
        course.setChargeMethodName(chargeMethod.getChargeMethodName().trim());
        course.setChargeMethodExpression(saveCourseDto.getChargeMethodExpression());
        if (chargeMethodId == 1) {
            //按课时收费计算总价
            course.setPrice(saveCourseDto.getChargeMethodExpression().
                    multiply(new BigDecimal(saveCourseDto.getCourseTotalPeriod().toString())));
        } else if (chargeMethodId == 2){
            //套餐总价
            course.setPrice(saveCourseDto.getChargeMethodExpression());
        }
        course.setCourseTotalPeriod(saveCourseDto.getCourseTotalPeriod());
        course.setCourseNowGradeCount(0);
        course.setCourseNowTeacherCount(0);
        course.setCourseNowStudentCount(0);
        course.setIsDelete(false);
        CourseMapper.insert(course);
    }

    /**
     * 通过id获得一个课程
     */
    @Override
    public Course getOneCourseById(Long id) {
        return CourseMapper.selectById(id);
    }

    /**
     * 通过页码获得课程
     * @param queryCoursePageDto e
     * @return e
     */
    @Override
    public Page<Course> getCoursePage(QueryCoursePageDto queryCoursePageDto) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        //是否要附上课程名条件
        if (queryCoursePageDto.getCourseName() != null) {
            queryWrapper.like("course_name", queryCoursePageDto.getCourseName().trim());
        }
        Page<Course> page = Page.of(queryCoursePageDto.getP(), pageSize);
        return CourseMapper.selectPage(page, queryWrapper);
    }

    /**
     * course_now_teacher_count
     * 通过课程id使拥有老师数增加
     */
    @Override
    public void incrTeacherCountByCourseId(Long courseId, int step) {
        CourseMapper.incrTeacherCountByCourseId(courseId, step);
    }
    /**
     * course_now_teacher_count
     * 通过课程id使拥有班级数增加
     */
    @Override
    public void incrGradeCountByCourseId(Long courseId, int step) {
        CourseMapper.incrGradeCountByCourseId(courseId, step);
    }

    /**
     * 获得所有课程的课程id和课程名
     */
    @Override
    public List<CourseVO> getCourseList() {
        return CourseMapper.selectCourseIdAndCourseName();
    }

    @Override
    @Cacheable(cacheNames = "getCategory")
    public List<CategoryVO> getCategory() throws InterruptedException {
        List<CategoryCourseBO> courseBO = CourseMapper.getCategory();
        List<CategoryVO> result = new CopyOnWriteArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(courseBO.size());
        for (CategoryCourseBO categoryCourseBO : courseBO) {
            CategoryVO categoryVO = new CategoryVO();
            categoryVO.setCourseId(categoryCourseBO.getCourseId());
            categoryVO.setCourseName(categoryCourseBO.getCourseName());
            //添加到result中
            result.add(categoryVO);
            CompletableFuture.runAsync(() -> {
                categoryVO.setGrades(gradeMapper.getCategoryByCourseId(categoryCourseBO.getCourseId()));
                countDownLatch.countDown();
            }, threadPoolTaskExecutor);
        }
        countDownLatch.await();
        return result;
    }

    @Override
    @CacheEvict(cacheNames = "getCategory")
    public void rmCategoryCache() {

    }
}




