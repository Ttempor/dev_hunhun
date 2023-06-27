package com.hun.service.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hun.bean.app.GradeScheduleBO;
import com.hun.bean.entity.base.Schedule;
import com.hun.bean.vo.BuyScheduleVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【schedule】的数据库操作Mapper
* @createDate 2023-04-02 15:02:33
* @Entity com.dev.hunhun.entity.base.Schedule
*/
public interface ScheduleMapper extends BaseMapper<Schedule> {

    /**
     * 查询下一个排课
     */
    @Select("SELECT * FROM hun_base.schedule where grade_id = #{gradeId} and " +
            "#{ctm} < start_timestamp " +
            "ORDER BY start_timestamp " +
            "asc limit 1")
    Schedule selectNextSchedule(@Param("gradeId") Long gradeId, @Param("ctm") long currentTimeMillis);

    List<Schedule> getByMonthAndGroupByGradeId(@Param("startTimestamp") long startTimestamp, @Param("endTimestamp") long endTimestamp);

    void plusNowStudentCount(@Param("scheduleId") Long scheduleId, @Param("step") int i);

    List<BuyScheduleVO> getBuyWaitScheduleByGradeId(@Param("gradeId") Long gradeId, @Param("studentId") Long studentId);

    List<GradeScheduleBO> getAppSchedules(@Param("gradeId") Long gradeId);
}




