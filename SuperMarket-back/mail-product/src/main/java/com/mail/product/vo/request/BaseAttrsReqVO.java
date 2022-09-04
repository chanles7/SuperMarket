package com.mail.product.vo.request;

import lombok.Data;

@Data
public class BaseAttrsReqVO {

    private Long attrId;

    private String attrValues;

    private Integer showDesc;
}