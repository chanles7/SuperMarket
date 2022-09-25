package com.mail.common.to;

import com.mail.common.vo.CartItemVO;
import lombok.Data;

import java.util.List;

/**
 * description 功能描述
 *
 * @author ch
 * create 2022/9/25 6:35
 */
@Data
public class OrderLockTO {

    private String orderNo;

    private List<CartItemVO> items;
}
