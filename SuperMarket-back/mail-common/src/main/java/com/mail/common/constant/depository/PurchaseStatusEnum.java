package com.mail.common.constant.depository;

import lombok.Getter;


@Getter
public enum PurchaseStatusEnum {

    CREATED("新建",0),
    ASSIGNED("已分配",1),
    RECEIVED("已领取",2),
    FINISHED("已完成",3),
    ERROR("有异常",4);

    private final String type;
    private final Integer value;

    PurchaseStatusEnum(String type, Integer value) {
        this.type = type;
        this.value = value;
    }
}
