package com.hun.service.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hun.bean.bo.AttendStudentBO;
import com.hun.bean.entity.base.Student;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【student_dev】的数据库操作Mapper
* @createDate 2023-04-03 15:42:47
* @Entity com.dev.hunhun.entity.base.Student
*/
public interface StudentMapper extends BaseMapper<Student> {
    /**
     * 通过学生id使学生拥有班级数增加
     */
    @Update("UPDATE `hun_base`.`student_dev` SET `grade_count` = `grade_count` + #{step} " +
            "WHERE `student_id` = #{studentId}")
    void incrGradeCountByStudentId(@Param("studentId") Long studentId, @Param("step") int step);

    /**
     * 批量消费减少余额和锁定余额
     */
    void batchSubtractBalanceAndLockBalance(@Param("schedulePrice") BigDecimal schedulePrice,
                                            @Param("attendStudentIds") List<Long> attendStudentIds);

}




