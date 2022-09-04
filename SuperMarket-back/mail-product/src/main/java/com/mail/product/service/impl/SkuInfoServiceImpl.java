package com.mail.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;

import com.mail.product.dao.SkuInfoDao;
import com.mail.product.entity.SkuInfoEntity;
import com.mail.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank((String) params.get("categoryId")), "catalog_id", params.get("categoryId"));
        wrapper.eq(StringUtils.isNotBlank((String) params.get("brandId")), "brand_id", params.get("brandId"));
        wrapper.ge(StringUtils.isNotBlank((String) params.get("min")), "price", params.get("min"));
        wrapper.le(StringUtils.isNotBlank((String) params.get("max")) && new BigDecimal((String) params.get("max")).compareTo(new BigDecimal("0")) == 1
                , "price", params.get("max"));
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(wrap -> wrap.eq("sku_id", key).or().like("sku_name", key));
        }
        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

}