package com.mail.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;

import com.mail.product.dao.SpuImagesDao;
import com.mail.product.entity.SpuImagesEntity;
import com.mail.product.service.SpuImagesService;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public void saveImages(Long id, List<String> images) {
        if (CollectionUtil.isEmpty(images)) return;
        List<SpuImagesEntity> collect = images.stream().map(img -> {
            SpuImagesEntity spuImage = new SpuImagesEntity();
            spuImage.setSpuId(id);
            spuImage.setImgUrl(img);
            return spuImage;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }
}