package com.hun.service.service.expand;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.bo.AttendStudentBO;
import com.hun.bean.dto.expand.recordspend.QueryRecordSpendDto;
import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.expand.RecordSpend;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【record_spend】的数据库操作Service
* @createDate 2023-04-04 15:04:35
*/
public interface RecordSpendService {

    /**
     * 条件查询
     */
    Page<RecordSpend> getRecordSpendPage(QueryRecordSpendDto queryRecordSpendPageDto);

    /**
     * 保存消费记录
     */
    void save(RecordSpend recordSpend);

}
