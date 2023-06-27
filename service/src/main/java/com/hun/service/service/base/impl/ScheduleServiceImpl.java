package com.hun.service.service.base.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hun.bean.app.GradeScheduleBO;
import com.hun.bean.dto.base.schedule.SaveScheduleDto;
import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.base.Schedule;
import com.hun.bean.enums.ChargeMethodCategory;
import com.hun.bean.vo.BuyScheduleVO;
import com.hun.bean.vo.SchedulePeriodVO;
import com.hun.bean.enums.GradeState;
import com.hun.bean.enums.ScheduleState;
import com.hun.common.exception.BusinessException;
import com.hun.service.mapper.base.ScheduleMapper;
import com.hun.service.service.base.GradeService;
import com.hun.service.service.base.ScheduleService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【schedule】的数据库操作Service实现
* @createDate 2023-04-01 19:26:43
*/
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Resource
    private ScheduleMapper scheduleMapper;
    @Resource
    private GradeService gradeService;
    @Resource
    private MapperFacade mapperFacade;

    /**
     * 通过班级id获得课表信息
     */
    @Override
    public List<Schedule> getScheduleByGradeId(Long gradeId) {
        QueryWrapper<Schedule> wrapper = new QueryWrapper<>();
        wrapper.eq("grade_id", gradeId);
        return scheduleMapper.selectList(wrapper);
    }
    /**
     * 通过班级id获得未上的课表信息
     */
    @Override
    public List<Schedule> getWaitScheduleByGradeId(Long gradeId) {
        QueryWrapper<Schedule> wrapper = new QueryWrapper<>();
        wrapper.eq("grade_id", gradeId);
        wrapper.eq("schedule_state", ScheduleState.WAIT.code());
        return scheduleMapper.selectList(wrapper);
    }


    @Override
    public List<BuyScheduleVO> getBuyWaitScheduleByGradeId(Long gradeId, Long studentId) {
        return scheduleMapper.getBuyWaitScheduleByGradeId(gradeId, studentId);
    }

    /**
     * 保存课表
     */
    @Override
    public Schedule trySaveSchedule(SaveScheduleDto saveScheduleDto) {
        if (saveScheduleDto.getStartTimestamp() - 1800000 <= System.currentTimeMillis()) {
            throw new BusinessException("上课时间至少要在30分钟后开始");
        }
        //查班级
        Grade grade = gradeService.getOneGradeByGradeId(saveScheduleDto.getGradeId());
        //非待排课状态,不可排课
        if (grade == null || !GradeState.isWait(grade)) {
            throw new BusinessException("非待排课状态,不可排课");
        }
        //查询课时已经排课课时
        Integer scheduleTotalPeriod = getScheduleTotalPeriodByGradeId(saveScheduleDto.getGradeId())
                .getScheduleTotalPeriod();
        //未被安排的课时需要小于或等于总课时
        if ((grade.getGradeTotalPeriod() - scheduleTotalPeriod) < saveScheduleDto.getConsumePeriod()) {
            throw new BusinessException("剩余课时不足");
        }
        //提前5分钟上课
        long preStartTimestamp = saveScheduleDto.getStartTimestamp() - 300000;
        //sql=3
        //防止上课时间冲突
        timeOverlapCheck(grade.getGradeId(), preStartTimestamp, saveScheduleDto.getEndTimestamp());
        Schedule schedule = mapperFacade.map(grade, Schedule.class);
        schedule.setGradeNowStudentCount(0);
//        schedule.setGradeMaxStudentCount(0);Bug
        schedule.setGradeRoom(saveScheduleDto.getGradeRoom().trim());
        //提前五分钟上课
        schedule.setStartTimestamp(preStartTimestamp);
        schedule.setStartDatetime(LocalDateTime.
                ofInstant(Instant.ofEpochMilli(saveScheduleDto.getStartTimestamp()), ZoneOffset.of("+8")));
        schedule.setEndTimestamp(saveScheduleDto.getEndTimestamp());
        schedule.setEndDatetime(LocalDateTime.
                ofInstant(Instant.ofEpochMilli(saveScheduleDto.getEndTimestamp()), ZoneOffset.of("+8")));
        schedule.setConsumePeriod(saveScheduleDto.getConsumePeriod());
        schedule.setScheduleState(ScheduleState.WAIT.code());
        //设置该节课收入为0
        schedule.setIncome(new BigDecimal("0.00"));
        //设置该节课的价钱
        if (ChargeMethodCategory.isPeriod(grade)) {
            //是按课时收费, 算出价钱= 课时 * 单价
            schedule.setPrice(schedule.getChargeMethodExpression().
                    multiply(new BigDecimal(String.valueOf(schedule.getConsumePeriod()))));
        } else {
            //按套餐收费, 是0;
            schedule.setPrice(new BigDecimal("0.00"));
        }
        //总共四次sql。。。
        //sql=4
        scheduleMapper.insert(schedule);
        //排课排满了,改为开课状态
        if (scheduleTotalPeriod + saveScheduleDto.getConsumePeriod() == grade.getGradeTotalPeriod()) {
            gradeService.gradeRunAndSendMq(grade.getGradeId());
            if (ChargeMethodCategory.isCombination(grade)) {
                //排课排满了, 是按套餐收费
                schedule.setPrice(grade.getPrice());
            }
        }
        return schedule;
    }

    @Override
    public void timeOverlapCheck(Long gradeId, Long preStartTimestamp, Long endTimestamp) {
        //防止上课时间冲突
        QueryWrapper<Schedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("grade_id", gradeId);
        queryWrapper.and((wrapper) -> {
            wrapper.and((w) -> {
                w.le("start_timestamp", preStartTimestamp);
                w.ge("end_timestamp", preStartTimestamp);
            });
            wrapper.or((w) -> {
                w.le("start_timestamp", endTimestamp);
                w.ge("end_timestamp", endTimestamp);
            });
        });
        //sql=3
        //查出来就表明存在冲突的排课
        if(scheduleMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("排课时间冲突,两节课之间至少间隔5分钟");
        }
    }


    /**
     * 通过班级id获得已经排课课时和已上课总课时
     */
    @Override
    public SchedulePeriodVO getScheduleTotalPeriodByGradeId(Long gradeId) {
        //统计已经排课的总课时
        QueryWrapper<Schedule> queryWrapper = new QueryWrapper<>();
        SchedulePeriodVO schedulePeriodVO = new SchedulePeriodVO();
        //统计已排课课时
        queryWrapper.eq("grade_id", gradeId);
        List<Schedule> schedules = scheduleMapper.selectList(queryWrapper);
        int total = 0;
        int endTotal = 0;
        for (Schedule schedule : schedules) {
            total += schedule.getConsumePeriod();
            if (ScheduleState.isEnd(schedule)) {
                //统计已经排课且已经上课课时
                endTotal += schedule.getConsumePeriod();
            }
        }
        schedulePeriodVO.setScheduleTotalPeriod(total);
        schedulePeriodVO.setEndScheduleTotalPeriod(endTotal);
        return schedulePeriodVO;
    }


    /**
     * 通过排课id获得排课
     */
    @Override
    public Schedule getByScheduleId(Long scheduleId) {
        return scheduleMapper.selectById(scheduleId);
    }
    /**
     * 获得班级现在的上课排课
     */

    @Override
    public Schedule getScheduleByNow(Long gradeId, long nowTimestamp) {
        QueryWrapper<Schedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("grade_id", gradeId);
        queryWrapper.le("start_timestamp", nowTimestamp);
        queryWrapper.ge("end_timestamp", nowTimestamp);
        return scheduleMapper.selectOne(queryWrapper);
    }

    @Override
    public void scheduleIng(Long scheduleId) {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(scheduleId);
        schedule.setScheduleState(ScheduleState.ING.code());
        scheduleMapper.updateById(schedule);
    }

    @Override
    public void scheduleEnd(Long scheduleId) {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(scheduleId);
        schedule.setScheduleState(ScheduleState.END.code());
        scheduleMapper.updateById(schedule);
    }


    /**
     * 获得下一节排课
     */

    @Override
    public Schedule getNextSchedule(Long gradeId) {
        return scheduleMapper.selectNextSchedule(gradeId, System.currentTimeMillis());
    }


    /**
     * 删除排课
     */
    @Override
    public void deleteSchedule(Long gradeId, Long scheduleId) {
        UpdateWrapper<Schedule> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("schedule_id", scheduleId);
        updateWrapper.eq("grade_id", gradeId);
        scheduleMapper.delete(updateWrapper);
    }

    @Override
    public List<Schedule> getByMonthAndGroupByGradeId(long startTimestamp, long endTimestamp) {
        return scheduleMapper.getByMonthAndGroupByGradeId(startTimestamp, endTimestamp);
    }


    @Override
    public void plusNowStudentCount(Long scheduleId, int step) {
        scheduleMapper.plusNowStudentCount(scheduleId, step);
    }

    @Override
    public void updateById(Schedule schedule) {
        scheduleMapper.updateById(schedule);
    }

    @Override
    public List<Schedule> getBatchByIds(Long gradeId, List<Long> ids) {
        QueryWrapper<Schedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("grade_id", gradeId);
        queryWrapper.in("schedule_id", ids);
        return scheduleMapper.selectList(queryWrapper);
    }

    @Override
    public List<GradeScheduleBO> getAppSchedules(Long gradeId) {
        return scheduleMapper.getAppSchedules(gradeId);
    }
}




