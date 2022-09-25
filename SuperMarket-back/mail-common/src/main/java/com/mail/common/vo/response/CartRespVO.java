package com.mail.common.vo.response;

import com.mail.common.vo.CartItemVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ch
 */
@Data
public class CartRespVO {

    List<CartItemVO> items;

    /**
     * 商品数量
     */
    private Integer countNum;

    /**
     * 商品类型数量
     */
    private Integer countType;

    /**
     * 总价
     */
    private BigDecimal totalAmount;

    /**
     * 满减
     */
    private BigDecimal reduce;

    /**
     * 最终价
     */
    private BigDecimal resAmount;

}
