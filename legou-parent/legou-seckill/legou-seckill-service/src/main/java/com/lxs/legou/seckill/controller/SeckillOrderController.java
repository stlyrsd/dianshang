package com.lxs.legou.seckill.controller;

import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.seckill.po.SeckillOrder;
import com.lxs.legou.seckill.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckill-order")
@CrossOrigin
public class SeckillOrderController extends BaseController<ISeckillOrderService, SeckillOrder> {

    @Autowired
    private ISeckillOrderService orderService;

    @RequestMapping("/add")
    public ResponseEntity add(String username, String time, Long id) {

        try {
            boolean b = orderService.add(id, time, username);
            if (b) {
                return ResponseEntity.ok("抢单成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity("抢单失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
