package com.mail.product.vo.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberPriceReqVO {

    private Long id;

    private String name;

    private BigDecimal price;
}