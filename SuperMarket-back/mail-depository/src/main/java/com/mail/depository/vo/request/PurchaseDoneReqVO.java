package com.mail.depository.vo.request;


import lombok.Data;

import java.util.List;

@Data
public class PurchaseDoneReqVO {


    private Long id;


    private List<PurchaseItemDoneReqVO> items;

}
