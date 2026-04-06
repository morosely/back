package com.changgou.order.controller;

import com.changgou.common.Result;
import com.changgou.common.StatusCode;
import com.changgou.common.TokenDecode;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    @GetMapping("/addCart")
    public Result addCart(@RequestParam("skuId") String skuId, @RequestParam("num") Integer num){
        //String username = "itcast";
        //动态获取当前人信息,暂时静态
        String username = TokenDecode.getUserInfo().get("username");
        cartService.addCart(skuId,num,username);
        return new Result(true, StatusCode.OK,"加入购物车成功");
    }

    @GetMapping("/list")
    public Map list(){
        //String username = "itcast";
        //动态获取当前人信息,暂时静态
        String username = TokenDecode.getUserInfo().get("username");
        Map map = cartService.list(username);
        return map;
    }
}
