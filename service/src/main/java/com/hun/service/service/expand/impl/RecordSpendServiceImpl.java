package com.hun.service.service.expand.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.bo.AttendStudentBO;
import com.hun.bean.dto.expand.recordspend.QueryRecordSpendDto;
import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.expand.RecordSpend;
import com.hun.service.service.expand.RecordSpendService;
import com.hun.service.mapper.expand.RecordSpendMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【record_spend】的数据库操作Service实现
* @createDate 2023-04-04 15:04:35
*/
@Service
public class RecordSpendServiceImpl implements RecordSpendService {
    @Resource
    private RecordSpendMapper recordSpendMapper;
    @Value("${default-page-size}")
    private long pageSize;

    /**
     * 条件查询
     */
    @Override
    public Page<RecordSpend> getRecordSpendPage(QueryRecordSpendDto queryRecordSpendPageDto) {
        QueryWrapper<RecordSpend> queryWrapper = new QueryWrapper<>();
        //是否要附上员工姓名条件
        if (queryRecordSpendPageDto.getStudentName() != null) {
            queryWrapper.like("student_name", queryRecordSpendPageDto.getStudentName().trim());
        }
        //是否要附上班级名条件
        if (queryRecordSpendPageDto.getGradeName() != null) {
            queryWrapper.like("grade_name", queryRecordSpendPageDto.getGradeName().trim());
        }
        Page<RecordSpend> page = Page.of(queryRecordSpendPageDto.getP(), pageSize);
        return recordSpendMapper.selectPage(page, queryWrapper);
    }


    /**
     * 保存消费记录
     */
    @Override
    public void save(RecordSpend recordSpend) {
        recordSpendMapper.insert(recordSpend);
    }


}




