package com.mail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.common.util.PageUtils;
import com.mail.product.entity.SkuSaleAttrValueEntity;
import com.mail.product.vo.response.AttrRespVO;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuSaleAttrValueEntity> listBySkuId(Long skuId);


    /**
     * 通过spuId查询其下的所有相关sku的销售属性信息
     *
     * @param spuId
     * @return
     */
    List<AttrRespVO> getSkuSaleAttrValueBySpuId(Long spuId);


    /**
     * 通过skuId查询销售属性信息
     * @param skuId
     * @return
     */
    List<SkuSaleAttrValueEntity> getSkuSaleAttrValueBySkuId(Long skuId);
}

