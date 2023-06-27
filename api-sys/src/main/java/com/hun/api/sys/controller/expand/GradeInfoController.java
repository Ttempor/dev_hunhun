package com.hun.api.sys.controller.expand;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.bo.ScheduleExpandStudentListVo;
import com.hun.bean.dto.expand.gradeinfo.DeleteGradeInfoDto;
import com.hun.bean.dto.expand.gradeinfo.QueryGradeInfoPageDto;
import com.hun.bean.dto.expand.recordgradedrop.QueryDropGradePageDto;
import com.hun.bean.entity.expand.GradeInfo;
import com.hun.bean.vo.RecordGradeDropVO;
import com.hun.common.response.BaseResponse;
import com.hun.service.service.base.CourseService;
import com.hun.service.service.base.GradeService;
import com.hun.service.service.base.StudentService;
import com.hun.service.service.expand.GradeInfoService;
import com.hun.service.service.expand.RecordGradeDropService;
import com.hun.service.service.expand.StudentScheduleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 班级信息
 */
@RestController
@RequestMapping("/api/gradeInfo")
@CrossOrigin
public class GradeInfoController {
    @Resource
    private GradeInfoService gradeInfoService;
    @Resource
    private GradeService gradeService;
    @Resource
    private CourseService courseService;
    @Resource
    private StudentService studentService;
    @Resource
    private StudentScheduleService studentScheduleService;
    @Resource
    private RecordGradeDropService recordGradeDropService;


    /**
     * 班级信息的条件分页
     */
    @GetMapping()
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<Page<GradeInfo>> getGradeInfoPage(@Validated QueryGradeInfoPageDto queryGradeInfoPageDto) {
        return BaseResponse.ok(gradeInfoService.getGradeInfoPage(queryGradeInfoPageDto));
    }

    /**
     * 获得班级里的学生
     */
    @GetMapping("/student")
//    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<List<GradeInfo>> getGradeInfoByGradeId(Long gradeId) {
        return BaseResponse.ok(gradeInfoService.getGradeInfoByGradeId(gradeId));
    }

    /**
     * 退课
     */
    @PutMapping("/tk")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> tk(@Validated @RequestBody DeleteGradeInfoDto deleteGradeInfoDto) {
        //退课删除班级学生, 保存退课原因
        gradeInfoService.tuiKe(deleteGradeInfoDto);
        //班级当前学生数-1
        gradeService.incrGradeNowStudentCountByGradeId(deleteGradeInfoDto.getGradeId(), -1);
        //学生班级数-1
        studentService.incrGradeCountByStudentId(deleteGradeInfoDto.getStudentId(), -1);
        //把学生的排课删除, 已上的课不删除
        studentScheduleService.deleteStudentSchedule(deleteGradeInfoDto.getStudentId(), deleteGradeInfoDto.getGradeId());
        courseService.rmCategoryCache();
        return BaseResponse.ok("成功");
    }

    /**
     * 获得退课信息
     */
    @GetMapping("/tk")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<Page<RecordGradeDropVO>> getDropPage(@Validated QueryDropGradePageDto queryDropGradePageDto) {
        return BaseResponse.ok(recordGradeDropService.getDropPage(queryDropGradePageDto));
    }

    /**
     * 排课id获得该课次的所有学生信息
     */
    @GetMapping("/schedule")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<List<ScheduleExpandStudentListVo>> getSchedule(@RequestParam Long scheduleId) {
        return BaseResponse.ok(gradeInfoService.getInfoByScheduleId(scheduleId));
    }
}
