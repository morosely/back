package com.changgou.search.controller;

import com.changgou.common.Result;
import com.changgou.common.StatusCode;
import com.changgou.search.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/search")
public class SkuController {

    @Autowired
    private SkuService skuService;

    @RequestMapping("/import")
    public Result importEs() {

        skuService.importEs();
        return new Result(true, StatusCode.OK, "导入成功");
    }
    /**
     *
     * @param searchMap  搜索的条件 map
     * @return  resultMap  返回的结果 map
     */
    @GetMapping
    public Map search(@RequestParam(required = false) Map<String, String> searchMap){
        return  skuService.search(searchMap);
    }
}
