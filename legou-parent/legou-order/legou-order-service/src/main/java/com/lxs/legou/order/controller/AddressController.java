package com.lxs.legou.order.controller;

import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.order.service.IAddressService;
import com.lxs.legou.order.config.TokenDecode;
import com.lxs.legou.security.po.Address;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @Des 新职课商城项目
 * @Author 雪松
 * @Date 2020/12/4 16:34
 */
@RestController
@RequestMapping("/address")
public class AddressController extends BaseController<IAddressService, Address> {

    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 根据实体条件查询
     * @return
     */
    @ApiOperation(value="查询", notes="根据实体条件查询")
    @RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
    @Override
    public List<Address> list(Address entity) {
        String username = null;
        try {
            username = tokenDecode.getUserInfo().get("user_name");
        } catch (IOException e) {
            e.printStackTrace();
        }
        entity.setUsername(username);

        //根据当前用户查询收件人地址
        return service.list(entity);
    }
}
