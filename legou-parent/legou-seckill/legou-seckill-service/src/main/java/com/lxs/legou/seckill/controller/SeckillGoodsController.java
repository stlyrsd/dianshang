package com.lxs.legou.seckill.controller;

import com.lxs.legou.common.utils.DateUtil;
import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.seckill.po.SeckillGoods;
import com.lxs.legou.seckill.service.ISeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/seckill-goods")
@CrossOrigin
public class SeckillGoodsController extends BaseController<ISeckillGoodsService, SeckillGoods> {

    @Autowired
    private ISeckillGoodsService goodsService;

    /**
     * 返回5个时间间隔
     * @return
     */
    @GetMapping("/menus")
    public List<Date> datemenus() {
        List<Date> dates = DateUtil.getDateMenus();
        for (Date date : dates) {
            System.out.println(date);
        }
        return dates;
    }

    @RequestMapping("/list/{time}")
    public List<SeckillGoods> list(@PathVariable("time") String time) {
        return goodsService.list(time);
    }

    /**
     * 根据时区和id查询秒杀商品
     * @param time
     * @param id
     * @return
     */
    @RequestMapping("/one")
    public SeckillGoods one(String time, Long id) {
        return goodsService.one(time, id);
    }

}
