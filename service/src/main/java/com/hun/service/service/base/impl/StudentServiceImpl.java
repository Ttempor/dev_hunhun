package com.hun.service.service.base.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.bo.AttendStudentBO;
import com.hun.bean.dto.base.student.DepositDto;
import com.hun.bean.dto.base.student.QueryStudentPageDto;
import com.hun.bean.dto.base.student.SaveStudentDto;
import com.hun.bean.dto.base.student.UpdateStudentDto;
import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.base.Student;
import com.hun.bean.entity.expand.RecordSpend;
import com.hun.common.exception.BusinessException;
import com.hun.service.mapper.base.StudentMapper;
import com.hun.service.service.base.StudentService;
import com.hun.service.service.expand.RecordSpendService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【student】的数据库操作Service实现
* @createDate 2023-03-24 22:34:28
*/
@Setter
@Service
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private RecordSpendService recordSpendService;
    @Value("${default-page-size}")
    private long pageSize;


    /**
     * 通过页码获得学生
     * @param queryStudentPageDto e
     * @return e
     */
    @Override
    public Page<Student> getStudentsByPage(QueryStudentPageDto queryStudentPageDto) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        //是否要附上学生id条件
        if (queryStudentPageDto.getStudentId() != null) {
            queryWrapper.eq("student_id", queryStudentPageDto.getStudentId());
        }
        //是否要附上学生姓名条件
        if (queryStudentPageDto.getStudentName() != null) {
            queryWrapper.like("student_name", queryStudentPageDto.getStudentName().trim());
        }
        Page<Student> page = Page.of(queryStudentPageDto.getP(), pageSize);
        return studentMapper.selectPage(page, queryWrapper);
    }


    /**
     * 保存学生
     * @param saveStudentDto e
     */
    @Override
    public void saveStudent(SaveStudentDto saveStudentDto) {
        //电话号码已校验
        Student student = new Student();
        student.setStudentName(saveStudentDto.getStudentName().trim());
        student.setStudentSex(saveStudentDto.getStudentSex());
        student.setStudentPhone(saveStudentDto.getStudentPhone().trim());
        student.setGradeCount(0);
        BigDecimal bigDecimal = new BigDecimal("0.00");
        student.setBalance(bigDecimal);
        student.setLockBalance(bigDecimal);
        student.setSpend(bigDecimal);
        studentMapper.insert(student);
    }
    /**
     * 通过id获得一个学生
     */
    @Override
    public Student getOneStudentById(Long id) {
        return studentMapper.selectById(id);
    }


    /**
     * 修改一个学生
     */
    @Override
    public void updateOneStudent(UpdateStudentDto updateStudentDto) {
        Student student = new Student();
        student.setStudentName(updateStudentDto.getStudentName().trim());
        student.setStudentSex(updateStudentDto.getStudentSex());
        student.setStudentPhone(updateStudentDto.getStudentPhone().trim());

        UpdateWrapper<Student> wrapper = new UpdateWrapper<>();
        wrapper.eq("student_id", updateStudentDto.getStudentId());
        studentMapper.update(student, wrapper);
    }

    /**
     * 通过学生id使学生班级数增加
     */
    @Override
    public void incrGradeCountByStudentId(Long studentId, int step) {
        studentMapper.incrGradeCountByStudentId(studentId, step);
    }



    /**
     * 充值余额
     */
    @Override
    public void deposit(Long studentId, BigDecimal balance) {
        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("balance = balance+" + balance);
        updateWrapper.eq("student_id", studentId);
        studentMapper.update(null, updateWrapper);
    }


    /**
     * 锁定余额
     */

    @Override
    public void lockBalance(Long studentId, BigDecimal price) {
        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("lock_balance = lock_balance + " + price);
        updateWrapper.eq("student_id", studentId);
        studentMapper.update(null,updateWrapper);
    }

    /**
     * 批量查询学生id
     */
    @Override
    public List<Student> selectBatchIds(List<Long> studentIds) {
        return studentMapper.selectBatchIds(studentIds);
    }

    @Override
    public void unLockBalance(Long studentId, BigDecimal lockSpendBalance) {
        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("lock_balance = lock_balance - " + lockSpendBalance);
        updateWrapper.eq("student_id", studentId);
        studentMapper.update(null,updateWrapper);
    }

    @Override
    public void updateNameById(Long studentId, String name) {
        Student student = new Student();
        student.setStudentId(studentId);
        student.setStudentName(name);
        studentMapper.updateById(student);
    }

    @Override
    public void updateSexById(Long studentId, String sex) {
        Student student = new Student();
        student.setStudentId(studentId);
        student.setStudentSex(sex);
        studentMapper.updateById(student);
    }

    @Override
    public void validatedBindPhone(Long studentId) {
        Student student = studentMapper.selectById(studentId);
        if (student.getStudentPhone() != null) {
            throw new BusinessException("手机号码已经绑定");
        }
    }

    @Override
    public void updatePhoneById(Long studentId, String phoneNumber) {
        Student student = new Student();
        student.setStudentId(studentId);
        student.setStudentPhone(phoneNumber);
        studentMapper.updateById(student);
    }

    @Override
    public void validatedBindName(Long studentId) {
        Student student = studentMapper.selectById(studentId);
        if (student.getStudentName() != null) {
            throw new BusinessException("修改姓名请联系管理员");
        }
    }

    @Override
    public void validatedBindSex(Long studentId) {
        Student student = studentMapper.selectById(studentId);
        if (student.getStudentSex() != null) {
            throw new BusinessException("修改性别请联系管理员");
        }
    }

    /**
     * 批量消费减少余额并记录消费
     */
    @Override
    public void batchConsumeAndRecord(Grade grade, BigDecimal schedulePrice, List<Student> students, List<Long> attendStudentIds, String description) {
        //批量扣款，扣锁额和余额
        studentMapper.batchSubtractBalanceAndLockBalance(schedulePrice, attendStudentIds);

        //一般来说,学生不可能为size=0
        RecordSpend recordSpend = new RecordSpend();
        //填充课程名,收费方式,班级名
        warpRecordSpend(recordSpend, grade);
        //填充本次消费描述
        recordSpend.setDescription(description);
        //遍历学习,进行消费记录
        for (Student student : students) {
            //保存消费学生id
            recordSpend.setStudentId(student.getStudentId());
            //消费学生姓名
            recordSpend.setStudentName(student.getStudentName());
            //记录本次消费额度
            recordSpend.setConsume(schedulePrice);
            //记录本次消费后余额
            recordSpend.setBalance(student.getBalance().subtract(schedulePrice));
            //记录本次消费时间
            recordSpend.setSpendTime(LocalDateTime.now());
            //防止主键插入重复
            recordSpend.setRecordSpendId(null);
            //消费记录插入到数据库中
            recordSpendService.save(recordSpend);
        }
    }

    public void warpRecordSpend(RecordSpend recordSpend, Grade grade) {
        recordSpend.setGradeId(grade.getGradeId());
        recordSpend.setGradeName(grade.getGradeName());
        recordSpend.setCourseId(grade.getCourseId());
        recordSpend.setCourseName(grade.getCourseName());
        recordSpend.setChargeMethodId(grade.getChargeMethodId());
        recordSpend.setChargeMethodName(grade.getChargeMethodName());
        recordSpend.setChargeMethodExpression(grade.getChargeMethodExpression());
    }
}




