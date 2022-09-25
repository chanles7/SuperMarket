package com.mail.cart.feign;

import com.mail.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * description 功能描述
 *
 * @author ch
 * create 2022/9/23 4:05
 */
@FeignClient("mail-product")
public interface ProductFeignService {

    @GetMapping("product/skuinfo/info/{skuId}")
    R getSkuInfo(@PathVariable("skuId") Long skuId);

}
