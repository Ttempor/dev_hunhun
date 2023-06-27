package com.hun.service.service.base;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.dto.base.teacher.QueryTeacherPageDto;
import com.hun.bean.dto.base.teacher.SaveTeacherDto;
import com.hun.bean.dto.base.teacher.UpdateTeacherByTeacherIdDto;
import com.hun.bean.entity.base.Teacher;
import com.hun.bean.vo.TeacherVO;

import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【teacher_dev】的数据库操作Service
* @createDate 2023-03-25 23:25:09
*/
public interface TeacherService {

    /**
     * 分页获得老师
     * @param queryTeacherPageDto
     * @return
     */
    Page<Teacher> getTeacherByPage(QueryTeacherPageDto queryTeacherPageDto);
    /**
     * 保存一个老师
     * @param saveTeacherDto
     * @return
     */
    void saveTeacher(SaveTeacherDto saveTeacherDto);

    /**
     * 通过老师id获得一个老师
     */
    Teacher getOneTeacherByTeacherId(Long id);

    /**
     * 通过员工id修改老师信息
     */
    void updateTeacherByTeacherId(UpdateTeacherByTeacherIdDto updateTeacherDto, boolean trim);

    /**
     * 通过员工id获得一个老师
     */
    Teacher getOneTeacherByEmployeeId(Long id);
    /**
     * 通过姓名获得多个老师, 老师姓名可能重名
     * @param name
     * @return
     */
    List<Teacher> getListTeacherByName(String name);

    /**
     * 通过老师id使老师拥有班级数增加
     */
    void incrGradeCountByTeacherId(Long teacherId, int step);


    /**
     * 获得所有老师的老师id和员工名
     */
    List<TeacherVO> getTeacherList();

    /**
     * 通过老师id查询一个老师
     * @return e
     */
    Teacher getTeacherOne(Long teacherId);
}
