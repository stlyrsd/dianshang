package com.lxs.legou.order.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.order.po.Order;

public interface IOrderService extends ICrudService<Order> {

    /**
     * 添加订单
     * @param order
     */
    public void add(Order order);

    /**
     * 修改订单完成支付状态
     * @param outTradeNo
     * @param tradeNo
     */
    public void updatePayStatus(String outTradeNo, String tradeNo);

}
