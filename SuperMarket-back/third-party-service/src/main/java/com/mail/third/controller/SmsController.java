package com.mail.third.controller;

import com.mail.common.util.R;
import com.mail.third.service.SmsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("third/sms")
public class SmsController {


    @Resource
    private SmsService smsService;


    @GetMapping("sendCode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        return smsService.sendCode(phone, code);
    }
}
