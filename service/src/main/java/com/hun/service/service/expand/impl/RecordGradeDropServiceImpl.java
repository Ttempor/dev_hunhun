package com.hun.service.service.expand.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.dto.expand.recordgradedrop.QueryDropGradePageDto;
import com.hun.bean.entity.expand.RecordGradeDrop;
import com.hun.bean.vo.RecordGradeDropVO;
import com.hun.service.mapper.expand.RecordGradeDropMapper;
import com.hun.service.service.expand.RecordGradeDropService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【record_grade_drop】的数据库操作Service实现
* @createDate 2023-04-05 15:51:29
*/
@Service
public class RecordGradeDropServiceImpl implements RecordGradeDropService {

    @Resource
    private RecordGradeDropMapper recordGradeDropMapper;
    @Value("${default-page-size}")
    private long pageSize;

    /**
     * 退课记录的条件分页
     */
    @Override
    public Page<RecordGradeDropVO> getDropPage(QueryDropGradePageDto queryDropGradePageDto) {
        QueryWrapper<RecordGradeDrop> queryWrapper = new QueryWrapper<>();
        //是否要附上员工姓名条件
        if (queryDropGradePageDto.getStudentName() != null) {
            queryWrapper.like("student_name", queryDropGradePageDto.getStudentName().trim());
        }
        //是否要附上员工姓名条件
        if (queryDropGradePageDto.getEmployeeName() != null) {
            queryWrapper.like("employee_name", queryDropGradePageDto.getEmployeeName().trim());
        }
        //是否要附上班级名条件
        if (queryDropGradePageDto.getGradeName() != null) {
            queryWrapper.like("grade_name", queryDropGradePageDto.getGradeName().trim());
        }
        Page<RecordGradeDropVO> page = Page.of(queryDropGradePageDto.getP(), pageSize);
        return recordGradeDropMapper.selectByPage(page, queryWrapper);
    }

    /**
     * 保存退课记录
     */
    @Override
    public void save(RecordGradeDrop recordGradeDrop) {
        recordGradeDropMapper.insert(recordGradeDrop);
    }

    @Override
    public Integer getCountByGradeIdAndMonth(long gradeId, long startTimestamp, long endTimestamp) {
        return recordGradeDropMapper.getCountByGradeIdAndMonth(gradeId, startTimestamp, endTimestamp);
    }

    @Override
    public List<RecordGradeDrop> getByMonthAndGroupByGradeId(long startTimestamp, long endTimestamp) {
        return recordGradeDropMapper.getByMonthAndGroupByGradeId(startTimestamp, endTimestamp);
    }
}




