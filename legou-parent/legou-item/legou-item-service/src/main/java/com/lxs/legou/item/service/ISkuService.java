package com.lxs.legou.item.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.item.po.Sku;

public interface ISkuService extends ICrudService<Sku> {

    /**
     * 减库存
     * @param num
     * @param skuId
     */
    public void decrCount(Integer num, Long skuId);

}
