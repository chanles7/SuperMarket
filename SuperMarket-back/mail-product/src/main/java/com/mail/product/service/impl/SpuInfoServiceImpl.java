package com.mail.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mail.common.to.SkuReductionTO;
import com.mail.common.to.SpuBoundTO;
import com.mail.common.util.R;
import com.mail.product.entity.*;
import com.mail.product.feign.CouponFeignService;
import com.mail.product.service.*;
import com.mail.product.vo.request.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;

import com.mail.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.swing.*;

@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private SpuImagesService spuImagesService;
    @Resource
    private AttrService attrService;
    @Resource
    private ProductAttrValueService productAttrValueService;
    @Resource
    private SkuInfoService skuInfoService;
    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    private CouponFeignService couponFeignService;


    @Override
    public void saveSpuInfo(SpuInfoEntity spuInfo) {
        this.save(spuInfo);
    }


    @Transactional
    @Override
    public void saveProduct(SpuInfoReqVO spuInfoReqVO) {
        log.info("spuInfoReqVO:{}", spuInfoReqVO);

        //1、保存spu基本信息
        SpuInfoEntity spuInfo = BeanUtil.copyProperties(spuInfoReqVO, SpuInfoEntity.class);
        spuInfo.setCreateTime(new Date());
        spuInfo.setUpdateTime(new Date());
        this.saveSpuInfo(spuInfo);

        Long spuId = spuInfo.getId();


        //2、保存spu的描述图片
        List<String> decript = spuInfoReqVO.getDecript();
        if (CollectionUtil.isNotEmpty(decript)) {
            SpuInfoDescEntity spuInfoDesc = new SpuInfoDescEntity();
            spuInfoDesc.setSpuId(spuId);
            spuInfoDesc.setDecript(String.join(",", decript));
            spuInfoDescService.saveSpuInfoDesc(spuInfoDesc);
        }


        //3、保存spu的图片集
        List<String> spuImages = spuInfoReqVO.getImages();
        if (CollectionUtil.isNotEmpty(spuImages)) {
            spuImagesService.saveImages(spuId, spuImages);
        }


        //4、保存spu的规格参数
        List<BaseAttrsReqVO> baseAttrs = spuInfoReqVO.getBaseAttrs();
        if (CollectionUtil.isNotEmpty(baseAttrs)) {
            List<ProductAttrValueEntity> productAttrValueList = baseAttrs.stream()
                    .map(baseAttr -> {
                        ProductAttrValueEntity productAttrValue = new ProductAttrValueEntity();
                        productAttrValue.setSpuId(spuId);
                        productAttrValue.setAttrId(baseAttr.getAttrId());
                        AttrEntity attr = attrService.getById(productAttrValue);
                        productAttrValue.setAttrName(attr.getAttrName());
                        productAttrValue.setAttrValue(baseAttr.getAttrValues());
                        productAttrValue.setQuickShow(baseAttr.getShowDesc());
                        return productAttrValue;
                    })
                    .collect(Collectors.toList());
            productAttrValueService.saveBatch(productAttrValueList);
        }

        //5、保存spu积分信息
        BoundReqVO bounds = spuInfoReqVO.getBounds();
        if (BeanUtil.isNotEmpty(bounds)) {
            SpuBoundTO spuBoundTO = BeanUtil.copyProperties(bounds, SpuBoundTO.class);
            spuBoundTO.setSpuId(spuId);
            R r = couponFeignService.saveSpuBounds(spuBoundTO);
            if (r.getCode() != 200) {
                log.error("调用服务失败");
            }
        }


        //6、保存当前spu对应的sku消息
        List<SkuInfoReqVO> skus = spuInfoReqVO.getSkus();
        if (CollectionUtil.isEmpty(skus)) return;


        skus.forEach(sku -> {
            //6.1、保存sku基本信息
            String skuDefaultImg = "";
            List<ImageReqVO> skuImages = sku.getImages();
            for (ImageReqVO skuImage : skuImages) {
                if (skuImage.getDefaultImg() == 1)
                    skuDefaultImg = skuImage.getImgUrl();
            }

            SkuInfoEntity skuInfo = BeanUtil.copyProperties(sku, SkuInfoEntity.class);
            skuInfo.setBrandId(spuInfo.getBrandId());
            skuInfo.setCatalogId(spuInfo.getCatalogId());
            skuInfo.setSaleCount(0L);
            skuInfo.setSpuId(spuInfo.getId());
            skuInfo.setSkuDefaultImg(skuDefaultImg);
            skuInfoService.save(skuInfo);

            Long skuId = skuInfo.getSkuId();


            //6.2、保存sku图片信息
            if (CollectionUtil.isNotEmpty(skuImages)) {
                List<SkuImagesEntity> skuImagesEntityList = skuImages.stream()
                        .map(skuImage -> {
                            SkuImagesEntity skuImagesEntity = BeanUtil.copyProperties(skuImage, SkuImagesEntity.class);
                            skuImagesEntity.setSkuId(skuId);
                            return skuImagesEntity;
                        })
                        .filter(entity -> StringUtils.isNotBlank(entity.getImgUrl()))
                        .collect(Collectors.toList());
                skuImagesService.saveBatch(skuImagesEntityList);
            }


            //6.3、保存sku销售属性信息
            List<AttrReqVO> attrs = sku.getAttr();
            if (CollectionUtil.isNotEmpty(attrs)) {
                List<SkuSaleAttrValueEntity> skuSaleAttrValueList = attrs.stream()
                        .map(attr -> {
                            SkuSaleAttrValueEntity skuSaleAttrValue = BeanUtil.copyProperties(attr, SkuSaleAttrValueEntity.class);
                            skuSaleAttrValue.setSkuId(skuId);
                            return skuSaleAttrValue;
                        })
                        .collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);
            }

            //6.4、保存sku优惠、满减信息
            SkuReductionTO skuReductionTO = BeanUtil.copyProperties(sku, SkuReductionTO.class);
            if (BeanUtil.isNotEmpty(skuReductionTO)) {
                if (skuReductionTO.getFullCount() > 0 || skuReductionTO.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
                    skuReductionTO.setSkuId(skuId);
                    R r = couponFeignService.saveSkuReduction(skuReductionTO);
                    if (r.getCode() != 200) {
                        log.error("调用服务失败");
                    }
                }
            }
        });
    }


    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank((String) params.get("categoryId")), "catalog_id", params.get("categoryId"));
        wrapper.eq(StringUtils.isNotBlank((String) params.get("brandId")), "brand_id", params.get("brandId"));
        wrapper.eq(StringUtils.isNotBlank((String) params.get("status")), "publish_status", params.get("status"));
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(wrap -> wrap.eq("id", key).or().like("spu_name", key));
        }
        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }
}