package com.lxs.legou.item.controller;

import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.core.po.ResponseBean;
import com.lxs.legou.item.po.SpecGroup;
import com.lxs.legou.item.service.ISpecGroupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Des 新职课商城项目
 * @Author 雪松
 * @Date 2020/11/2 15:19
 */
@RestController
@RequestMapping(value = "/group")
public class SpecGroupController extends BaseController<ISpecGroupService, SpecGroup> {

    @ApiOperation(value="保存规格参数", notes = "保存规格参数")
    @PostMapping("/save-group")
    public ResponseBean saveGroup(@RequestBody List<SpecGroup> specGroup) {
        ResponseBean rm = new ResponseBean();
        try {
            if (null != specGroup && specGroup.size() > 0) {
                service.saveGroup(specGroup.get(0).getCid(), specGroup);
            }
        } catch (Exception e) {
            e.printStackTrace();
            rm.setSuccess(false);
            rm.setMsg("保存失败");
        }
        return rm;
    }

}
