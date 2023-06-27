package com.hun.service.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.bo.AttendStudentBO;
import com.hun.bean.dto.base.student.DepositDto;
import com.hun.bean.dto.base.student.QueryStudentPageDto;
import com.hun.bean.dto.base.student.SaveStudentDto;
import com.hun.bean.dto.base.student.UpdateStudentDto;
import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.base.Student;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【student】的数据库操作Service
* @createDate 2023-03-24 22:34:28
*/
public interface StudentService {

    /**
     * 分页获得学生
     * @param queryStudentPageDto e
     * @return e
     */
    Page<Student> getStudentsByPage(QueryStudentPageDto queryStudentPageDto);
    /**
     * 保存一个学生
     * @param saveStudentDto
     * @return
     */
    void saveStudent(SaveStudentDto saveStudentDto);

    /**
     * 通过id获得一个学生
     */
    Student getOneStudentById(Long id);

    /**
     * 修改一个学生
     * @param updateStudentDto e
     */
    void updateOneStudent(UpdateStudentDto updateStudentDto);

    /**
     * 通过学生id使学生班级数增加
     */
    void incrGradeCountByStudentId(Long studentId, int step);


    /**
     * 充值余额
     */
    void deposit(Long studentId, BigDecimal balance);

    /**
     * 锁定余额
     */
    void lockBalance(Long studentId, BigDecimal price);


    /**
     * 批量消费减少余额并记录消费
     */
    void batchConsumeAndRecord(Grade grade, BigDecimal schedulePrice, List<Student> students, List<Long> attendStudentIds, String description);

    /**
     * 批量查询学生id
     */
    List<Student> selectBatchIds(List<Long> studentIds);

    /**
     * 解锁学生余额
     */
    void unLockBalance(Long studentId, BigDecimal lockSpendBalance);

    /**
     * 通过id修改姓名
     * @param studentId id
     * @param name 姓名
     */
    void updateNameById(Long studentId, String name);

    /**
     * 通过id修改姓名
     * @param studentId id
     * @param sex 性别
     */
    void updateSexById(Long studentId, String sex);

    /**
     * 校验学生是否已经绑定手机号, 已经绑定则抛出异常
     */
    void validatedBindPhone(Long studentId);
    /**
     * 通过id修改手机号码
     * @param studentId id
     * @param phoneNumber 手机号码
     */
    void updatePhoneById(Long studentId, String phoneNumber);

    /**
     *校验是否已经填写姓名, 禁止重复填写, 否则抛出异常
     */
    void validatedBindName(Long studentId);

    /**
     *校验是否已经选择性别, 禁止重复填写, 否则抛出异常
     */
    void validatedBindSex(Long studentId);
}
