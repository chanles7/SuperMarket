package com.mail.product.vo.request;

import com.mail.product.vo.response.AttrRespVO;
import lombok.Data;

import java.util.List;

@Data
public class SkuInfoReqVO {

    private List<AttrRespVO> attr;

    private String skuName;

    private String price;

    private String skuTitle;

    private String skuSubtitle;

    private List<String> images;

    private List<String> descar;

    private int fullCount;

    private int discount;

    private int countStatus;

    private int fullPrice;

    private int reducePrice;

    private int priceStatus;
    
    private List<MemberPriceReqVO> memberPrice;
}
