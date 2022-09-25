package com.mail.order.feign;

import com.mail.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * description 功能描述
 *
 * @author ch
 * create 2022/9/24 16:12
 */
@FeignClient("mail-user")
public interface UserFeignService {


    /**
     * 通过用户id获取地址信息
     * @param userId 用户id
     * @return
     */
    @GetMapping("user/memberreceiveaddress/list/user")
    R getAddressListByUserId(@RequestParam String userId);


    /**
     * 通过用户地址id计算运费并返回地址信息
     * @param addressId 地址id
     * @return
     */
    @GetMapping("user/memberreceiveaddress/transport/fee/{id}")
    R getTransportFeeByAddressId(@PathVariable("id") String addressId);
}
