package com.mail.depository.dao;

import com.mail.depository.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
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
}
