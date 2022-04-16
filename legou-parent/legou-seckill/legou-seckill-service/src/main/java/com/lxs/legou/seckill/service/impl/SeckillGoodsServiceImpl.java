package com.lxs.legou.seckill.service.impl;

import com.lxs.legou.common.utils.SystemConstants;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.seckill.po.SeckillGoods;
import com.lxs.legou.seckill.service.ISeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeckillGoodsServiceImpl extends CrudServiceImpl<SeckillGoods> implements ISeckillGoodsService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *
     * @param key 秒杀时区
     * @return
     */
    @Override
    public List<SeckillGoods> list(String key) {
        return redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + key).values();
    }

    @Override
    public SeckillGoods one(String time, Long id) {
        return (SeckillGoods) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).get(id);
    }
}
