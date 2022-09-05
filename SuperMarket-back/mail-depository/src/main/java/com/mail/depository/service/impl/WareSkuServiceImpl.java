package com.mail.depository.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mail.common.util.R;
import com.mail.depository.feign.ProductFeignService;
import com.mail.depository.vo.request.DepositorySkuReqVO;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;

import com.mail.depository.dao.WareSkuDao;
import com.mail.depository.entity.WareSkuEntity;
import com.mail.depository.service.WareSkuService;

import javax.annotation.Resource;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {


    @Resource
    private ProductFeignService productFeignService;


    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank((String) params.get("wareId")), "ware_id", params.get("wareId"));
        wrapper.eq(StringUtils.isNotBlank((String) params.get("skuId")), "sku_id", params.get("skuId"));
        IPage<WareSkuEntity> page = this.page(new Query<WareSkuEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }


    @Override
    public void addStock(DepositorySkuReqVO depositorySkuReqVO) {
        WareSkuEntity wareSkuEntity = this.query()
                .eq("sku_id", depositorySkuReqVO.getSkuId())
                .eq("ware_id", depositorySkuReqVO.getWareId())
                .one();
        if (wareSkuEntity != null) {
            //存在则更新数量
            wareSkuEntity.setStock(wareSkuEntity.getStock() + depositorySkuReqVO.getStock());
            this.updateById(wareSkuEntity);
        } else {
            //不存在则新增
            wareSkuEntity = BeanUtil.copyProperties(depositorySkuReqVO, WareSkuEntity.class);
            wareSkuEntity.setStockLocked(0);
            try {
                R r = productFeignService.getSkuInfo(wareSkuEntity.getSkuId());
                if (r.getCode() == 200) {
                    Map<String, Object> skuInfo = (Map<String, Object>) r.get("skuInfo");
                    wareSkuEntity.setSkuName((String) skuInfo.get("skuName"));
                }
            } catch (Exception e) {

            }
            this.save(wareSkuEntity);
        }
    }
}