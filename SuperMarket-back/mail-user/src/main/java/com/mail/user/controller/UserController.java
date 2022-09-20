package com.mail.user.controller;


import com.mail.common.util.R;
import com.mail.user.service.UserService;
import com.mail.user.vo.req.UserReqVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("user")
public class UserController {


    @Resource
    private UserService userService;


    @GetMapping("send")
    public R sendCode(String phone) {
        return userService.sendCode(phone);
    }


    @PostMapping("register")
    public R register(UserReqVO userReqVO) {
        return userService.register(userReqVO);
    }


    @GetMapping("login")
    public R login(UserReqVO userReqVO){
        return userService.login(userReqVO);
    }

}
