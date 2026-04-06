package com.shiji.controller;

import com.shiji.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    //使用RestTemplate调用
    @GetMapping(value = "/seata/rest", produces = "application/json")
    public String rest() {
        return businessService.rest();
    }

    //使用Feign调用
    @GetMapping(value = "/seata/feign", produces = "application/json")
    public String feign() {
        return businessService.feign();

    }

}
