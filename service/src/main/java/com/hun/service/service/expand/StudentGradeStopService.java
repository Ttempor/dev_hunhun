package com.hun.service.service.expand;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hun.bean.dto.expand.studentgradestop.QueryStudentStopDto;
import com.hun.bean.entity.expand.StudentGradeStop;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【student_grade_stop】的数据库操作Service
* @createDate 2023-04-16 22:35:08
*/
public interface StudentGradeStopService extends IService<StudentGradeStop> {

    /**
     *  条件查询
     * @param queryMissSchedulePageDto 饿
     * @return 饿
     */
    Page<StudentGradeStop> getPage(QueryStudentStopDto queryMissSchedulePageDto);

    /**
     * 通过班级id学生id查询
     */
    StudentGradeStop getOne(Long gradeId, Long studentId);

    /**
     * 充值停课余额
     * @param stopId 停课id
     * @param balance 余额
     */
    void stopGradeDeposit(Long stopId, BigDecimal balance);

    /**
     * 增加剩余锁额
     * @param stopId 停课id
     * @param balance 余额
     */
    void plusLockSpendBalance(Long stopId, BigDecimal balance);

    /**
     * 通过班级id获得某班级在某时间内的停课人数
     * @param gradeId 班级id
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return 人数
     */
    Integer getCountByGradeIdAndMonth(long gradeId, long startTimestamp, long endTimestamp);

    /**
     * 获得时间内停课, 通过gradeId进行分组去重
     * @param startTimestamp 开始时间
     * @param endTimestamp 结束时间
     * @return 分组
     */
    List<StudentGradeStop> getByMonthAndGroupByGradeId(long startTimestamp, long endTimestamp);
}
