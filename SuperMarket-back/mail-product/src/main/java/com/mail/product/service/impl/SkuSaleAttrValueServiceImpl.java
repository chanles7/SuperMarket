package com.mail.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;
import com.mail.product.dao.SkuSaleAttrValueDao;
import com.mail.product.entity.SkuSaleAttrValueEntity;
import com.mail.product.service.SkuSaleAttrValueService;
import com.mail.product.vo.response.AttrRespVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Resource
    private SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public List<SkuSaleAttrValueEntity> listBySkuId(Long skuId) {
        return this.query().eq("sku_id", skuId).list();
    }


    @Override
    public List<AttrRespVO> getSkuSaleAttrValueBySpuId(Long spuId) {
        return skuSaleAttrValueDao.getSkuSaleAttrValueBySpuId(spuId);
    }


    @Override
    public List<SkuSaleAttrValueEntity> getSkuSaleAttrValueBySkuId(Long skuId) {
        return this.query().eq("sku_id", skuId).list();
    }
}