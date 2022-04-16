package com.lxs.legou.seckill.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxs.legou.common.utils.DateUtil;
import com.lxs.legou.common.utils.SystemConstants;
import com.lxs.legou.seckill.dao.SeckillGoodsDao;
import com.lxs.legou.seckill.po.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;

//@Component
public class SeckillGoodsPushTask {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    /**
     * 定时任务：每个30秒执行一次
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void loadGoodsPushRedis() {
        //1 获取当前时间后的秒杀时间间隔(5)
        List<Date> dateMenus = DateUtil.getDateMenus();
        for (Date dateMenu : dateMenus) {
            String extName = DateUtil.data2str(dateMenu, DateUtil.PATTERN_YYYYMMDDHH);//2020122110
            /**
             * select * from seckill_goods_ where
             stock_count_>0
             and `status_`='1'
             and date_menu_="当前秒杀时间段2020121716"
             and id_ not in (redis中已有id)
             */
            QueryWrapper<SeckillGoods> query = Wrappers.<SeckillGoods>query();
            query.gt("stock_count_", 0);
            query.eq("status_", 1);
            query.eq("date_menu_", extName);

            //现有的redis中的秒杀商品id
            Set keys = redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + extName).keys();
            if (keys != null && keys.size() >0) {
                query.notIn("id_", keys);
            }
            List<SeckillGoods> seckillGoods = seckillGoodsDao.selectList(query);
            for (SeckillGoods seckillGood : seckillGoods) {
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + extName).put(seckillGood.getId(), seckillGood);

                //设置有效期（2小时）
            }



        }
    }

}
