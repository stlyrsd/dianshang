package com.lxs.legou.order.service.impl;

import com.lxs.legou.common.utils.IdWorker;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.order.client.SkuClient;
import com.lxs.legou.order.client.UserClient;
import com.lxs.legou.order.dao.IOrderItemDao;
import com.lxs.legou.order.po.Order;
import com.lxs.legou.order.po.OrderItem;
import com.lxs.legou.order.service.IOrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl extends CrudServiceImpl<Order> implements IOrderService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IOrderItemDao orderItemDao;

    @Autowired
    private SkuClient skuClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    @Override
    public void add(Order order) {
        //1 添加订单主表(Order)数据
        order.setId(idWorker.nextId());

        //2 循环购物车数据，添加订单明细(OrderItem)数据
        List<OrderItem> cartList = redisTemplate.boundHashOps("Cart_" + order.getUsername()).values();

        Long totalNum =0l; //订单总数量
        long totalMoney = 0l; //订单总金额
        for (OrderItem orderItem : cartList) {
            totalNum += orderItem.getNum();
            totalMoney += orderItem.getPayMoney();

            orderItem.setId(idWorker.nextId());//订单选项的iD
            orderItem.setOrderId(order.getId());//订单的iD
            orderItem.setIsReturn("0");//未退货
            orderItemDao.insert(orderItem);

            //3 调用商品微服务，减库存
            skuClient.decrCount(orderItem.getNum(), orderItem.getSkuId());
        }

        order.setTotalNum(totalNum);//设置总数量
        order.setTotalMoney(totalMoney);//设置总金额
        order.setPayMoney(totalMoney);//设置实付金额
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());
        order.setOrderStatus("0");//0:未完成
        order.setPayStatus("0");//未支付
        order.setConsignStatus("0");//未发货
        order.setIsDelete("0");//未删除

        getBaseMapper().insert(order);

        //4 增加用户积分
        userClient.addPoint(10l, order.getUsername());

        //5 删除redis中的购物车数据
        redisTemplate.delete("Cart_" + order.getUsername());

        //6 发送延迟30分钟消息，未支付，取消订单，回滚库存
        rabbitTemplate.convertAndSend(env.getProperty("mq.order.exchange.ttl"), env.getProperty("mq.order.routing.ttl"), order.getId().toString());
    }

    @Override
    public void updatePayStatus(String outTradeNo, String tradeNo) {
        Order order = getBaseMapper().selectById(Long.parseLong(outTradeNo));

        order.setUpdateTime(new Date());
        order.setPayTime(new Date());
        order.setOrderStatus("1");
        order.setPayStatus("1");
        order.setTransactionId(tradeNo);
        getBaseMapper().updateById(order);
    }
}
