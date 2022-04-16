package com.lxs.legou.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.item.po.Category;
import com.lxs.legou.item.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Des 新职课商城项目
 * @Author 雪松
 * @Date 2020/11/1 14:44
 */
@Service
public class CategoryServiceImpl extends CrudServiceImpl<Category> implements ICategoryService {

    @Override
    public List<Category> list(Category entity) {
        QueryWrapper<Category>  queryWrapper = Wrappers.query();
        if (StringUtils.isNotEmpty(entity.getTitle())) {
            queryWrapper.like("title_", entity.getTitle());
        }
        if (null != entity.getParentId()) {
            queryWrapper.eq("parent_id_", entity.getParentId());
        }
        if (null != entity.getIsRoot() && entity.getIsRoot().equals(1)) {
            queryWrapper.isNull("parent_id_");
        }

        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    public List<String> selectNamesByIds(List<Long> ids) {
        QueryWrapper<Category> queryWrapper = Wrappers.<Category>query().in("id_", ids);
        return getBaseMapper().selectList(queryWrapper).stream().map(item -> item.getTitle()).collect(Collectors.toList());
    }


}
