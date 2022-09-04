package com.mail.common.to;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberPriceTO {

    private Long id;

    private String name;

    private BigDecimal price;
}