package com.shiji.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("seata-order")
public interface OrderFeign {

    @PostMapping(path = "/order")
    String order(@RequestParam("userId") String userId,
                 @RequestParam("commodityCode") String commodityCode,
                 @RequestParam("orderCount") int orderCount);
}
