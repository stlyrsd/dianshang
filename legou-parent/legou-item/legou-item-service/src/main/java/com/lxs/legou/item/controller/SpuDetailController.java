package com.lxs.legou.item.controller;

import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.item.po.SpuDetail;
import com.lxs.legou.item.service.ISpuDetailService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Des 新职课商城项目
 * @Author 雪松
 * @Date 2020/11/3 14:51
 */
@RestController
@RequestMapping("/spu-detail")
public class SpuDetailController extends BaseController<ISpuDetailService, SpuDetail> {

}
