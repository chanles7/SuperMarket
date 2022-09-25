package com.mail.common.vo.request;

import lombok.Data;

@Data
public class DepositorySkuReqVO {
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * 仓库id
     */
    private Long wareId;
    /**
     * 库存数
     */
    private Integer stock;
}
