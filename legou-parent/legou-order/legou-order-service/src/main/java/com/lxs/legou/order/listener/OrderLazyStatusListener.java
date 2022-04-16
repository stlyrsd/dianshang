package com.lxs.legou.order.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxs.legou.order.po.Order;
import com.lxs.legou.order.service.IOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RabbitListener(queues = "${mq.order.queue.dlx}")
public class OrderLazyStatusListener {

    @Autowired
    private IOrderService orderService;

    @RabbitHandler
    public void handlerData(String msg) {
        if (StringUtils.isNotEmpty(msg)) {
            Long id = Long.parseLong(msg);
            Order order = orderService.getById(id);
            order.setOrderStatus("3");
            orderService.updateById(order);

            //回滚库存（作业）
        }
    }

}
