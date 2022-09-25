package com.mail.common.vo.response;

import com.mail.common.to.TransportFeeTO;
import com.mail.common.to.UserReceiveAddressTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * description 功能描述
 *
 * @author ch
 * create 2022/9/24 15:59
 */
@Data
public class OrderConfirmRespVO {

    private List<UserReceiveAddressTO> addressList;


    private CartRespVO cart;


    private TransportFeeTO transport;

    /**
     * 最终收款价
     */
    private BigDecimal price;

    /**
     * 订单防重令牌
     */
    private String orderToken;
}
