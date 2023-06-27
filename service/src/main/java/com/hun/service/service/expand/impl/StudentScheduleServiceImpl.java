package com.hun.service.service.expand.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hun.bean.app.StudentScheduleVO;
import com.hun.bean.bo.FinancialAnalysisHeaderBO;
import com.hun.bean.bo.FinancialAnalysisTeacherBO;
import com.hun.bean.dto.expand.attend.QueryAttendScheduleDto;
import com.hun.bean.dto.expand.miss.QueryMissScheduleDto;
import com.hun.bean.entity.base.Schedule;
import com.hun.bean.entity.expand.GradeInfo;
import com.hun.bean.entity.expand.StudentSchedule;
import com.hun.bean.enums.ScheduleState;
import com.hun.bean.vo.FinancialAnalysisTeacherVO;
import com.hun.service.service.base.ScheduleService;
import com.hun.service.service.expand.StudentScheduleService;
import com.hun.service.mapper.expand.StudentScheduleMapper;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【student_schedule】的数据库操作Service实现
* @createDate 2023-04-16 22:35:05
*/
@Service
public class StudentScheduleServiceImpl extends ServiceImpl<StudentScheduleMapper, StudentSchedule>
    implements StudentScheduleService{

    @Resource
    private StudentScheduleMapper studentScheduleMapper;
    @Resource
    private ScheduleService scheduleService;
    @Resource
    private MapperFacade mapperFacade;

    @Value("${default-page-size}")
    private long pageSize;
    @Override
    public void deleteStudentSchedule(Long studentId, Long gradeId) {
        QueryWrapper<StudentSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id", studentId);
        queryWrapper.eq("grade_id", gradeId);
        queryWrapper.ne("schedule_state", ScheduleState.END.code());
        studentScheduleMapper.delete(queryWrapper);
    }

    @Override
    public List<StudentSchedule> getPage(Long gradeId, Long studentId) {
        QueryWrapper<StudentSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id", studentId);
        queryWrapper.eq("source_grade_id", gradeId);
        return studentScheduleMapper.selectList(queryWrapper);
    }


    @Override
    public StudentSchedule getByScheduleIdAndStudentId(Long targetScheduleId, Long studentId) {
        QueryWrapper<StudentSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("schedule_id", targetScheduleId);
        queryWrapper.eq("student_id", studentId);
        return studentScheduleMapper.selectOne(queryWrapper);
    }



    @Override
    public void save(Long gradeId, Long studentId, String studentName) {
        //排课导入到学生个人排课中
        StudentSchedule studentSchedule = new StudentSchedule();
        studentSchedule.setStudentId(studentId);
        studentSchedule.setStudentName(studentName);

        for (Schedule schedule : scheduleService.getScheduleByGradeId(gradeId)) {
            mapperFacade.map(schedule, studentSchedule);
            studentSchedule.setStudentScheduleId(null);
            studentSchedule.setSourceScheduleId(schedule.getScheduleId());
            studentSchedule.setSourceGradeId(gradeId);
            save(studentSchedule);
        }
    }

    @Override
    public List<StudentSchedule> getRange(Long teacherId, long startTimestamp, long endTimestamp) {
        QueryWrapper<StudentSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id", teacherId);
        queryWrapper.eq("schedule_state", ScheduleState.END.code());
        queryWrapper.ge("start_timestamp", startTimestamp);
        queryWrapper.le("end_timestamp", endTimestamp);
        return studentScheduleMapper.selectList(queryWrapper);
    }

    @Override
    public Page<StudentSchedule> getAttendSchedulePage(QueryAttendScheduleDto queryAttendScheduleDto) {
        QueryWrapper<StudentSchedule> queryWrapper = new QueryWrapper<>();
        if (queryAttendScheduleDto.getStudentId() != null) {
            queryWrapper.eq("student_id", queryAttendScheduleDto.getStudentId());
        }
        if (queryAttendScheduleDto.getStudentName() != null) {
            queryWrapper.like("studentName_name", queryAttendScheduleDto.getStudentName().trim());
        }
        if (queryAttendScheduleDto.getGradeId() != null) {
            queryWrapper.eq("grade_id", queryAttendScheduleDto.getGradeId());
        }
        if (queryAttendScheduleDto.getStudentName() != null) {
            queryWrapper.like("grade_name", queryAttendScheduleDto.getStudentName().trim());
        }
        queryWrapper.eq("schedule_state", ScheduleState.END.code());
        queryWrapper.isNull("miss_reason");
        Page<StudentSchedule> page = Page.of(queryAttendScheduleDto.getP(), pageSize);
        return studentScheduleMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Page<StudentSchedule> getMissSchedulePage(QueryMissScheduleDto queryMissSchedulePgeDato) {
        QueryWrapper<StudentSchedule> queryWrapper = new QueryWrapper<>();
        if (queryMissSchedulePgeDato.getStudentId() != null) {
            queryWrapper.eq("student_id", queryMissSchedulePgeDato.getStudentId());
        }
        if (queryMissSchedulePgeDato.getStudentName() != null) {
            queryWrapper.like("studentName_name", queryMissSchedulePgeDato.getStudentName().trim());
        }
        if (queryMissSchedulePgeDato.getGradeId() != null) {
            queryWrapper.eq("grade_id", queryMissSchedulePgeDato.getGradeId());
        }
        if (queryMissSchedulePgeDato.getStudentName() != null) {
            queryWrapper.like("grade_name", queryMissSchedulePgeDato.getStudentName().trim());
        }
        queryWrapper.eq("schedule_state", ScheduleState.END.code());
        queryWrapper.isNotNull("miss_reason");
        Page<StudentSchedule> page = Page.of(queryMissSchedulePgeDato.getP(), pageSize);
        return studentScheduleMapper.selectPage(page, queryWrapper);
    }

    @Override
    public StudentSchedule studentIsMissSchedule(Long scheduleId, Long studentId) {
        QueryWrapper<StudentSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("schedule_id", scheduleId);
        queryWrapper.eq("student_id", studentId);
        queryWrapper.isNull("miss_reason");
        return studentScheduleMapper.selectOne(queryWrapper);
    }

    @Override
    public void miss(Long scheduleId, Long studentId, String missReason) {
        UpdateWrapper<StudentSchedule> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("schedule_id", scheduleId);
        updateWrapper.eq("student_id", studentId);
        updateWrapper.set("miss_reason", missReason);
        studentScheduleMapper.update(null, updateWrapper);
    }

    @Override
    public List<StudentSchedule> getByScheduleId(Long scheduleId) {
        QueryWrapper<StudentSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("schedule_id", scheduleId);
        return studentScheduleMapper.selectList(queryWrapper);
    }

    @Override
    public void endNotMiss(Long scheduleId) {
        UpdateWrapper<StudentSchedule> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("schedule_id", scheduleId);
        updateWrapper.isNull("miss_reason");
        updateWrapper.set("schedule_state", ScheduleState.END.code());
        studentScheduleMapper.update(null, updateWrapper);
    }

    @Override
    public void endIsMiss(Long scheduleId) {
        UpdateWrapper<StudentSchedule> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("schedule_id", scheduleId);
        updateWrapper.isNotNull("miss_reason");
        updateWrapper.set("schedule_state", ScheduleState.MISS.code());
        studentScheduleMapper.update(null, updateWrapper);
    }

    @Override
    public Integer attendCount(long startTimestamp, long endTimestamp) {
        return studentScheduleMapper.attendCount(startTimestamp, endTimestamp);
    }

    @Override
    public Integer missCount(long startTimestamp, long endTimestamp) {
        return studentScheduleMapper.missCount(startTimestamp, endTimestamp);
    }

    @Override
    public BigDecimal callCourseCancelIncome(long startTimestamp, long endTimestamp) {
        return studentScheduleMapper.callCourseCancelIncome(startTimestamp, endTimestamp);
    }

    @Override
    public BigDecimal courseCancelIncome(long startTimestamp, long endTimestamp) {
        return studentScheduleMapper.courseCancelIncome(startTimestamp, endTimestamp);
    }

    @Override
    public void scheduleIng(Long scheduleId) {
        UpdateWrapper<StudentSchedule> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("schedule_id", scheduleId);
        updateWrapper.set("schedule_state", ScheduleState.ING.code());
        studentScheduleMapper.update(null, updateWrapper);
    }

    @Override
    public void incomeNotMiss(Long scheduleId, BigDecimal income) {
        UpdateWrapper<StudentSchedule> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("schedule_id", scheduleId);
        updateWrapper.isNotNull("miss_reason");
        updateWrapper.set("income", income);
        studentScheduleMapper.update(null, updateWrapper);
    }

    @Override
    public void income(Long scheduleId, BigDecimal income) {
        UpdateWrapper<StudentSchedule> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("schedule_id", scheduleId);
        updateWrapper.set("income", income);
        studentScheduleMapper.update(null, updateWrapper);
    }

    @Override
    public Integer studentCourseCancelPeriod(long startTimestamp, long endTimestamp) {
        return studentScheduleMapper.studentCourseCancelPeriod(startTimestamp, endTimestamp);
    }

//    @Override
//    public List<StudentSchedule> getByTimestampByGroupTeacherId(long startTimestamp, long endTimestamp) {
//        return studentScheduleMapper.getByTimestampByGroupTeacherId(startTimestamp, endTimestamp);
//    }

//    @Override
//    public List<FinancialAnalysisTeacherVO> getByTimestampByTeacherIdByGroupGradeId(long teacherId, long startTimestamp, long endTimestamp) {
//        return studentScheduleMapper.getByTimestampByTeacherIdByGroupGradeId(teacherId, startTimestamp, endTimestamp);
//    }

    @Override
    public Integer getStudentCountByMonth(long startTimestamp, long endTimestamp) {
        return studentScheduleMapper.getStudentCountByMonth(startTimestamp, endTimestamp);
    }

    @Override
    public Integer getMonthStudentCountByGradeId(long gradeId, long startTimestamp, long endTimestamp) {
        return studentScheduleMapper.getMonthStudentCountByGradeId(gradeId, startTimestamp, endTimestamp);
    }

    @Override
    public FinancialAnalysisTeacherBO getAllCountAndIncomeByGradeId(long gradeId, long startTimestamp, long endTimestamp) {
        return studentScheduleMapper.getAllCountAndIncomeByGradeId(gradeId, startTimestamp, endTimestamp);
    }

    @Override
    public FinancialAnalysisHeaderBO getAllCountAndIncome(long startTimestamp, long endTimestamp) {
        return studentScheduleMapper.getAllCountAndIncome(startTimestamp, endTimestamp);
    }

    @Override
    public void updateByScheduleId(StudentSchedule studentSchedule) {
        UpdateWrapper<StudentSchedule> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("schedule_id", studentSchedule.getScheduleId());
        studentScheduleMapper.update(studentSchedule, updateWrapper);
    }

    @Override
    public Integer studentInScheduleByScheduleIds(Long studentId, List<Long> scheduleIds) {
        return studentScheduleMapper.studentInScheduleByScheduleIds(studentId, scheduleIds);
    }

    @Override
    public List<StudentScheduleVO> getStudentSchedules(Long studentId, Long gradeId) {
        return studentScheduleMapper.getStudentSchedules(studentId, gradeId);
    }

}




