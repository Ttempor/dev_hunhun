package com.hun.service.mapper.expand;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.entity.expand.RecordGradeDrop;
import com.hun.bean.vo.RecordGradeDropVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【record_grade_drop】的数据库操作Mapper
* @createDate 2023-04-05 15:51:29
* @Entity com.dev.hunhun.entity.expand.RecordGradeDrop
*/
public interface RecordGradeDropMapper extends BaseMapper<RecordGradeDrop> {

    @Select("SELECT record_grade_drop_id,grade_info_id,student_id,student_name,course_id," +
            "course_name,grade_id,grade_name,teacher_id,employee_name,charge_method_id," +
            "charge_method_name,charge_method_expression,consume_period,miss_period,total_period," +
            "spend_balance,lock_balance,lock_spend_balance,grade_state,drop_datetime,drop_reason " +
            "FROM hun_expand.record_grade_drop ${ew.customSqlSegment}")
    Page<RecordGradeDropVO> selectByPage(Page<RecordGradeDropVO> page,
                                         @Param(Constants.WRAPPER) QueryWrapper<RecordGradeDrop> queryWrapper);

    Integer getCountByGradeIdAndMonth(@Param("gradeId") long gradeId,
                                      @Param("startTimestamp")long startTimestamp,
                                      @Param("endTimestamp")long endTimestamp);

    List<RecordGradeDrop> getByMonthAndGroupByGradeId(@Param("startTimestamp")long startTimestamp,
                                                       @Param("endTimestamp")long endTimestamp);
}




