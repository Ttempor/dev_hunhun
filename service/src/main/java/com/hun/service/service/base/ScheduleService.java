package com.hun.service.service.base;


import com.hun.bean.app.GradeScheduleBO;
import com.hun.bean.dto.base.schedule.SaveScheduleDto;
import com.hun.bean.entity.base.Schedule;
import com.hun.bean.vo.BuyScheduleVO;
import com.hun.bean.vo.SchedulePeriodVO;

import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【schedule】的数据库操作Service
* @createDate 2023-04-01 19:26:43
*/
public interface ScheduleService {


    /**
     * 通过班级id获得课表信息
     */
    List<Schedule> getScheduleByGradeId(Long gradeId);

    /**
     * 通过班级id获得未上的课表信息
     */
    List<Schedule> getWaitScheduleByGradeId(Long gradeId);

    /**
     * 通过班级id获得未上的课表信息, 包括是否已购
     */
    List<BuyScheduleVO> getBuyWaitScheduleByGradeId(Long gradeId, Long studentId);

    /**
     * 保存课表
     */
    Schedule trySaveSchedule(SaveScheduleDto saveScheduleDto);

    /**
     * 时间冲突查询
     */
    void timeOverlapCheck(Long gradeId, Long preStartTimestamp, Long endTimestamp);

    /**
     * 通过班级id获得已经排课和已上课总课时
     */
    SchedulePeriodVO getScheduleTotalPeriodByGradeId(Long gradeId);


    /**
     * 通过排课id获得排课
     */
    Schedule getByScheduleId(Long scheduleId);

    /**
     * 获得班级现在的上课排课
     */
    Schedule getScheduleByNow(Long gradeId, long nowTimestamp);

    /**
     * 排课处于上课中
     */
    void scheduleIng(Long scheduleId);

    /**
     * 排课已上
     */
    void scheduleEnd(Long scheduleId);

    /**
     * 获得下一节排课
     */
    Schedule getNextSchedule(Long gradeId);

    /**
     * 删除排课
     */
    void deleteSchedule(Long gradeId, Long scheduleId);


    /**
     * 获得时间内的排课, 通过gradeId进行分组去重
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return 分组
     */
    List<Schedule> getByMonthAndGroupByGradeId(long startTimestamp, long endTimestamp);

    /**
     * 增加排课当前学生人数
     */
    void plusNowStudentCount(Long scheduleId, int step);

    /**
     * 通过id修改schedule
     */
    void updateById(Schedule schedule);

    /**
     * 批量查询schedule
     * @param ids scheduleId
     * @return
     */
    List<Schedule> getBatchByIds(Long gradeId, List<Long> ids);

    /**
     * 通过班级id获得班级的排课, 上课中或已经结课不返回, 只查询未上的排课
     * @param gradeId e
     */
    List<GradeScheduleBO> getAppSchedules(Long gradeId);
}
