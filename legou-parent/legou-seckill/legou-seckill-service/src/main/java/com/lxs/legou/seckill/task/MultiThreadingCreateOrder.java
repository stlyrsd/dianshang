package com.lxs.legou.seckill.task;

import com.lxs.legou.common.utils.IdWorker;
import com.lxs.legou.common.utils.SystemConstants;
import com.lxs.legou.seckill.dao.SeckillGoodsDao;
import com.lxs.legou.seckill.po.SeckillGoods;
import com.lxs.legou.seckill.po.SeckillOrder;
import com.lxs.legou.seckill.pojo.SeckillStatus;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class MultiThreadingCreateOrder {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 多线程下单操作
     */
    @Async
    public void createOrder() {
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps(SystemConstants.SEC_KILL_USER_QUEUE_KEY).rightPop();

        if (seckillStatus != null) {
            String time = seckillStatus.getTime(); //秒杀时区
            Long id = seckillStatus.getGoodsId(); //秒杀商品id
            String username = seckillStatus.getUsername(); //秒杀用户

            //分布式锁
            RLock lock = redissonClient.getLock("seckillstock:" + id);

            try {
                //获得锁
                //waitTime：等待锁的时间
                //leaseTime：所得持有时间
                lock.tryLock(20,20, TimeUnit.SECONDS);

                //获得秒杀商品信息
                SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).get(id);

                //如果没有库存，直接抛出异常
                if (goods == null || goods.getStockCount() <=0) {
                    throw new RuntimeException("已售罄");
                }
                //将秒杀订单存入redis
                SeckillOrder seckillOrder = new SeckillOrder();
                seckillOrder.setId(idWorker.nextId());
                seckillOrder.setSeckillId(id);
                seckillOrder.setMoney(goods.getCostPrice());
                seckillOrder.setUserId(username);
                seckillOrder.setCreateTime(new Date());
                seckillOrder.setStatus("0");

                //模拟下单耗时操作
                try {
                    System.out.println("*********************开始模拟下单操作***************************");
                    Thread.sleep(10000);
                    System.out.println("*********************结束模拟下单操作***************************");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                /**
                 * hash -> namespace = SeckillOrder
                 *      - key       value
                 *       username   seckillOrder
                 */
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY).put(username, seckillOrder);

                //库存递减
                goods.setStockCount(goods.getStockCount() - 1);

                if (goods.getStockCount() <= 0) { //库存为0，同步到mysql
                    seckillGoodsDao.updateById(goods);
                    //清楚redis中的秒杀商品
                    redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).delete(id);
                } else {
                    //同步库存到redis
                    redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).put(id, goods);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }

}
