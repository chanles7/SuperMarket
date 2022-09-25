package com.mail.cart.service;

import com.mail.common.util.R;

public interface CartService {

    /**
     * 新增或修改购物车中商品数量
     *
     * @param skuId 商品id
     * @param num   数量
     * @return
     */
    R addOrUpdateCart(Long skuId, Integer num);


    R list();


    /**
     * 更新购物车商品勾选状态
     *
     * @param skuId 商品id
     * @param check 勾选状态
     * @return
     */
    R updateCheck(Long skuId, Boolean check);


    /**
     * 确认订单信息
     *
     * @return
     */
    R getCurrentCart();
}
