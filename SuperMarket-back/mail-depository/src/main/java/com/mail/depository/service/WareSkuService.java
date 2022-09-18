package com.mail.depository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.common.util.PageUtils;
import com.mail.depository.entity.WareSkuEntity;
import com.mail.depository.vo.request.DepositorySkuReqVO;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:41:54
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPageByCondition(Map<String, Object> params);

    void addStock(DepositorySkuReqVO depositorySkuReqVO);

    Map<Long, Boolean> getHasStock(List<Long> ids);
}

