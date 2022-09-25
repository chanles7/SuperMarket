package com.mail.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.common.util.PageUtils;
import com.mail.common.util.R;
import com.mail.common.vo.request.OrderSubmitReqVO;
import com.mail.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:43:26
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);


    /**
     * 订单提交
     * @param orderSubmitReqVO 订单相关信息
     * @return
     */
    R submit(OrderSubmitReqVO orderSubmitReqVO);

}

