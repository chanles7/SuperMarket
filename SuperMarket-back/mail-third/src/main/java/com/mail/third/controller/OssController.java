package com.mail.third.controller;


import com.mail.common.util.R;
import com.mail.third.service.OssService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("third/oss")
public class OssController {


    @Resource
    private OssService ossService;


    @RequestMapping("policy")
    public R policy(){
        return ossService.policy();
    }




}
