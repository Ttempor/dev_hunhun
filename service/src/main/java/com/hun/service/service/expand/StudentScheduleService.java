package com.hun.service.service.expand;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hun.bean.app.StudentScheduleVO;
import com.hun.bean.bo.FinancialAnalysisHeaderBO;
import com.hun.bean.bo.FinancialAnalysisTeacherBO;
import com.hun.bean.dto.expand.attend.QueryAttendScheduleDto;
import com.hun.bean.dto.expand.miss.QueryMissScheduleDto;
import com.hun.bean.entity.expand.StudentSchedule;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【student_schedule】的数据库操作Service
* @createDate 2023-04-16 22:35:05
*/
public interface StudentScheduleService extends IService<StudentSchedule> {

    /**
     * 删除学生在某班级里的排课
     * @param studentId 学生id
     */
    void deleteStudentSchedule(Long studentId, Long gradeId);

    /**
     * @param gradeId 班级id
     * @param studentId 学生id
     * @return 获得班级中某学生的排课
     */
    List<StudentSchedule> getPage(Long gradeId, Long studentId);

    /**
     * 防止重复调班
     * @param studentId 学生id
     * @param targetScheduleId 排课id
     */
    StudentSchedule getByScheduleIdAndStudentId(Long targetScheduleId, Long studentId);


    /**
     * 把班级排课批量导入新加入班级的学生排课中
     * @param gradeId 班级id
     * @param studentId 学生id
     * @param studentName 学生姓名
     */
    void save(Long gradeId, Long studentId, String studentName);

    /**
     * 获得范围内老师教的学生
     */
    List<StudentSchedule> getRange(Long teacherId, long startTimestamp, long endTimestamp);

    /**
     * 分页查询出勤
     */
    Page<StudentSchedule> getAttendSchedulePage(QueryAttendScheduleDto queryAttendScheduleDto);

    /**
     * 分页查询缺勤
     */
    Page<StudentSchedule> getMissSchedulePage(QueryMissScheduleDto queryMissSchedulePageDto);

    /**
     * 学生是否缺勤
     */
    StudentSchedule studentIsMissSchedule(Long scheduleId, Long studentId);

    /**
     * 记录缺勤
     * @param scheduleId 排课id
     * @param studentId 学生id
     * @param missReason 缺勤原因
     */
    void miss(Long scheduleId, Long studentId, String missReason);

    /**
     * 获得该课的学生
     * @param scheduleId 排课id
     */
    List<StudentSchedule> getByScheduleId(Long scheduleId);

    /**
     * 把该课改为已上状态,缺勤不算
     * @param scheduleId 课id
     */
    void endNotMiss(Long scheduleId);

    /**
     * 把该课缺勤的改为缺勤状态
     * @param scheduleId 课id
     */
    void endIsMiss(Long scheduleId);

    /**
     * 获得时间范围内的出勤人数, 包括套餐
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return 出勤人数
     */
    Integer attendCount(long startTimestamp, long endTimestamp);

    /**
     * 获得时间范围内的缺勤人数, 包括套餐
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return 缺勤人数
     */
    Integer missCount(long startTimestamp, long endTimestamp);

    /**
     * 获得时间范围内的点名课消金额, 不包括套餐, 仅按课时
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return 点名课消金额
     */
    BigDecimal callCourseCancelIncome(long startTimestamp, long endTimestamp);

    /**
     * 获得时间范围内的总课消金额, 包括套餐
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return 总课消金额
     */
    BigDecimal courseCancelIncome(long startTimestamp, long endTimestamp);


    /**
     * 把课改为上课中
     * @param scheduleId 排课id
     */
    void scheduleIng(Long scheduleId);

    /**
     * 设置学生排课收入, 缺勤除外
     * @param scheduleId 排课id
     * @param income 收入
     */
    void incomeNotMiss(Long scheduleId, BigDecimal income);

    /**
     * 设置学生排课收入
     * @param scheduleId 排课id
     * @param income 收入
     */
    void income(Long scheduleId, BigDecimal income);

    /**
     * 学员时间范围内总课消课时, 包括套餐
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return
     */
    Integer studentCourseCancelPeriod(long startTimestamp, long endTimestamp);

    /**
     * 获得时间范围内的学生排课, 安装班级名进行分组
     * 作用1：用于获得时间内的班级
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     */
//    List<StudentSchedule> getByTimestampByGroupTeacherId(long startTimestamp, long endTimestamp);

    /**
     * 返回老师运营数据分析
     * @param teacherId 老师id
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return 老师列表
     */
//    List<FinancialAnalysisTeacherVO> getByTimestampByTeacherIdByGroupGradeId(long teacherId, long startTimestamp, long endTimestamp);

    /**
     * 获得这个月学生就读人数
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return 学生人数
     */
    Integer getStudentCountByMonth(long startTimestamp, long endTimestamp);

    /**
     * 通过班级id获得这个月学生就读人数
     * @param gradeId 班级id
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return 学生人数
     */
    Integer getMonthStudentCountByGradeId(long gradeId, long startTimestamp, long endTimestamp);


    /**
     * 通过班级id获得时间范围内出勤人数 缺勤人数 总课消金额[包括套餐]
     * @param gradeId 班级id
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return bo
     */
    FinancialAnalysisTeacherBO getAllCountAndIncomeByGradeId(long gradeId, long startTimestamp, long endTimestamp);


    /**
     * 获得时间范围内出勤人数 缺勤人数 总课消金额[包括套餐]
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return bo
     */
    FinancialAnalysisHeaderBO getAllCountAndIncome(long startTimestamp, long endTimestamp);


    /**
     * 通过scheduleId更新studentSchedule
     */
    void updateByScheduleId(StudentSchedule studentSchedule);


    /**
     * 学生是否在studentSchedule中, 1为在, 0为不在, 用了limit, 所以只有这两个值或者null
     */
    Integer studentInScheduleByScheduleIds(Long studentId, List<Long> scheduleIds);

    /**
     * 获得学生在某个班级的班课信息
     * @param studentId 学生id
     * @param gradeId 班级id
     * @return 班课信息
     */
    List<StudentScheduleVO> getStudentSchedules(Long studentId, Long gradeId);
}
