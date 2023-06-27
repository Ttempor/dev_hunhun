package com.hun.api.sys.controller.base;


import com.hun.bean.entity.base.ChargeMethod;
import com.hun.common.response.BaseResponse;
import com.hun.service.service.base.ChargeMethodService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课程
 * @author 性能小钢炮
 */
@RestController
@RequestMapping("/api/chargeMethod")
@CrossOrigin
public class ChargeMethodController {
    @Resource
    private ChargeMethodService chargeMethodService;

    /**
     * 获得所有收费方式
     */
    @GetMapping()
    @PreAuthorize("@pms.hasPermission('admin')")
    public BaseResponse<List<ChargeMethod>> getCoursePage() {
        return BaseResponse.ok(chargeMethodService.getAllChargeMethod());
    }

}
