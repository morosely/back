package com.shiji.controller;

import com.shiji.service.OrderService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    //添加订单
    @PostMapping(value = "/order", produces = "application/json")
    public String order(String userId, String commodityCode, int orderCount) {
        return orderService.order(userId,commodityCode,orderCount);
    }

}
