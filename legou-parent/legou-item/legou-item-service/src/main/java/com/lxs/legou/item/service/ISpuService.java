package com.lxs.legou.item.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.item.po.Spu;

public interface ISpuService extends ICrudService<Spu> {

    /**
     * 保存spu
     * @param spu
     *   - spu
     *   - spuDetail
     *   - skus
     */
    public void saveSpu(Spu spu);

}
