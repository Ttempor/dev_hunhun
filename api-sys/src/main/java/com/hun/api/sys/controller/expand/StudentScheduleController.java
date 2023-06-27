package com.hun.api.sys.controller.expand;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.dto.expand.attend.QueryAttendScheduleDto;
import com.hun.bean.dto.expand.miss.InsertMissScheduleDto;
import com.hun.bean.dto.expand.miss.QueryMissScheduleDto;
import com.hun.bean.entity.base.Schedule;
import com.hun.bean.entity.expand.GradeInfo;
import com.hun.bean.entity.expand.StudentSchedule;
import com.hun.bean.enums.ScheduleState;
import com.hun.common.exception.BusinessException;
import com.hun.common.response.BaseResponse;
import com.hun.security.common.util.AuthUserContext;
import com.hun.service.service.base.CourseService;
import com.hun.service.service.base.ScheduleService;
import com.hun.service.service.expand.GradeInfoService;
import com.hun.service.service.expand.StudentScheduleService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 缺勤
 */
@RestController
@RequestMapping("/api/studentSchedule")
@CrossOrigin
public class StudentScheduleController {
    @Resource
    private StudentScheduleService studentScheduleService;
    @Resource
    private ScheduleService scheduleService;
    @Resource
    private CourseService courseService;
    @Resource
    private GradeInfoService gradeInfoService;
    @Resource
    private MapperFacade mapperFacade;


    /**
     * 条件查询
     */
    @GetMapping()
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<List<StudentSchedule>> getPage(@RequestParam Long gradeId, @RequestParam Long studentId) {
        return BaseResponse.ok(studentScheduleService.getPage(gradeId, studentId));
    }

    /**
     * 调班
     */
    @PutMapping()
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> tb(@RequestParam("stScId") Long scheduleStudentId,
                                   @RequestParam("stId") Long studentId,
                                   @RequestParam("sScId") Long sourceScheduleId,
                                   @RequestParam("tScId") Long targetScheduleId) {
        //获得目标排课
        Schedule targetSchedule = scheduleService.getByScheduleId(targetScheduleId);
        if (targetSchedule == null || !ScheduleState.isWAIT(targetSchedule)) {
            throw new BusinessException("该课不存在或者已上");
        }
        if (targetSchedule.getGradeNowStudentCount().compareTo(targetSchedule.getGradeMaxStudentCount()) >= 0){
            throw new BusinessException("人数已满, 无法加入");
        }
        //防止调课后上了两节相同的课
        if (studentScheduleService.getByScheduleIdAndStudentId(targetScheduleId, studentId) != null) {
            throw new BusinessException("重复调课");
        }
        StudentSchedule studentSchedule = new StudentSchedule();

        studentSchedule.setStudentScheduleId(scheduleStudentId);
        studentSchedule.setScheduleId(targetSchedule.getScheduleId());
        studentSchedule.setGradeId(targetSchedule.getGradeId());
        studentSchedule.setGradeName(targetSchedule.getGradeName());
        studentSchedule.setTeacherId(targetSchedule.getTeacherId());
        studentSchedule.setEmployeeName(targetSchedule.getEmployeeName());
        studentSchedule.setGradeRoom(targetSchedule.getGradeRoom());
        studentSchedule.setStartTimestamp(targetSchedule.getStartTimestamp());
        studentSchedule.setStartDatetime(targetSchedule.getStartDatetime());
        studentSchedule.setEndTimestamp(targetSchedule.getEndTimestamp());
        studentSchedule.setEndDatetime(targetSchedule.getEndDatetime());
        //改老师id 老师名 班级id 班级名 上课事件 下课事件 排课id
        studentScheduleService.updateById(studentSchedule);
        //原schedule人数减少
        scheduleService.plusNowStudentCount(sourceScheduleId, -1);
        //目标schedule人数增加
        scheduleService.plusNowStudentCount(targetScheduleId, 1);
        courseService.rmCategoryCache();
        return BaseResponse.ok("成功");
    }


    /**
     * 查询老师已上的课的学生列表
     */
    @GetMapping("/student/range")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<List<StudentSchedule>> getTeacherStudentDay(@RequestParam Long teacherId,
                                                                    @RequestParam Long startTimestamp,
                                                                    @RequestParam Long endTimestamp) {
        //查询出勤和未出勤学生
        return BaseResponse.ok(studentScheduleService.getRange(teacherId, startTimestamp, endTimestamp));
    }

    /**
     * 出勤的条件分页
     */
    @GetMapping("/attendSchedule")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<Page<StudentSchedule>> getAttendSchedulePage(@Validated QueryAttendScheduleDto queryAttendScheduleDto) {
        return BaseResponse.ok(studentScheduleService.getAttendSchedulePage(queryAttendScheduleDto));
    }

    /**
     * 缺勤的条件分页
     */
    @GetMapping("/missSchedule")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<Page<StudentSchedule>> getMissSchedulePage(@Validated QueryMissScheduleDto queryMissSchedulePageDto) {
        return BaseResponse.ok(studentScheduleService.getMissSchedulePage(queryMissSchedulePageDto));
    }


    /**
     * 保存缺勤
     */
    @PostMapping("/missSchedule")
    @PreAuthorize("@pms.hasPermission('employee')")
    public BaseResponse<String> missSchedule(@Validated @RequestBody InsertMissScheduleDto insertMissScheduleDto) {
        //获得老师id
        String tid = AuthUserContext.get().getUserId();
        tid = tid.substring(9, tid.length() - 2);
        Long teacherId = Long.valueOf(tid);
        Long gradeId = insertMissScheduleDto.getGradeId();
        Schedule scheduleByNow = scheduleService.getScheduleByNow(gradeId, System.currentTimeMillis());
        if (scheduleByNow == null) {
            throw new BusinessException("班级未上课,无法标记缺勤");
        }
        if (!scheduleByNow.getTeacherId().equals(teacherId)) {
            throw new BusinessException("该老师不是该节课任课老师");
        }
        //查看该学生是否已经缺勤
        StudentSchedule studentSchedule = studentScheduleService.studentIsMissSchedule(scheduleByNow.getScheduleId(),
                insertMissScheduleDto.getStudentId());
        if (studentSchedule != null) {
            throw new BusinessException("学生已经缺勤");
        }
        GradeInfo gradeInfo = gradeInfoService.getOneByGradeIdAndStudentId(gradeId,
                insertMissScheduleDto.getStudentId());
        if (gradeInfo == null) {
            throw new BusinessException("学生不在该班级中");
        }
        //记录缺勤
        studentScheduleService.miss(scheduleByNow.getScheduleId(), insertMissScheduleDto.getStudentId(),
                insertMissScheduleDto.getMissReason());
        courseService.rmCategoryCache();
        return BaseResponse.ok("成功");
    }
}
