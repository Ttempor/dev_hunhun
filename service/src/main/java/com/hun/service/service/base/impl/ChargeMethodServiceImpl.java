package com.hun.service.service.base.impl;


import com.hun.bean.entity.base.ChargeMethod;
import com.hun.service.mapper.base.ChargeMethodMapper;
import com.hun.service.service.base.ChargeMethodService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 性能小钢炮
* @description 针对表【charge_method_dev】的数据库操作Service实现
* @createDate 2023-03-25 00:35:28
*/
@Service
public class ChargeMethodServiceImpl implements ChargeMethodService {
    @Resource
    private ChargeMethodMapper chargeMethodMapper;


    /**
     * 通过id查询一个收费方式
     * @param chargeMethodId e
     * @return e
     */
    @Override
    public ChargeMethod getOneChargeMethodById(Long chargeMethodId) {
        return chargeMethodMapper.selectById(chargeMethodId);
    }

    /**
     * 获得所有收费方式
     * @return e
     */
    @Override
    public List<ChargeMethod> getAllChargeMethod() {
        return chargeMethodMapper.selectList(null);
    }
}




