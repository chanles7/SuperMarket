package com.mail.cart.feign;

import com.mail.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * description 功能描述
 *
 * @author ch
 * create 2022/9/23 5:27
 */
@FeignClient("mail-depository")
public interface DepositoryFeignService {

    @GetMapping("depository/waresku/stock/num/{skuId}")
    R getStockNum(@PathVariable Long skuId);


    @GetMapping("depository/waresku/enough/stock/{skuId}/{needNum}")
    R getStockEnough(@PathVariable("skuId") Long skuId, @PathVariable("needNum") Integer needNum);
}
