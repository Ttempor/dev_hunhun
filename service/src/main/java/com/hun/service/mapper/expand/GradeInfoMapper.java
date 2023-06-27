package com.hun.service.mapper.expand;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hun.bean.app.StudentGradeVO;
import com.hun.bean.bo.AttendStudentBO;
import com.hun.bean.bo.ScheduleExpandStudentListVo;
import com.hun.bean.entity.expand.GradeInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【grade_info】的数据库操作Mapper
* @createDate 2023-04-04 19:44:02
* @Entity com.dev.hunhun.entity.expand.GradeInfo
*/
public interface GradeInfoMapper extends BaseMapper<GradeInfo> {

    GradeInfo selectOneByGradeIdAndStudentId(@Param("gradeId") Long gradeId, @Param("studentId") Long studentId);


    /**
     * 批量增加学生缺勤课时
     */
    void batchPlusMissPeriod(@Param("gradeId") Long gradeId, @Param("missPeriod") int missPeriod,
                             @Param("missStudentIds") List<Long> missStudentIds);

    /**
     * 批量增加学生缺勤课时
     */
    void batchPlusConsumePeriod(@Param("gradeId") Long gradeId, @Param("consumePeriod") int consumePeriod,
                                @Param("consumeStudentIds") List<Long> consumeStudentIds);


    /**
     * 获得学生
     */
    List<AttendStudentBO> getGradeStudents(Long gradeId);

    List<ScheduleExpandStudentListVo> getInfoByScheduleId(@Param("scheduleId") Long scheduleId);

    List<StudentGradeVO> getStudentGrades(@Param("studentId") Long studentId);
}




