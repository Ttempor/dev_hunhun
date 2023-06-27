package com.hun.service.service.expand;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.bo.AttendStudentBO;
import com.hun.bean.bo.ScheduleExpandStudentListVo;
import com.hun.bean.dto.expand.gradeinfo.DeleteGradeInfoDto;
import com.hun.bean.dto.expand.gradeinfo.QueryGradeInfoPageDto;
import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.base.Student;
import com.hun.bean.entity.expand.GradeInfo;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【gradeInfo】的数据库操作Service
* @createDate 2023-03-24 22:34:28
*/
public interface GradeInfoService {


    /**
     * 班级信息的条件分页
     */
    Page<GradeInfo> getGradeInfoPage(QueryGradeInfoPageDto queryGradeInfoPageDto);

    /**
     *通过学生id和班级id查询一个班级人员信息,判断班级中是否存在该学生
     */
    GradeInfo getOneByGradeIdAndStudentId(Long gradeId, Long studentId);

    /**
     * 保存一个班级人员信息,如添加一个学生到班级中调用
     * @param gradeInfo e
     */
    void saveGradeInfo(GradeInfo gradeInfo);

    /**
     * 退课
     */
    void tuiKe(DeleteGradeInfoDto deleteGradeInfoDto);


    /**
     * 批量增加学生缺勤课时
     */
    void batchPlusMissPeriod(Long gradeId, int missPeriod, List<Long> missStudentId);

    /**
     * 批量增加学生出勤课时
     */
    void batchPlusConsumePeriod(Long gradeId, int consumePeriod, List<Long> consumeStudentIds);


    /**
     * 通过班级id获得班级的学生
     */
    List<AttendStudentBO> getGradeStudents(Long gradeId);

    /**
     * 获得班级里的学生
     */
    List<GradeInfo> getGradeInfoByGradeId(Long gradeId);

    /**
     *班级变为上课中
     */
    void gradeIng(Long gradeId);
    /**
     *班级变为待排课
     */
    void gradeWait(Long gradeId);
    /**
     *班级变为开课中
     */
    void gradeRun(Long gradeId);

    /**
     * 从班级中删除学生
     */
    void deleteGradeInfo(Long gradeId, Long studentId);

    /**
     * 学生是否在该课程中
     */
    GradeInfo studentInCourse(Long courseId, Long studentId);

    /**
     * 排课id获得该课次的所有学生信息
     */
    List<ScheduleExpandStudentListVo> getInfoByScheduleId(Long scheduleId);


    /**
     * 学生购课, 处理学生是否在班级中, 如果不在则进行处理
     *
     * @param gradeId     班级id
     * @param studentId   学生id
     * @param student     学生
     * @param price       总价
     * @param totalPeriod 总课时
     * @param grade 班级
     */
    void buySchedulePreCheckAndHandle(Long gradeId, Long studentId, Student student,
                                      BigDecimal price, Integer totalPeriod, Grade grade);
}
