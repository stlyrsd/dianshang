package com.lxs.legou.item.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.item.po.Sku;
import com.lxs.legou.item.po.Spu;
import com.lxs.legou.item.service.ISkuService;
import com.lxs.legou.item.service.ISpuDetailService;
import com.lxs.legou.item.service.ISpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Des 新职课商城项目
 * @Author 雪松
 * @Date 2020/11/3 14:29
 */
@Service
public class SpuServiceImpl extends CrudServiceImpl<Spu> implements ISpuService {

    @Autowired
    private ISpuDetailService spuDetailService;

    @Autowired
    private ISkuService skuService;

    @Override
    @Transactional
    public void saveSpu(Spu spu) {
        //保存spu -> spu持久化产生主键属性
        this.saveOrUpdate(spu);

        //保存spuDetail
        if (null == spu.getSpuDetail().getId()) { //添加
            spu.getSpuDetail().setId(spu.getId());
            spuDetailService.save(spu.getSpuDetail());
        } else {
            spuDetailService.updateById(spu.getSpuDetail()); //修改
        }

        //保存skus
        //删除当前spu的所有的sku
        skuService.remove(Wrappers.<Sku>query().eq("spu_id_", spu.getId()));
        for (Sku sku : spu.getSkus()) {
            sku.setSpuId(spu.getId());
            skuService.save(sku);
        }

    }

}
