package com.mail.common.vo.request;


import lombok.Data;

@Data
public class PurchaseItemDoneReqVO {

    private Long itemId;

    private Integer status;

    private String reason;
}
