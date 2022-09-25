package com.mail.user.feign;

import com.mail.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("mail-third")
public interface ThirdFeignService {

    @GetMapping("third/sms/sendCode")
    R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);

}
