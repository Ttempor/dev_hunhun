package com.hun.service.service.base;


import com.hun.bean.entity.base.ChargeMethod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【charge_method_dev】的数据库操作Service
* @createDate 2023-03-25 00:35:28
*/
public interface ChargeMethodService {
    /**
     * 通过id查询一个收费方式
     * @param chargeMethodId e
     * @return e
     */
    ChargeMethod getOneChargeMethodById(Long chargeMethodId);


    /**
     * 获得所有收费方式
     * @return e
     */
    List<ChargeMethod> getAllChargeMethod();
}
