package com.mail.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mail.common.to.SpuBoundTO;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;

import com.mail.coupon.dao.SpuBoundsDao;
import com.mail.coupon.entity.SpuBoundsEntity;
import com.mail.coupon.service.SpuBoundsService;


@Service("spuBoundsService")
public class SpuBoundsServiceImpl extends ServiceImpl<SpuBoundsDao, SpuBoundsEntity> implements SpuBoundsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuBoundsEntity> page = this.page(
                new Query<SpuBoundsEntity>().getPage(params),
                new QueryWrapper<SpuBoundsEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public void saveSpuBound(SpuBoundTO spuBoundTO) {
        SpuBoundsEntity spuBoundsEntity = BeanUtil.copyProperties(spuBoundTO, SpuBoundsEntity.class);
        this.save(spuBoundsEntity);
    }
}