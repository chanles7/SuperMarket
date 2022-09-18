package com.mail.product.feign;


import com.mail.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("mail-depository")
public interface DepositoryFeignService {

    @GetMapping("depository/waresku/hasStock")
    R getHasStock(@RequestParam List<Long> ids);

}
