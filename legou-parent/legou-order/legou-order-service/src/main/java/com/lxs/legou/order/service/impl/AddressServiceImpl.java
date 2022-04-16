package com.lxs.legou.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.order.service.IAddressService;
import com.lxs.legou.security.po.Address;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl extends CrudServiceImpl<Address> implements IAddressService {

    @Override
    public List<Address> list(Address entity) {
        //根据用户名查询收货地址
        QueryWrapper<Address> queryWrapper = Wrappers.<Address>query();
        if (StringUtils.isNotEmpty(entity.getUsername())) {
            queryWrapper.eq("username_", entity.getUsername());
        }
        return getBaseMapper().selectList(queryWrapper);
    }
}
