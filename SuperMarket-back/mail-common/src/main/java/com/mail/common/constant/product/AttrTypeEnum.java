package com.mail.common.constant.product;

import lombok.Getter;

@Getter
public enum AttrTypeEnum {


    ATTR_TYPE_BASE("base",1),
    ATTR_TYPE_SALE("sale",0);

    private final String type;
    private final Integer value;

    AttrTypeEnum(String type, Integer value) {
        this.type = type;
        this.value = value;
    }
}