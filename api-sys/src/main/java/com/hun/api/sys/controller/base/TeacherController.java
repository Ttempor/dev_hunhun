package com.hun.api.sys.controller.base;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.dto.base.teacher.QueryTeacherPageDto;
import com.hun.bean.dto.base.teacher.SaveTeacherDto;
import com.hun.bean.entity.base.Teacher;
import com.hun.bean.vo.TeacherVO;
import com.hun.common.response.BaseResponse;
import com.hun.service.service.base.TeacherService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 老师
 */
@RestController
@RequestMapping("/api/teacher")
@CrossOrigin
public class TeacherController {
    @Resource
    private TeacherService teacherService;


    /**
     * 老师的条件分页
     * @param queryTeacherPageDto e
     */
    @GetMapping()
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<Page<Teacher>> getTeacherByPage(@Validated QueryTeacherPageDto queryTeacherPageDto) {
        return BaseResponse.ok(teacherService.getTeacherByPage(queryTeacherPageDto));
    }

    /**
     * 添加一个老师
     * @param saveTeacherDto e
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<String> saveOneTeacher(@Validated @RequestBody SaveTeacherDto saveTeacherDto) {
        teacherService.saveTeacher(saveTeacherDto);
        return BaseResponse.ok("保存成功");
    }

    /**
     * 获得所有老师的老师id和员工名
     */
    @GetMapping("/list")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<List<TeacherVO>> getTeacherList() {
        return BaseResponse.ok(teacherService.getTeacherList());
    }

    /**
     * 通过老师id查询一个老师
     */
    @GetMapping("/one")
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<Teacher> getTeacherOne(Long teacherId) {
        return BaseResponse.ok(teacherService.getTeacherOne(teacherId));
    }


}
