package com.hun.service.service.expand.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.bo.AttendStudentBO;
import com.hun.bean.bo.ScheduleExpandStudentListVo;
import com.hun.bean.dto.expand.gradeinfo.DeleteGradeInfoDto;
import com.hun.bean.dto.expand.gradeinfo.QueryGradeInfoPageDto;
import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.base.Student;
import com.hun.bean.entity.expand.GradeInfo;
import com.hun.bean.entity.expand.RecordGradeDrop;
import com.hun.bean.enums.GradeState;
import com.hun.common.exception.BusinessException;
import com.hun.service.service.base.GradeService;
import com.hun.service.service.expand.RecordGradeDropService;
import com.hun.service.mapper.expand.GradeInfoMapper;
import com.hun.service.service.base.StudentService;
import com.hun.service.service.expand.GradeInfoService;
import lombok.Setter;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

/**
* @author 性能小钢炮
* @description 针对表【gradeInfo】的数据库操作Service实现
* @createDate 2023-03-24 22:34:28
*/
@Setter
@Service
public class GradeInfoServiceImpl implements GradeInfoService {
    @Resource
    private GradeInfoMapper gradeInfoMapper;
    @Resource
    private GradeService gradeService;
    @Resource
    private StudentService studentService;
    @Resource
    private MapperFacade mapperFacade;
    @Resource
    private RecordGradeDropService recordGradeDropService;
    @Value("${default-page-size}")
    private long pageSize;
    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * 班级信息的条件分页
     */
    @Override
    public Page<GradeInfo> getGradeInfoPage(QueryGradeInfoPageDto queryGradeInfoPageDto) {
        QueryWrapper<GradeInfo> queryWrapper = new QueryWrapper<>();
        //是否要附上员工姓名条件
        if (queryGradeInfoPageDto.getEmployeeName() != null) {
            queryWrapper.like("employee_name", queryGradeInfoPageDto.getEmployeeName().trim());
        }
        //是否要附上班级名条件
        if (queryGradeInfoPageDto.getGradeName() != null) {
            queryWrapper.like("grade_name", queryGradeInfoPageDto.getGradeName().trim());
        }
        //是否要附上课程姓名条件
        if (queryGradeInfoPageDto.getCourseName() != null) {
            queryWrapper.like("course_name", queryGradeInfoPageDto.getCourseName().trim());
        }
        //是否要附上员工姓名条件
        if (queryGradeInfoPageDto.getStudentName() != null) {
            queryWrapper.like("student_name", queryGradeInfoPageDto.getStudentName().trim());
        }
        Page<GradeInfo> page = Page.of(queryGradeInfoPageDto.getP(), pageSize);
        return gradeInfoMapper.selectPage(page, queryWrapper);
    }

    /**
     *通过学生id和班级id查询一个班级人员信息,判断班级中是否存在该学生
     */
    @Override
    public GradeInfo getOneByGradeIdAndStudentId(Long gradeId, Long studentId) {
        return gradeInfoMapper.selectOneByGradeIdAndStudentId(gradeId, studentId);
    }

    /**
     * 保存一个班级信息
     * @param gradeInfo e
     */
    @Override
    public void saveGradeInfo(GradeInfo gradeInfo) {
        gradeInfoMapper.insert(gradeInfo);
    }


    /**
     * 退课
     */

    @Override
    public void tuiKe(DeleteGradeInfoDto deleteGradeInfoDto) {
        //学生在班级中
        //查班级
        GradeInfo gradeInfo = getOneByGradeIdAndStudentId(deleteGradeInfoDto.getGradeId(), deleteGradeInfoDto.getStudentId());
        if (gradeInfo == null) {
            throw new BusinessException("非法查询");
        }
        //上课中不可退课,已结课不可退课
        int gradeState = gradeInfo.getGradeState();
        if (GradeState.isEnd(gradeState) || GradeState.isIng(gradeState)) {
            throw new BusinessException("上课中或已结课状态，不可退课");
        }
        //按课时收费,套餐不可退课
        if (gradeInfo.getChargeMethodId() == 1) {
            RecordGradeDrop recordGradeDrop = new RecordGradeDrop();
            recordGradeDrop.setGradeInfoId(gradeInfo.getGradeInfoId());
            recordGradeDrop.setGradeId(gradeInfo.getGradeId());
            recordGradeDrop.setGradeName(gradeInfo.getGradeName());
            recordGradeDrop.setStudentId(gradeInfo.getStudentId());
            recordGradeDrop.setStudentName(gradeInfo.getStudentName());
            recordGradeDrop.setCourseId(gradeInfo.getCourseId());
            recordGradeDrop.setCourseName(gradeInfo.getCourseName());
            recordGradeDrop.setTeacherId(gradeInfo.getTeacherId());
            recordGradeDrop.setEmployeeName(gradeInfo.getEmployeeName());
            recordGradeDrop.setChargeMethodId(gradeInfo.getChargeMethodId());
            recordGradeDrop.setChargeMethodName(gradeInfo.getChargeMethodName());
            recordGradeDrop.setChargeMethodExpression(gradeInfo.getChargeMethodExpression());
            recordGradeDrop.setConsumePeriod(gradeInfo.getConsumePeriod());
            recordGradeDrop.setMissPeriod(gradeInfo.getMissPeriod());
            recordGradeDrop.setTotalPeriod(gradeInfo.getMissPeriod());
            recordGradeDrop.setSpendBalance(gradeInfo.getSpendBalance());
            recordGradeDrop.setLockBalance(gradeInfo.getLockBalance());
            recordGradeDrop.setLockSpendBalance(gradeInfo.getLockSpendBalance());
            recordGradeDrop.setGradeState(gradeInfo.getGradeState());
            recordGradeDrop.setDropDatetime(LocalDateTime.now());
            recordGradeDrop.setDropTimestamp(recordGradeDrop.getDropDatetime()
                    .toInstant(ZoneOffset.of("+8")).toEpochMilli());
            recordGradeDrop.setDropReason(deleteGradeInfoDto.getReason().trim());
            //退课
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    //保存退课原因和退课时间
                    recordGradeDropService.save(recordGradeDrop);
                    //将剩余锁定余额解锁
                    studentService.lockBalance(gradeInfo.getStudentId(), gradeInfo.getLockSpendBalance().negate());
                    //从班级中删除学生
                    gradeInfoMapper.deleteById(gradeInfo.getGradeInfoId());
                }
            });
            return;
        }
        throw new BusinessException("套餐不可退课");
    }




    /**
     * 批量增加学生缺勤课时
     */
    @Override
    public void batchPlusMissPeriod(Long gradeId, int missPeriod, List<Long> missStudentId) {
        gradeInfoMapper.batchPlusMissPeriod(gradeId, missPeriod, missStudentId);
    }


    /**
     * 批量增加学生出勤课时
     */

    @Override
    public void batchPlusConsumePeriod(Long gradeId, int consumePeriod, List<Long> consumeStudentIds) {
        gradeInfoMapper.batchPlusConsumePeriod(gradeId, consumePeriod, consumeStudentIds);
    }


    /**
     * 通过班级id获得班级的学生
     */

    @Override
    public List<AttendStudentBO> getGradeStudents(Long gradeId) {
        QueryWrapper<GradeInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("grade_id", gradeId);
        return gradeInfoMapper.getGradeStudents(gradeId);
    }


    @Override
    public List<GradeInfo> getGradeInfoByGradeId(Long gradeId) {
        QueryWrapper<GradeInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("grade_id", gradeId);
        return gradeInfoMapper.selectList(queryWrapper);
    }

    @Override
    public void gradeIng(Long gradeId) {
        UpdateWrapper<GradeInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("grade_state", GradeState.ing.code());
        updateWrapper.eq("grade_id", gradeId);
        gradeInfoMapper.update(null, updateWrapper);
    }

    @Override
    public void gradeWait(Long gradeId) {
        UpdateWrapper<GradeInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("grade_state", GradeState.wait.code());
        updateWrapper.eq("grade_id", gradeId);
        gradeInfoMapper.update(null, updateWrapper);
    }

    @Override
    public void gradeRun(Long gradeId) {
        UpdateWrapper<GradeInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("grade_state", GradeState.run.code());
        updateWrapper.eq("grade_id", gradeId);
        gradeInfoMapper.update(null, updateWrapper);
    }



    /**
     * 退课
     */
    @Override
    public void deleteGradeInfo(Long gradeId, Long studentId) {
        QueryWrapper<GradeInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("grade_id", gradeId);
        queryWrapper.eq("student_id", studentId);
        gradeInfoMapper.delete(queryWrapper);
    }

    @Override
    public GradeInfo studentInCourse(Long courseId, Long studentId) {
        QueryWrapper<GradeInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("student_id", studentId);
        return gradeInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public List<ScheduleExpandStudentListVo> getInfoByScheduleId(Long scheduleId) {
        return gradeInfoMapper.getInfoByScheduleId(scheduleId);
    }



    @Override
    public void buySchedulePreCheckAndHandle(Long gradeId, Long studentId, Student student, BigDecimal price, Integer totalPeriod, Grade grade) {
        //查出班级学生
        GradeInfo gradeInfo = getOneByGradeIdAndStudentId(gradeId, studentId);
        //学生是否在班级中
        if (gradeInfo == null) {
            //不在班级中
            //班级人数是否已满
            if (grade.getGradeNowStudentCount() >= grade.getGradeMaxStudentCount()) {
                throw new BusinessException("班级学生人数已满");
            }
            gradeInfo = mapperFacade.map(grade, GradeInfo.class);
            gradeInfo.setStudentId(student.getStudentId());
            gradeInfo.setStudentName(student.getStudentName());
            gradeInfo.setConsumePeriod(0);
            gradeInfo.setMissPeriod(0);
            gradeInfo.setTotalPeriod(totalPeriod);
            gradeInfo.setSpendBalance(new BigDecimal("0"));
            gradeInfo.setLockBalance(price);
            gradeInfo.setLockSpendBalance(price);
            gradeInfo.setGradeState(grade.getGradeState());
            //添加到gradeInfo中
            saveGradeInfo(gradeInfo);
            //班级人数+1
            gradeService.incrGradeNowStudentCountByGradeId(gradeId, 1);
            //学生班级数+1
            studentService.incrGradeCountByStudentId(studentId, 1);
        }

    }

}




