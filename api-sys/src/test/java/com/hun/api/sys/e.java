package com.hun.api.sys;

import com.hun.bean.entity.base.Schedule;
import com.hun.service.mapper.expand.StudentGradeStopMapper;
import com.hun.service.mapper.expand.StudentScheduleMapper;
import com.hun.service.service.MqService;
import com.hun.service.service.base.ScheduleService;
import com.hun.service.service.expand.GradeInfoService;
import com.hun.service.service.expand.StudentScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest
public class e {

    @Resource
    private StudentScheduleMapper studentScheduleMapper;
    @Resource
    private StudentGradeStopMapper studentsto;

    @Resource
    private ScheduleService scheduleService;

    @Test
    public void e() {
        long gradeId = 5L;
//        System.out.println(studentScheduleMapper.courseCancel(0, System.currentTimeMillis()));
//        System.out.println(studentScheduleMapper.callCourseCancel(0, System.currentTimeMillis()));
//        System.out.println(studentScheduleService.getTeacherList(0, System.currentTimeMillis()));
//        studentsto.getCountByGradeIdAndMonth(1, 1680278400000L, 1682870400000L);
//        studentsto.getCountByGradeIdAndMonth(2,1680278400000L, 1682870400000L);
//        studentsto.getCountByGradeIdAndMonth(4,1680278400000L, 1682870400000L);
//        studentsto.getCountByGradeIdAndMonth(5,1680278400000L, 1682870400000L);

//        scheduleService.getBatchByIds(Arrays.asList(1l,2l,3l, 4L,5l,6l,7l,8l,9l));
//        studentScheduleMapper.inScheduleByScheduleIds(Arrays.asList(1l,2l,3l, 4L,5l,6l,7l,8l,9l));
    }
}
