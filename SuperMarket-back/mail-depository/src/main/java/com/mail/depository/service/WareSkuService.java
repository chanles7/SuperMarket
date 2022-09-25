package com.mail.depository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.common.to.OrderLockTO;
import com.mail.common.util.PageUtils;
import com.mail.common.util.R;
import com.mail.depository.entity.WareSkuEntity;
import com.mail.common.vo.request.DepositorySkuReqVO;

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


    /**
     * 获取库存数量
     *
     * @param skuId sku
     * @return
     */
    Integer getStockNum(Long skuId);


    /**
     * 查看某个商品有无库存
     *
     * @param skuId  商品id
     * @param needNum 购买数量
     * @return
     */
    Boolean getStockEnough(Long skuId, Integer needNum);


    /**
     * 锁库存
     * @param orderLockTO 订单信息
     * @return
     */
    R lockStock(OrderLockTO orderLockTO);
}

