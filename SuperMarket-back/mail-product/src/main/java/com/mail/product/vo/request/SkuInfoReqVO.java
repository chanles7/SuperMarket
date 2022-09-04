package com.mail.product.vo.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class SkuInfoReqVO {

    private List<AttrReqVO> attr;

    private String skuName;

    private BigDecimal price;

    private String skuTitle;

    private String skuSubtitle;

    private List<ImageReqVO> images;

    private List<String> descar;

    private Integer fullCount;

    private BigDecimal discount;

    private Integer countStatus;

    private BigDecimal fullPrice;

    private BigDecimal reducePrice;

    private Integer priceStatus;

    private List<MemberPriceReqVO> memberPrice;


    @Override
    public String toString() {
        return "SkuInfoReqVO{" +
                "attr=" + attr +
                ", " + '\n' + "skuName='" + skuName + '\'' +
                ", price=" + price +
                ", skuTitle='" + skuTitle + '\'' +
                ", skuSubtitle='" + skuSubtitle + '\'' +
                ", " + '\n' + "images=" + images +
                ", descar=" + descar +
                ", fullCount=" + fullCount +
                ", discount=" + discount +
                ", countStatus=" + countStatus +
                ", fullPrice=" + fullPrice +
                ", reducePrice=" + reducePrice +
                ", priceStatus=" + priceStatus +
                ", " + '\n' + "memberPriceReqVO=" + memberPrice +
                '}';
    }
}