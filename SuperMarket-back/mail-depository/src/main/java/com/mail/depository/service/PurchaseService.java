package com.mail.depository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.common.util.PageUtils;
import com.mail.depository.entity.PurchaseEntity;

import java.util.Map;

/**
 * 采购信息
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:41:54
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

