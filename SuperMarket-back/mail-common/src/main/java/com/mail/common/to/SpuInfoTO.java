package com.mail.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * description 功能描述
 *
 * @author ch
 * create 2022/9/25 5:01
 */
@Data
public class SpuInfoTO {

    /**
     * 商品id
     */
    private Long id;
    /**
     * 商品名称
     */
    private String spuName;
    /**
     * 商品描述
     */
    private String spuDescription;
    /**
     * 所属分类id
     */
    private Long catalogId;

    private String catelogName;
    /**
     * 品牌id
     */
    private Long brandId;

    private String brandName;
    /**
     *
     */
    private BigDecimal weight;
}
