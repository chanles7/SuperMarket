package com.mail.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;
import com.mail.product.dao.SkuInfoDao;
import com.mail.product.entity.*;
import com.mail.product.service.*;
import com.mail.product.vo.response.AttrGroupRespVO;
import com.mail.product.vo.response.AttrRespVO;
import com.mail.product.vo.response.SkuDetailInfoRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;


@Slf4j
@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private AttrGroupService attrGroupService;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private ThreadPoolExecutor executor;


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


    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        return this.query().eq("spu_id", spuId).list();
    }


    @Override
    public SkuDetailInfoRespVO getDetailInfoBySkuId(Long skuId) {
        SkuDetailInfoRespVO skuDetailInfoRespVO = new SkuDetailInfoRespVO();

        //1、sku基本信息
        CompletableFuture<SkuInfoEntity> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfoEntity skuInfoEntity = this.getById(skuId);
            skuDetailInfoRespVO.setSkuInfo(skuInfoEntity);
            return skuInfoEntity;
        }, executor);


        //2、sku图片信息
        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
            List<SkuImagesEntity> imagesEntityList = skuImagesService.getImagesBySkuId(skuId);
            skuDetailInfoRespVO.setImagesList(imagesEntityList);
        }, executor);


        //3、sku的销售属性组合
        CompletableFuture<Void> saleAttrFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            List<SkuSaleAttrValueEntity> saleAttrValueList = skuSaleAttrValueService.listBySkuId(skuId);
            Map<String, Boolean> map = new HashMap<>();
            saleAttrValueList.forEach(item -> map.put(item.getAttrValue(), true));
            List<AttrRespVO> attrRespVOList = skuSaleAttrValueService.getSkuSaleAttrValueBySpuId(res.getSpuId());
            Set<Long> attrIds = attrRespVOList.stream()
                    .map(AttrRespVO::getAttrId)
                    .collect(Collectors.toSet());
            List<SkuDetailInfoRespVO.AttrVO> saleAttrVoList = attrIds.stream()
                    .map(attrId -> {
                        SkuDetailInfoRespVO.AttrVO attrVO = new SkuDetailInfoRespVO.AttrVO();
                        attrVO.setAttrId(attrId);
                        return attrVO;
                    })
                    .collect(Collectors.toList());
            attrRespVOList.forEach(attrRespVO -> {
                saleAttrVoList.stream()
                        .filter(saleAttrVO -> attrRespVO.getAttrId().equals(saleAttrVO.getAttrId()))
                        .forEach(saleAttrVO -> {
                            if (saleAttrVO.getAttrName() == null) {
                                saleAttrVO.setAttrName(attrRespVO.getAttrName());
                            }
                            SkuDetailInfoRespVO.AttrValueVO attrValueVO = new SkuDetailInfoRespVO.AttrValueVO();
                            //此处根据sql，valueSelect字段只有一个值
                            attrValueVO.setAttrValue(attrRespVO.getValueSelect());
                            if (map.get(attrRespVO.getValueSelect()) != null) {
                                attrValueVO.setIsChecked(true);
                            }
                            List<Long> skuIdList = Arrays.stream(attrRespVO.getSkuIds().split(","))
                                    .map(Long::parseLong)
                                    .collect(Collectors.toList());
                            attrValueVO.setSkuIds(skuIdList);
                            if (saleAttrVO.getAttrValueList() == null) {
                                List<SkuDetailInfoRespVO.AttrValueVO> attrValueVOList = new ArrayList<>();
                                saleAttrVO.setAttrValueList(attrValueVOList);
                            }
                            saleAttrVO.getAttrValueList().add(attrValueVO);
                        });
            });
            skuDetailInfoRespVO.setSaleAttrList(saleAttrVoList);
        }, executor);


        CompletableFuture<Void> descFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            //4、spu的介绍
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(res.getSpuId());
            skuDetailInfoRespVO.setSpuInfoDesc(spuInfoDescEntity);
        }, executor);


        CompletableFuture<Void> attrGroupFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            //5、spu的规格参数信息
            List<AttrGroupRespVO> attrGroupRespVOList = attrGroupService.getAttrTreeByCategoryId(res.getCatalogId());
            List<SkuDetailInfoRespVO.AttrGroupVO> attrGroupVOList = attrGroupRespVOList.stream()
                    .map(attrGroupRespVO -> {
                        SkuDetailInfoRespVO.AttrGroupVO attrGroupVO = new SkuDetailInfoRespVO.AttrGroupVO();
                        attrGroupVO.setAttrGroupId(attrGroupRespVO.getAttrGroupId());
                        attrGroupVO.setAttrGroupName(attrGroupRespVO.getAttrGroupName());
                        List<SkuDetailInfoRespVO.AttrVO> attrs = attrGroupRespVO.getAttrs().stream()
                                .map(attrEntity -> {
                                    SkuDetailInfoRespVO.AttrVO attrVo = BeanUtil.copyProperties(attrEntity, SkuDetailInfoRespVO.AttrVO.class);
                                    List<SkuDetailInfoRespVO.AttrValueVO> attrValueList = Arrays.stream(attrEntity.getValueSelect().split(";"))
                                            .map(item -> {
                                                SkuDetailInfoRespVO.AttrValueVO attrValueVo = new SkuDetailInfoRespVO.AttrValueVO();
                                                attrValueVo.setAttrValue(item);
                                                return attrValueVo;
                                            })
                                            .collect(Collectors.toList());
                                    attrVo.setAttrValueList(attrValueList);
                                    return attrVo;
                                })
                                .collect(Collectors.toList());
                        attrGroupVO.setAttrs(attrs);
                        return attrGroupVO;
                    })
                    .collect(Collectors.toList());
            skuDetailInfoRespVO.setAttrGroupList(attrGroupVOList);
        }, executor);


        CompletableFuture<Void> categoryFuture = skuInfoFuture.thenAcceptAsync((res) -> {
            //6、分类信息
            Long[] currentCategoryPath = categoryService.getCurrentCategoryPath(res.getCatalogId());
            SkuDetailInfoRespVO.CategoryVo categoryVo = new SkuDetailInfoRespVO.CategoryVo();
            List<String> list = new ArrayList<>();
            for (Long categoryId : currentCategoryPath) {
                CategoryEntity category = categoryService.getById(categoryId);
                list.add(category.getName());
            }
            categoryVo.setCategory1Name(list.get(0));
            categoryVo.setCategory2Name(list.get(1));
            categoryVo.setCategory3Name(list.get(2));
            skuDetailInfoRespVO.setCategoryView(categoryVo);
        }, executor);


        try {
            CompletableFuture.allOf(imageFuture, saleAttrFuture, descFuture, attrGroupFuture, categoryFuture).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return skuDetailInfoRespVO;
    }
}