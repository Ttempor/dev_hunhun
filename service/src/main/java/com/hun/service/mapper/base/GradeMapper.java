package com.hun.service.mapper.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hun.bean.app.AppGradeVO;
import com.hun.bean.app.CategoryItemVO;
import com.hun.bean.app.StudentGradeVO;
import com.hun.bean.entity.base.Grade;
import com.hun.bean.vo.GradeVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【grade】的数据库操作Mapper
* @createDate 2023-03-25 18:35:44
* @Entity com.dev.hunhun.entity.base.Grade
*/
public interface GradeMapper extends BaseMapper<Grade> {

    int updateTeacherInfoByGradeId(@Param("teacherId") Long teacherId,
                                                @Param("employeeName") String employeeName,
                                                @Param("gradeId") Long gradeId);

    int updateGradeMaxStudentCountByGradeId(@Param("gradeMaxStudentCount") Integer gradeMaxStudentCount, @Param("gradeId") Long gradeId);


    /**
     * 通过老师id使老师拥有班级数增加
     */
    void incrGradeNowStudentCountByGradeId(@Param("gradeId")Long gradeId, @Param("step") int step);



    /**
     * 获得所有待排课和停课中且人数未满的班级id和班级名
     * @return e e
     */
    @Select("select grade_id, grade_name, grade_state, price, employee_name, charge_method_id from `hun_base`.`grade` where (grade_now_student_count < grade_max_student_count) " +
            "and (grade_state = 0 or grade_state = 1) ")
    List<GradeVO> selectGradeIdAndGradeName();


    /**
     * 获得待排课状态和停课状态的班级id和班级名
     * @return e e
     */
    @Select("select grade_id, grade_name, grade_state, price from `hun_base`.`grade` ${ew.customSqlSegment} ")
    List<GradeVO> getGradeListByState(@Param(Constants.WRAPPER) QueryWrapper<Grade> queryWrapper);


    /**
     * 收入
     * @param gradeId 班级id
     * @param income 收入
     * @return 影响行数
     */
    int income(@Param("gradeId") Long gradeId, @Param("income") BigDecimal income);

    Integer getNowStudentCount(@Param("gradeId")Long gradeId);

    List<CategoryItemVO> getCategoryByCourseId(@Param("courseId") Long courseId);

    AppGradeVO getCategoryGrade(@Param("gradeId") Long gradeId);

}




