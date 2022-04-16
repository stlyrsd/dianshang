package com.lxs.legou.order.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 延迟队列配置，30分钟未支付订单，取消订单，回滚库存
 * @Des 新职课商城项目
 * @Author 雪松
 * @Date 2020/12/10 17:05
 */
@Configuration
public class MqConfig {

    @Autowired
    private Environment env;

    /**
     * 正常交换机
     * @return
     */
    @Bean
    public Exchange ttlExchange() {
        return ExchangeBuilder.directExchange(env.getProperty("mq.order.exchange.ttl")).durable(true).build();
    }

    /**
     * 死信交换机
     * @return
     */
    @Bean
    public Exchange dlxExchange() {
        return ExchangeBuilder.directExchange(env.getProperty("mq.order.exchange.dlx")).durable(true).build();
    }

    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue dlxQueue() {
        return QueueBuilder.durable(env.getProperty("mq.order.queue.dlx")).build();
    }

    /**
     * 正常队列
     * @return
     */
    @Bean
    public Queue ttlQueue() {
        return QueueBuilder
                .durable(env.getProperty("mq.order.queue.ttl"))
                //队列的过期时间，10秒过期
                .withArgument("x-message-ttl", 10000)
                //指定死信交换机
                .withArgument("x-dead-letter-exchange", env.getProperty("mq.order.exchange.dlx"))
                .withArgument("x-dead-letter-routing-key", env.getProperty("mq.order.routing.dlx"))
                .build();
    }


    /**
     * 正常交换机，队列绑定
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding ttlBinding(@Qualifier("ttlQueue") Queue queue, @Qualifier("ttlExchange")  Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(env.getProperty("mq.order.routing.ttl")).noargs();
    }

    /**
     * 死信交换机，队列绑定
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding dlxBinding(@Qualifier("dlxQueue") Queue queue, @Qualifier("dlxExchange")  Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(env.getProperty("mq.order.routing.dlx")).noargs();
    }


}
