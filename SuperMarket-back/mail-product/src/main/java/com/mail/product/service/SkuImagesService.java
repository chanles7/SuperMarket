package com.mail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.product.entity.SkuImagesEntity;
import com.mail.common.util.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * sku图片
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuImagesEntity> getImagesBySkuId(Long skuId);
}

