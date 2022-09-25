package com.mail.common.vo.request;


import lombok.Data;

import java.util.List;

@Data
public class PurchaseDoneReqVO {


    private Long id;


    private List<PurchaseItemDoneReqVO> items;

}
