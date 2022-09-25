package com.mail.cart.controller;


import com.mail.cart.service.CartService;
import com.mail.common.util.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("cart")
public class CartController {

    @Resource
    private CartService cartService;


    @PostMapping("addOrUpdateCart/{skuId}/{number}")
    public R addOrUpdateCart(@PathVariable("skuId")Long skuId, @PathVariable("number")Integer num){
        return cartService.addOrUpdateCart(skuId,num);
    }


    @GetMapping("list")
    public R list(){
        return cartService.list();
    }


    @PostMapping("updateCheck/{skuId}/{check}")
    public R updateCheck(@PathVariable("skuId")Long skuId, @PathVariable("check")Boolean check){
        return cartService.updateCheck(skuId,check);
    }


    @GetMapping("confirmOrder")
    public R getCurrentCart(){
        return cartService.getCurrentCart();
    }

}
