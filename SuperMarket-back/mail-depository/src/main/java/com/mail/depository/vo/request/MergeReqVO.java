package com.mail.depository.vo.request;


import lombok.Data;

import java.util.List;

@Data
public class MergeReqVO {

    private Long purchaseId;

    private List<Long> items;
}
