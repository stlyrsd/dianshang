package com.lxs.legou.item.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import com.lxs.legou.item.dao.SpecGroupDao;
import com.lxs.legou.item.dao.SpecParamDao;
import com.lxs.legou.item.po.SpecGroup;
import com.lxs.legou.item.po.SpecParam;
import com.lxs.legou.item.service.ISpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecGroupServiceImpl extends CrudServiceImpl<SpecGroup> implements ISpecGroupService {

    @Autowired
    private SpecParamDao specParamDao;

    @Override
    public List<SpecGroup> list(SpecGroup entity) {
        return ((SpecGroupDao) getBaseMapper()).selectList(entity);
    }

    @Override
    public void saveGroup(Long cid, List<SpecGroup> groups) {
        //根据cid删除所有的规格参数分组和规格参数项
        getBaseMapper().delete(Wrappers.<SpecGroup>query().eq("cid_", cid));
        specParamDao.delete(Wrappers.<SpecParam>query().eq("cid_", cid));

        //添加规格参数返祖和规格项
        for (SpecGroup group : groups) {
            getBaseMapper().insert(group);
            for (SpecParam param : group.getParams()) {
                param.setGroupId(group.getId());
                specParamDao.insert(param);
            }
        }

    }
}
