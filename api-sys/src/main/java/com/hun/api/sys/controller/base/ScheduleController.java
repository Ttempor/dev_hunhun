package com.hun.api.sys.controller.base;


import com.hun.bean.bo.AttendStudentBO;
import com.hun.bean.dto.base.schedule.DeleteScheduleDto;
import com.hun.bean.dto.base.schedule.SaveScheduleDto;
import com.hun.bean.dto.base.schedule.UpdateScheduleDto;
import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.base.Schedule;
import com.hun.bean.entity.expand.GradeInfo;
import com.hun.bean.entity.expand.StudentSchedule;
import com.hun.bean.enums.GradeState;
import com.hun.bean.enums.ScheduleState;
import com.hun.bean.vo.BuyScheduleVO;
import com.hun.bean.vo.SchedulePeriodVO;
import com.hun.common.exception.BaseNormativeVerifyException;
import com.hun.common.exception.BusinessException;
import com.hun.common.response.BaseResponse;
import com.hun.service.service.base.CourseService;
import com.hun.service.service.base.GradeService;
import com.hun.service.service.base.ScheduleService;
import com.hun.service.service.expand.GradeInfoService;
import com.hun.service.service.expand.StudentScheduleService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;

/**
 * 课表
 */
@RestController
@RequestMapping("/api/schedule")
@CrossOrigin
public class ScheduleController {
    @Resource
    private ScheduleService scheduleService;
    @Resource
    private GradeService gradeService;
    @Resource
    private CourseService courseService;
    @Resource
    private GradeInfoService gradeInfoService;
    @Resource
    private StudentScheduleService studentScheduleService;
    @Resource
    private MapperFacade mapperFacade;


    /**
     * 通过班级id获得课表信息
     */
    @GetMapping
    public BaseResponse<List<Schedule>> getScheduleByGradeId(Long gradeId) {
        return BaseResponse.ok(scheduleService.getScheduleByGradeId(gradeId));
    }
    /**
     * 通过班级id获得未上的课表信息
     */
    @GetMapping("wait")
    public BaseResponse<List<Schedule>> getWaitScheduleByGradeId(Long gradeId) {
        return BaseResponse.ok(scheduleService.getWaitScheduleByGradeId(gradeId));
    }

    /**
     * 通过班级id获得未上的课表信息, 包括是否已购
     */
    @GetMapping("/buy/wait")
    public BaseResponse<List<BuyScheduleVO>> getBuyWaitScheduleByGradeId(Long gradeId, Long studentId) {
        return BaseResponse.ok(scheduleService.getBuyWaitScheduleByGradeId(gradeId, studentId));
    }

    /**
     * 通过班级id获得已经排课课时和已上课总课时
     */
    @GetMapping("/period")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<SchedulePeriodVO> getScheduleTotalPeriodByGradeId(Long gradeId) {
        return BaseResponse.ok(scheduleService.getScheduleTotalPeriodByGradeId(gradeId));
    }

    /**
     * 保存课表
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> saveSchedule(@Validated @RequestBody SaveScheduleDto saveScheduleDto) {
        if (saveScheduleDto.getEndTimestamp() - saveScheduleDto.getStartTimestamp() < 300000) {
            throw new BaseNormativeVerifyException("要求每节课时长最小为五分钟");
        }
        //尝试保存排课，保存成功后，返回排课信息
        Schedule schedule = scheduleService.trySaveSchedule(saveScheduleDto);

        courseService.rmCategoryCache();
        /*
        //映射成学生排课
        StudentSchedule studentSchedule = mapperFacade.map(schedule, StudentSchedule.class);
        //设置源班级id,源排课id
        studentSchedule.setSourceScheduleId(schedule.getScheduleId());
        studentSchedule.setSourceGradeId(schedule.getGradeId());
        //获得班级下所有学生
        List<AttendStudentBO> gradeStudents = gradeInfoService.getGradeStudents(saveScheduleDto.getGradeId());
        //批量保存到每个学生的个人排课中
        for (AttendStudentBO gradeStudent : gradeStudents) {
            //设置null，使用自增主键
            studentSchedule.setStudentScheduleId(null);
            studentSchedule.setStudentId(gradeStudent.getStudentId());
            studentSchedule.setStudentName(gradeStudent.getStudentName());
            studentScheduleService.save(studentSchedule);
        }
         */
        return BaseResponse.ok("添加成功");
    }

    /**
     * 删除课表
     */
//    @PutMapping("/delete")
//    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> deleteSchedule(@Validated @RequestBody DeleteScheduleDto deleteScheduleDto) {
        //删除排课,排课id,要求班级处于排课中
        Grade grade = gradeService.getOneGradeByGradeId(deleteScheduleDto.getGradeId());
        if (!GradeState.isWait(grade)) {
            throw new BusinessException("班级处于排课中才能排课");
        }
        //删除排课
        scheduleService.deleteSchedule(deleteScheduleDto.getGradeId(), deleteScheduleDto.getScheduleId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("schedule_id", deleteScheduleDto.getScheduleId());
        //删除学生的排课信息
        studentScheduleService.removeByMap(map);
        courseService.rmCategoryCache();
        return BaseResponse.ok("删除成功");
    }


    /**
     * 修改课表上课时间和教室
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> updateSchedule(@Validated @RequestBody UpdateScheduleDto updateScheduleDto) {
        Schedule schedule = scheduleService.getByScheduleId(updateScheduleDto.getScheduleId());
        //提前5分钟上课, 让每节课的间隔至少有5分钟
        long preStartTimestamp = updateScheduleDto.getStartTimestamp() - 300000;
        //防止上课时间冲突
        scheduleService.timeOverlapCheck(schedule.getGradeId(), preStartTimestamp, updateScheduleDto.getEndTimestamp());
        if (!ScheduleState.isWAIT(schedule)) {
            throw new BusinessException("未上课才能修改排课");
        }
        if (updateScheduleDto.getEndTimestamp() - updateScheduleDto.getStartTimestamp() < 300000) {
            throw new BaseNormativeVerifyException("要求每节课时长最小为五分钟");
        }
        if (updateScheduleDto.getStartTimestamp() - 1800000 <= System.currentTimeMillis()) {
            throw new BusinessException("上课时间至少要在30分钟后开始");
        }


        schedule = new Schedule();
        schedule.setScheduleId(updateScheduleDto.getScheduleId());
        schedule.setGradeRoom(updateScheduleDto.getGradeRoom());
        schedule.setStartTimestamp(preStartTimestamp);
        schedule.setStartDatetime(LocalDateTime.ofInstant(Instant.ofEpochMilli(updateScheduleDto.getStartTimestamp()),
                ZoneOffset.of("+8")));
        schedule.setEndTimestamp(updateScheduleDto.getEndTimestamp());
        schedule.setEndDatetime(LocalDateTime.ofInstant(Instant.ofEpochMilli(updateScheduleDto.getEndTimestamp()),
                ZoneOffset.of("+8")));
        scheduleService.updateById(schedule);

        StudentSchedule studentSchedule = new StudentSchedule();
        studentSchedule.setScheduleId(updateScheduleDto.getScheduleId());
        studentSchedule.setGradeRoom(schedule.getGradeRoom());
        studentSchedule.setStartTimestamp(preStartTimestamp);
        studentSchedule.setStartDatetime(schedule.getStartDatetime());
        studentSchedule.setEndTimestamp(schedule.getEndTimestamp());
        studentSchedule.setEndDatetime(schedule.getEndDatetime());
        studentScheduleService.updateByScheduleId(studentSchedule);
        return BaseResponse.ok("添加成功");
    }
}
