package com.mail.user.controller;


import com.mail.common.util.R;
import com.mail.user.service.UserService;
import com.mail.user.vo.req.UserReqVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

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
    public R register(@RequestBody UserReqVO userReqVO) {
        return userService.register(userReqVO);
    }


    @PostMapping("login")
    public R login(@RequestBody UserReqVO userReqVO){
        return userService.login(userReqVO);
    }


    @GetMapping("getUserInfo")
    public R getUserIndo(ServletRequest req) {
        return userService.getUserIndo(req);
    }


    @RequestMapping("logout")
    public R logout(ServletRequest req) {
        return userService.logout(req);
    }

}
