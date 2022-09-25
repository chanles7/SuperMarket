package com.mail.common.to;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * sku信息
 * 
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
@Data
public class SkuInfoTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * skuId
	 */
	private Long skuId;
	/**
	 * spuId
	 */
	private Long spuId;
	/**
	 * sku名称
	 */
	private String skuName;
	/**
	 * sku介绍描述
	 */
	private String skuDesc;
	/**
	 * 所属分类id
	 */
	private Long catalogId;
	/**
	 * 品牌id
	 */
	private Long brandId;
	/**
	 * 默认图片
	 */
	private String skuDefaultImg;
	/**
	 * 标题
	 */
	private String skuTitle;
	/**
	 * 副标题
	 */
	private String skuSubtitle;
	/**
	 * 价格
	 */
	private BigDecimal price;
	/**
	 * 销量
	 */
	private Long saleCount;

}
