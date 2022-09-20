package com.mail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.product.entity.SpuImagesEntity;
import com.mail.common.util.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveImages(Long id, List<String> images);
}

