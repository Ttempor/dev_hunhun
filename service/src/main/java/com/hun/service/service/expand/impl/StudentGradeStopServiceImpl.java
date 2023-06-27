package com.hun.service.service.expand.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hun.bean.dto.expand.studentgradestop.QueryStudentStopDto;
import com.hun.bean.entity.expand.StudentGradeStop;
import com.hun.service.service.expand.StudentGradeStopService;
import com.hun.service.mapper.expand.StudentGradeStopMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【student_grade_stop】的数据库操作Service实现
* @createDate 2023-04-16 22:35:08
*/
@Service
public class StudentGradeStopServiceImpl extends ServiceImpl<StudentGradeStopMapper, StudentGradeStop>
    implements StudentGradeStopService{
    @Resource
    private StudentGradeStopMapper studentGradeStopMapper;

    @Value("${default-page-size}")
    private long pageSize;
    @Override
    public Page<StudentGradeStop> getPage(QueryStudentStopDto queryMissSchedulePageDto) {
            QueryWrapper<StudentGradeStop> queryWrapper = new QueryWrapper<>();
            //是否要附上员工姓名条件
            if (queryMissSchedulePageDto.getStudentId() != null) {
                queryWrapper.eq("student_id", queryMissSchedulePageDto.getStudentId());
            }
            //是否要附上员工姓名条件
            if (queryMissSchedulePageDto.getStudentName() != null) {
                queryWrapper.like("student_name", queryMissSchedulePageDto.getStudentName().trim());
            }
            //是否要附上班级名条件
            if (queryMissSchedulePageDto.getGradeId() != null) {
                queryWrapper.eq("grade_id", queryMissSchedulePageDto.getGradeId());
            }
            //是否要附上班级名条件
            if (queryMissSchedulePageDto.getGradeName() != null) {
                queryWrapper.like("grade_name", queryMissSchedulePageDto.getGradeName().trim());
            }
            Page<StudentGradeStop> page = Page.of(queryMissSchedulePageDto.getP(), pageSize);
            return studentGradeStopMapper.selectPage(page, queryWrapper);
    }

    @Override
    public StudentGradeStop getOne(Long gradeId, Long studentId) {
        QueryWrapper<StudentGradeStop> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("grade_id", gradeId);
        queryWrapper.eq("student_id", studentId);
        return studentGradeStopMapper.selectOne(queryWrapper);
    }

    @Override
    public void stopGradeDeposit(Long stopId, BigDecimal balance) {
        UpdateWrapper<StudentGradeStop> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("deposit_lock_balance = deposit_lock_balance + " + balance);
        updateWrapper.eq("stop_id", stopId);
        studentGradeStopMapper.update(null,updateWrapper);
    }

    @Override
    public void plusLockSpendBalance(Long stopId, BigDecimal balance) {
        UpdateWrapper<StudentGradeStop> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("lock_spend_balance = lock_spend_balance + " + balance);
        updateWrapper.eq("stop_id", stopId);
        studentGradeStopMapper.update(null,updateWrapper);
    }

    @Override
    public Integer getCountByGradeIdAndMonth(long gradeId, long startTimestamp, long endTimestamp) {
        return studentGradeStopMapper.getCountByGradeIdAndMonth(gradeId, startTimestamp, endTimestamp);
    }

    @Override
    public List<StudentGradeStop> getByMonthAndGroupByGradeId(long startTimestamp, long endTimestamp) {
        return studentGradeStopMapper.getByMonthAndGroupByGradeId(startTimestamp, endTimestamp);
    }

}




