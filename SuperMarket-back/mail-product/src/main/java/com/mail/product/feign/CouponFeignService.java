package com.mail.product.feign;

import com.mail.common.to.SkuReductionTO;
import com.mail.common.to.SpuBoundTO;
import com.mail.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("mail-coupon")
public interface CouponFeignService {


    @PostMapping("coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTO spuBoundTO);


    @PostMapping("coupon/skufullreduction/saveInfo")
    R saveSkuReduction(SkuReductionTO skuReductionTO);

}
