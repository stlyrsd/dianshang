package com.lxs.legou.item.dao;

import com.lxs.legou.core.dao.ICrudDao;
import com.lxs.legou.item.po.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Des 新职课商城项目
 * @Author 雪松
 * @Date 2020/11/3 11:33
 */
public interface SkuDao extends ICrudDao<Sku> {

    @Select("select * from sku_ where spu_id_ = #{skuId}")
    public List<Sku> findBySkuId(Integer spuId);

    @Update(value="update sku_ set stock_ = stock_ - #{num} where id_ =#{skuId} and stock_ >= #{num}")
    public int decrCount(@Param("num") Integer num, @Param("skuId") Long skuId);
    
}
