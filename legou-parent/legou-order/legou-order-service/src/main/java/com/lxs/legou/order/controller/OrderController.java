package com.lxs.legou.order.controller;

import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.order.config.TokenDecode;
import com.lxs.legou.order.po.Order;
import com.lxs.legou.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/order")
public class OrderController extends BaseController<IOrderService, Order> {

    @Autowired
    private TokenDecode tokenDecode;

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody Order order) throws IOException {
        order.setUsername(tokenDecode.getUserInfo().get("user_name"));
        service.add(order);
        return ResponseEntity.ok("下单成功");
    }


}
