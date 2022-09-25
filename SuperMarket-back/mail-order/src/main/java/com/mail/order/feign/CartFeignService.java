package com.mail.order.feign;

import com.mail.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * description 功能描述
 *
 * @author ch
 * create 2022/9/25 4:13
 */
@FeignClient("mail-cart")
public interface CartFeignService {

    @GetMapping("cart/confirmOrder")
    R getCurrentCart();
}
