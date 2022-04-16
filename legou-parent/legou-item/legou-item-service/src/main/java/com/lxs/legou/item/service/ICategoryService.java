package com.lxs.legou.item.service;

import com.lxs.legou.core.service.ICrudService;
import com.lxs.legou.item.po.Category;

import java.util.List;

/**
 * @Des 新职课商城项目
 * @Author 雪松
 * @Date 2020/11/1 14:43
 */
public interface ICategoryService extends ICrudService<Category> {

    public List<String> selectNamesByIds(List<Long> ids);

}
