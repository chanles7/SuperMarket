package com.mail.common.vo.request;


import lombok.Data;

import java.util.List;

@Data
public class MergeReqVO {

    private Long purchaseId;

    private List<Long> items;
}
