package com.lxs.legou.seckill.service.impl;

import com.lxs.legou.common.utils.IdWorker;
import com.lxs.legou.common.utils.SystemConstants;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.seckill.dao.SeckillGoodsDao;
import com.lxs.legou.seckill.dao.SeckillOrderDao;
import com.lxs.legou.seckill.po.SeckillGoods;
import com.lxs.legou.seckill.po.SeckillOrder;
import com.lxs.legou.seckill.pojo.SeckillStatus;
import com.lxs.legou.seckill.service.ISeckillOrderService;
import com.lxs.legou.seckill.task.MultiThreadingCreateOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SeckillOrderServiceImpl extends CrudServiceImpl<SeckillOrder> implements ISeckillOrderService {

    @Autowired
    private MultiThreadingCreateOrder multiThreadingCreateOrder;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Boolean add(Long id, String time, String username) {
        /*
            用户秒杀排队次数 -> namespace = UserQueueCount
                                - username 次数
         */
        Long userQueueCount = redisTemplate.boundHashOps(SystemConstants.SEC_KILL_QUEUE_REPEAT_KEY).increment(username, 1);
        if (userQueueCount > 1) {
            throw new RuntimeException("秒杀重复排队");
        }

        SeckillStatus seckillStatus = new SeckillStatus(username, new Date(), 1, id, time);
        //将秒杀排队信息，leftpush存入redis的list队列中
        redisTemplate.boundListOps(SystemConstants.SEC_KILL_USER_QUEUE_KEY).leftPush(seckillStatus);

        //多线程抢单
        multiThreadingCreateOrder.createOrder();

        return true;
    }
}
