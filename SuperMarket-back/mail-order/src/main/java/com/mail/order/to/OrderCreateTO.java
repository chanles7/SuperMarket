package com.mail.order.to;

import com.mail.order.entity.OrderEntity;
import com.mail.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * description 功能描述
 *
 * @author ch
 * create 2022/9/25 2:45
 */
@Data
public class OrderCreateTO {

    private OrderEntity order;

    private List<OrderItemEntity> orderItems;

    private BigDecimal transportFee;

    private BigDecimal payablePrice;

    private BigDecimal price;

}
