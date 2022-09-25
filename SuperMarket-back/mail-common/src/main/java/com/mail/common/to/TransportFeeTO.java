package com.mail.common.to;

import com.mail.common.vo.response.MemberReceiveAddressRespVO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * description 功能描述
 *
 * @author ch
 * create 2022/9/25 3:22
 */
@Data
public class TransportFeeTO {

    private MemberReceiveAddressRespVO address;

    private BigDecimal fee;
}
