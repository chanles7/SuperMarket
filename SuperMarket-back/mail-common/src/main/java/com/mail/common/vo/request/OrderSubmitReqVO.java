package com.mail.common.vo.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * description 功能描述
 *
 * @author ch
 * create 2022/9/25 1:39
 */
@Data
public class OrderSubmitReqVO {

    private Long addressId;

    /**
     * 支付类型
     */
    private Integer payType;

    private String token;

    /**
     * 应付价格
     */
    private BigDecimal payablePrice;

    private String remark;
}
