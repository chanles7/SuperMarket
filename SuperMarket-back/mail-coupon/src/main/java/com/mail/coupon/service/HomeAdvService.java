package com.mail.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.common.util.PageUtils;
import com.mail.coupon.entity.HomeAdvEntity;

import java.util.Map;

/**
 * 首页轮播广告
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:39:50
 */
public interface HomeAdvService extends IService<HomeAdvEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

