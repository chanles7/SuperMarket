package com.mail.cart.constant;

/**
 * description 功能描述
 *
 * @author ch
 * create 2022/9/23 2:21
 */
public interface CartConstant {

    /**
     * 购物车缓存
     */
    String CART_USER_CACHE_PREFIX = "mail:cart:user:";
    String CART_TMP_CACHE_PREFIX = "mail:cart:tmp:";


    /**
     * 订单防重令牌
     */
    String USER_ORDER_TOKEN_PREFIX = "mail:order:token:";


}
