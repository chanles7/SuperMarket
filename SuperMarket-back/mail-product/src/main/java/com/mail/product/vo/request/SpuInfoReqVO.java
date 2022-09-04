package com.mail.product.vo.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class SpuInfoReqVO {

    private String spuName;

    private String spuDescription;

    private Long catalogId;

    private Long brandId;

    private BigDecimal weight;

    private Integer publishStatus;

    private List<String> decript;

    private List<String> images;

    private BoundReqVO bounds;

    private List<BaseAttrsReqVO> baseAttrs;

    private List<SkuInfoReqVO> skus;


    @Override
    public String toString() {
        return "SpuInfoReqVO{" +
                "spuName='" + spuName + '\'' +
                ", spuDescription='" + spuDescription + '\'' +
                ", catalogId=" + catalogId +
                ", brandId=" + brandId +
                ", weight=" + weight +
                ", publishStatus=" + publishStatus +
                ", " + '\n' + "decript=" + decript +
                ", " + '\n' + "images=" + images +
                ", " + '\n' + "bounds=" + bounds +
                ", " + '\n' + "baseAttrs=" + baseAttrs +
                ", " + '\n' + "skus=" + skus +
                '}';
    }
}
