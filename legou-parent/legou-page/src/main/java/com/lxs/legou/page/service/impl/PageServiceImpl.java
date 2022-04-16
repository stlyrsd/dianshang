package com.lxs.legou.page.service.impl;

import com.lxs.legou.common.utils.JsonUtils;
import com.lxs.legou.item.po.Category;
import com.lxs.legou.item.po.Sku;
import com.lxs.legou.item.po.Spu;
import com.lxs.legou.item.po.SpuDetail;
import com.lxs.legou.page.client.CategoryClient;
import com.lxs.legou.page.client.SkuClient;
import com.lxs.legou.page.client.SpuClient;
import com.lxs.legou.page.client.SpuDetailClient;
import com.lxs.legou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Des 新职课商城项目
 * @Author 雪松
 * @Date 2020/11/13 10:52
 */
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private SpuClient spuClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpuDetailClient spuDetailClient;

    @Autowired
    private SkuClient skuClient;

    @Value("${pagepath}")
    private String pagepath;

    @Autowired
    private TemplateEngine templateEngine;


    /**
     * 构建数据模型
     * @param spuId
     * @return
     */
    private Map<String,Object> buildDataModel(Long spuId){
        //构建数据模型
        Map<String,Object> dataMap = new HashMap<>();
        //获取spu 和SKU列表
        Spu spu = spuClient.edit(spuId);
        Category c1 = categoryClient.edit(spu.getCid1());
        //获取分类信息
        dataMap.put("category1", categoryClient.edit(spu.getCid1()));
        dataMap.put("category2", categoryClient.edit(spu.getCid2()));
        dataMap.put("category3", categoryClient.edit(spu.getCid3()));

        List<Sku> skus = skuClient.selectSkusBySpuId(spu.getId());
        List<String> images = new ArrayList<>();
        for (Sku sku : skus) {
            images.add(sku.getImages());
        }
        dataMap.put("imageList", images);

        SpuDetail spuDetail = spuDetailClient.edit(spu.getId());
        Map<String, Object> genericMap = JsonUtils.parseMap(spuDetail.getSpecialSpec(), String.class, Object.class);

        dataMap.put("specificationList", genericMap);

        dataMap.put("spu",spu);
        dataMap.put("spuDetail",spuDetail);

        //根据spuId查询Sku集合
        dataMap.put("skuList", skus);
        return dataMap;
    }


    @Override
    public void createPageHtml(Long id) {
        //模板 + contextMap = html
        Context context = new Context();
        Map<String, Object> dataModel = buildDataModel(id);
        context.setVariables(dataModel); //model.addAttribute("key", "value") ${key}

        File dir = new File(pagepath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dest = new File(dir, id + ".html");

        try {
            PrintWriter writer = new PrintWriter(dest, "UTF-8");
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
