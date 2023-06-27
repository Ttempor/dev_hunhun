package com.hun.service.mapper.expand;
import com.hun.bean.app.StudentScheduleVO;
import com.hun.bean.bo.FinancialAnalysisHeaderBO;
import com.hun.bean.bo.FinancialAnalysisTeacherBO;
import com.hun.bean.vo.FinancialAnalysisTeacherVO;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hun.bean.entity.expand.StudentSchedule;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【student_schedule】的数据库操作Mapper
* @createDate 2023-04-16 22:35:05
* @Entity com.hun.bean.entity.expand.StudentSchedule
*/
public interface StudentScheduleMapper extends BaseMapper<StudentSchedule> {
    Integer attendCount(@Param("startTimestamp")long startTimestamp, @Param("endTimestamp")long endTimestamp);

    Integer missCount(@Param("startTimestamp")long startTimestamp, @Param("endTimestamp")long endTimestamp);

    BigDecimal callCourseCancelIncome(@Param("startTimestamp")long startTimestamp, @Param("endTimestamp")long endTimestamp);

    BigDecimal courseCancelIncome(@Param("startTimestamp")long startTimestamp, @Param("endTimestamp")long endTimestamp);

    Integer studentCourseCancelPeriod(@Param("startTimestamp")long startTimestamp, @Param("endTimestamp")long endTimestamp);

//    List<StudentSchedule> getByTimestampByGroupTeacherId(@Param("startTimestamp")long startTimestamp, @Param("endTimestamp")long endTimestamp);

//    List<FinancialAnalysisTeacherVO> getByTimestampByTeacherIdByGroupGradeId(@Param("teacherId") long teacherId,
//                                                                             @Param("startTimestamp")long startTimestamp,
//                                                                             @Param("endTimestamp")long endTimestamp);

    Integer getStudentCountByMonth(@Param("startTimestamp")long startTimestamp, @Param("endTimestamp")long endTimestamp);

    Integer getMonthStudentCountByGradeId(@Param("gradeId")long gradeId, @Param("startTimestamp")long startTimestamp, @Param("endTimestamp")long endTimestamp);

    FinancialAnalysisTeacherBO getAllCountAndIncomeByGradeId(@Param("gradeId")long gradeId, @Param("startTimestamp")long startTimestamp, @Param("endTimestamp")long endTimestamp);

    FinancialAnalysisHeaderBO getAllCountAndIncome(@Param("startTimestamp")long startTimestamp, @Param("endTimestamp")long endTimestamp);

    Integer studentInScheduleByScheduleIds(@Param("studentId") Long studentId, @Param("scheduleIds") List<Long> scheduleIds);

    List<StudentScheduleVO> getStudentSchedules(@Param("studentId") Long studentId, @Param("gradeId") Long gradeId);
}




