package com.hun.service.mapper.expand;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hun.bean.entity.expand.StudentGradeStop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【student_grade_stop】的数据库操作Mapper
* @createDate 2023-04-16 22:35:08
* @Entity com.hun.bean.entity.expand.StudentGradeStop
*/
public interface StudentGradeStopMapper extends BaseMapper<StudentGradeStop> {

    Integer getCountByGradeIdAndMonth(@Param("gradeId") long gradeId,
                                      @Param("startTimestamp")long startTimestamp,
                                      @Param("endTimestamp")long endTimestamp);


    List<StudentGradeStop> getByMonthAndGroupByGradeId(@Param("startTimestamp")long startTimestamp,
                                                       @Param("endTimestamp")long endTimestamp);
}




