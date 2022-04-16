package com.lxs.legou.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxs.legou.admin.po.Dict;
import com.lxs.legou.admin.service.IDictService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DictServiceImplTest {

    @Autowired
    IDictService dictService;

    @Test
    public void test1() {
//        IPage<Dict> page = new Page<>(1, 10);
//        IPage<Dict> p = dictService.listPage(page, new Dict());
//        System.out.println("current= " + page.getCurrent());
//        System.out.println("total= " + page.getTotal());
//        System.out.println("getPages= " + page.getPages());
//        System.out.println("getSize= " + page.getSize());
//        System.out.println("getRecords=" + page.getRecords());
    }

}