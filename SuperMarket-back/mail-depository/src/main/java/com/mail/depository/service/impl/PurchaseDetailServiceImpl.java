package com.mail.depository.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;

import com.mail.depository.dao.PurchaseDetailDao;
import com.mail.depository.entity.PurchaseDetailEntity;
import com.mail.depository.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank((String) params.get("wareId")), "ware_id", params.get("wareId"));
        wrapper.eq(StringUtils.isNotBlank((String) params.get("status")), "status", params.get("status"));
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(item -> item.eq("purchase_id", key).or().eq("ware_id", key));
        }
        IPage<PurchaseDetailEntity> page = this.page(new Query<PurchaseDetailEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }


    @Override
    public List<PurchaseDetailEntity> listDetailByPurchaseId(Long purchaseId) {
        return this.query().eq("purchase_id", purchaseId).list();
    }
}