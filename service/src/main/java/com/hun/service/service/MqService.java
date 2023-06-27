package com.hun.service.service;

import com.hun.bean.entity.base.Schedule;
import com.hun.common.constants.MQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
@Slf4j
public class MqService {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendGradeStart(Long gradeId, Schedule schedule) {
        log.info("rabbitMQ:nextStart:{}", Math.toIntExact(schedule.getStartTimestamp() - System.currentTimeMillis()));
        //发送到队列中
        rabbitTemplate.convertAndSend(MQ.START_GRADE_EXCHANGE, MQ.START_GRADE_ROUTER_KEY, gradeId, (message -> {
            message.getMessageProperties().
                    setDelay(Math.toIntExact(schedule.getStartTimestamp() - System.currentTimeMillis()));
            return message;
        }));
    }

    public void sendGradeOver(Long gradeId, Schedule schedule) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gradeId", gradeId);
        hashMap.put("scheduleId", schedule.getScheduleId());
        hashMap.put("schedulePeriod", schedule.getConsumePeriod());
        //发送到队列中
        rabbitTemplate.convertAndSend(MQ.END_GRADE_EXCHANGE, MQ.END_GRADE_ROUTER_KEY, hashMap, (message -> {
                    message.getMessageProperties().
                            setDelay(Math.toIntExact(schedule.getEndTimestamp() - schedule.getStartTimestamp()));
                    return message;
                }));
    }
}
