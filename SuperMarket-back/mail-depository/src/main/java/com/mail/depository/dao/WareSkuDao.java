package com.mail.depository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mail.depository.entity.WareSkuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:41:54
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {


    List<Map<String, Object>> getStockBySkuId(@Param("ids") List<Long> ids);


    /**
     * 查询出可以出库的仓库id集合
     *
     * @param skuId 商品id
     * @return
     */
    List<Long> hasStockWareList(Long skuId);


    /**
     * 锁库存
     *
     * @param skuId  商品id
     * @param count  购买数量
     * @param wareId 仓库id
     * @return
     */
    Long lockSkuStock(@Param("skuId") Long skuId, @Param("num") Integer count, @Param("wareId") Long wareId);
}
