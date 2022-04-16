package com.lxs.legou.security.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lxs.legou.core.po.BaseEntity;
import lombok.Data;

/**
 * @Des 新职课商城项目
 * @Author 雪松
 * @Date 2020/12/3 11:43
 */
@Data
@TableName("address_")
public class Address extends BaseEntity {

    @TableField("username_")
    private String username;//用户名

    @TableField("province_")
    private String provinceid;//省

    @TableField("city_")
    private String cityid;//市

    @TableField("area_")
    private String areaid;//县/区

    @TableField("phone_")
    private String phone;//电话

    @TableField("address_")
    private String address;//详细地址

    @TableField("contact_")
    private String contact;//联系人

    @TableField("is_default_")
    private String isDefault;//是否是默认 1默认 0否

    @TableField("alias_")
    private String alias;//别名

}