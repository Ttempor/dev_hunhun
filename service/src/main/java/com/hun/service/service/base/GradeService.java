package com.hun.service.service.base;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.app.AppGradeVO;
import com.hun.bean.app.CategoryItemVO;
import com.hun.bean.app.StudentGradeVO;
import com.hun.bean.bo.BuyScheduleBO;
import com.hun.bean.dto.base.grade.*;
import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.base.Student;
import com.hun.bean.vo.GradeVO;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【grade】的数据库操作Service
* @createDate 2023-03-25 18:35:44
*/
public interface GradeService {

    /**
     * 班级的条件分页
     * @param queryGradePageDto e
     */
    Page<Grade> getGradePage(QueryGradePageDto queryGradePageDto);

    /**
     * 保存一个班级
     * @param saveGradeDto e
     */
    void saveGrade(SaveGradeDto saveGradeDto);


    /**
     * 修改一个班级
     * @param updateGradeDto e
     */
    void updateOneGrade(UpdateGradeDto updateGradeDto);


    /**
     * 添加一个学生到班级中
     */
    void saveStudentToGrade(Student student, Grade grade);


    /**
     * 检查学生
     */
    Student getStudentAndCheckNotInGrade(Long studentId, Grade grade);

    /**
     * 检查班级能否加入学生
     */
    Grade getGradeAndCheckJoin(Long gradeId);
    /**
     * 获得所有待排课和停课中且人数未满的班级id和班级名 获得待排课状态和停课状态的班级id和班级名
     */
    List<GradeVO> getGradeList();

    /**
     * 通过班级id查老师
     */
    Grade getOneGradeByGradeId(Long gradeId);


    /**
     * 指定班级id改为上课中
     */
    void gradeIng(Long gradeId);

    /**
     * 指定班级id改为开课中
     */
    void gradeRunAndSendMq(Long gradeId);

    /**
     * 指定班级id改为已结课, gradeInfo同样变为已经结课
     */
    void gradeEnd(Long gradeId);

    /**
     * 修改班级已上课时
     */
    void plusNowPeriod(Long gradeId, Integer period);


    /**
     * 通过状态查询所有班级id和名称和状态
     */
    List<GradeVO> getGradeByState(QueryByStateDto queryByStateDto);

    /**
     *  通过老师id获得上课中的班级列表
     */
    List<Grade> getIngGradeListByTeacherId(Long teacherId);
    
    /**
     *  通过老师id获得上课中的班级列表
     */
    List<Grade> getNotEndGradeListByTeacherId(Long teacherId);


    /**
     *  班级当前学生增加
     * @param gradeId 班级id
     * @param step 增加值
     */
    void incrGradeNowStudentCountByGradeId(Long gradeId, int step);

    /**
     * 增加收入
     * @param gradeId 班级id
     * @param schedulePrice 收入
     */
    void income(Long gradeId, BigDecimal schedulePrice);

    /**
     * 获得班级学生当前人数
     * @param gradeId 班级id
     * @return 学生人数
     */
    Integer getNowStudentCount(Long gradeId);

    /***
     * 学员购课业务校验
     * @param scheduleIds 课id
     * @param studentId 学员id
     */
    BuyScheduleBO buySchedulePreCheck(Long gradeId, List<Long> scheduleIds, Long studentId);


    /**
     * 获得分类
     * @return 分类
     */
    List<CategoryItemVO> getCategoryByCourseId(Long courseId);

    /**
     * 通过班级id获得班级信息,app用
     */
    AppGradeVO getCategoryGrade(Long gradeId);


    /**
     * 通过学生id获得学生的班级, app用
     */
    List<StudentGradeVO> getStudentGrades(Long studentId);
}

