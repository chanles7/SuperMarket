package com.mail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.common.utils.PageUtils;
import com.mail.product.entity.AttrGroupEntity;

import java.util.Map;

/**
 * 属性分组
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params, Long categoryId);
}

