package com.hun.api.sys.controller.expand;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.dto.expand.recordspend.QueryRecordSpendDto;
import com.hun.bean.entity.expand.RecordSpend;
import com.hun.common.response.BaseResponse;
import com.hun.service.service.expand.RecordSpendService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 员工
 */
@RestController
@RequestMapping("/api/recordSpend")
@CrossOrigin
public class RecordSpendController {
    @Resource
    private RecordSpendService recordSpendService;


    /**
     * 条件分页
     */
    @GetMapping
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<Page<RecordSpend>> getRecordSpendPage(@Validated QueryRecordSpendDto queryRecordSpendPageDto) {
        return BaseResponse.ok(recordSpendService.getRecordSpendPage(queryRecordSpendPageDto));
    }

}
