package com.mail.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.mail.common.to.MemberPriceTO;
import com.mail.common.to.SkuReductionTO;
import com.mail.coupon.entity.MemberPriceEntity;
import com.mail.coupon.entity.SkuLadderEntity;
import com.mail.coupon.service.MemberPriceService;
import com.mail.coupon.service.SkuLadderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;

import com.mail.coupon.dao.SkuFullReductionDao;
import com.mail.coupon.entity.SkuFullReductionEntity;
import com.mail.coupon.service.SkuFullReductionService;

import javax.annotation.Resource;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Resource
    private SkuLadderService skuLadderService;
    @Resource
    private MemberPriceService memberPriceService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public void saveInfo(SkuReductionTO skuReductionTO) {
        //1、保存阶梯式价格信息
        SkuLadderEntity skuLadder = BeanUtil.copyProperties(skuReductionTO, SkuLadderEntity.class);
        skuLadder.setAddOther(skuReductionTO.getCountStatus());
        if (skuLadder.getFullCount() > 0) {
            skuLadderService.save(skuLadder);
        }


        //2、保存满减信息
        SkuFullReductionEntity skuFullReduction = BeanUtil.copyProperties(skuReductionTO, SkuFullReductionEntity.class);
        skuFullReduction.setAddOther(skuReductionTO.getPriceStatus());
        if (skuFullReduction.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
            this.save(skuFullReduction);
        }


        //3、保存会员价格信息
        List<MemberPriceTO> memberPriceTOList = skuReductionTO.getMemberPrice();
        if (CollectionUtil.isNotEmpty(memberPriceTOList)) {
            List<MemberPriceEntity> memberPriceList = memberPriceTOList.stream()
                    .map(memberPriceTO -> {
                        MemberPriceEntity memberPrice = new MemberPriceEntity();
                        memberPrice.setSkuId(skuReductionTO.getSkuId());
                        memberPrice.setMemberLevelId(memberPriceTO.getId());
                        memberPrice.setMemberLevelName(memberPriceTO.getName());
                        memberPrice.setMemberPrice(memberPriceTO.getPrice());
                        memberPrice.setAddOther(1);
                        return memberPrice;
                    })
                    .filter(memberPrice -> memberPrice.getMemberPrice().compareTo(new BigDecimal("0")) == 1)
                    .collect(Collectors.toList());
            memberPriceService.saveBatch(memberPriceList);
        }

    }
}