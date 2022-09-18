package com.mail.common.to.es;


import lombok.Data;


import java.math.BigDecimal;
import java.util.List;


@Data
public class SkuInfoEsTO {

    private Long skuId;

    private Long spuId;

    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;

    private Boolean hasStock;

    private Long hotScore;

    private Long saleCount;

    private Long brandId;

    private String brandName;

    private String brandImg;

    private Long categoryId;

    private String categoryName;

    private List<Attr> attrs;


    @Data
    public static class Attr {

        private Long attrId;

        private String attrName;

        private String attrValue;

    }

}
