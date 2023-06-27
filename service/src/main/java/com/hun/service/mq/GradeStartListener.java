package com.hun.service.mq;

import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.base.Schedule;
import com.hun.bean.enums.GradeState;
import com.hun.bean.enums.ScheduleState;
import com.hun.common.constants.MQ;
import com.hun.common.util.RedisUtil;
import com.hun.service.service.MqService;
import com.hun.service.service.base.GradeService;
import com.hun.service.service.base.ScheduleService;
import com.hun.service.service.expand.StudentScheduleService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RabbitListener(queues = MQ.START_GRADE_QUEUE)
public class GradeStartListener {
    @Resource
    private ScheduleService scheduleService;
    @Resource
    private StudentScheduleService studentScheduleService;
    @Resource
    private GradeService gradeService;
    @Resource
    private MqService mqService;

    /**
     * 上课
     */
    @RabbitHandler
    public void gradeStart(Long gradeId) {
        //排课时间至少离当前时间相距一小时分钟
        //计算上课时间,提前一分钟
        //两节课的时间至少间隔10分钟

        //查询班级
        Grade grade = gradeService.getOneGradeByGradeId(gradeId);

        //班级处于开课中状态才可以上课
        if (GradeState.isRun(grade)) {
            //计算出下一节课时间是否是现在
            Schedule schedule = scheduleService.getScheduleByNow(grade.getGradeId(), System.currentTimeMillis());

            if (schedule == null || ScheduleState.isEnd(schedule)) {
                //不是上课时间, 或者已上
                return;
            }

            //上锁.
            if (!RedisUtil.
                    set("GradeId:" + grade.getGradeId(),
                            1,
                            ((schedule.getEndTimestamp() - schedule.getStartTimestamp()) / 1000L) - 290L)) {
                return;
            }

            //是现在有课,改为上课中,不考虑事务
            gradeService.gradeIng(grade.getGradeId());
            //排课处于上课中,不考虑事务
            scheduleService.scheduleIng(schedule.getScheduleId());
            //排课处于上课中,不考虑事务
            studentScheduleService.scheduleIng(schedule.getScheduleId());
            //发送班级id,排课id,排课课时,到下课队列中
            mqService.sendGradeOver(gradeId, schedule);
        }
    }
}
