package com.mail.common.vo;


import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ch
 */
@Data
public class CartItemVO {

    private Long skuId;

    private Boolean check;

    private String title;

    private String image;

    private BigDecimal price;

    private Integer count;

    private BigDecimal totalPrice;

    private Boolean hasStock;

    public void setTotalPrice() {
        this.totalPrice = this.price.multiply(new BigDecimal(this.count));
    }
}
