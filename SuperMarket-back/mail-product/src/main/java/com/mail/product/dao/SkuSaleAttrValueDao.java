package com.mail.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mail.product.entity.SkuSaleAttrValueEntity;
import com.mail.product.vo.response.AttrRespVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<AttrRespVO> getSkuSaleAttrValueBySpuId(Long spuId);
}
