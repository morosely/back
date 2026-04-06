package com.shiji.service;

import com.shiji.feign.OrderFeign;
import com.shiji.feign.StorageFeign;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class BusinessService {
    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";
    private static final String USER_ID = "U100001";
    private static final String COMMODITY_CODE = "C00321";
    private static final int ORDER_COUNT = 2;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private OrderFeign orderFeign;
    @Autowired
    private StorageFeign storageFeign;

    @GlobalTransactional(timeoutMills = 300000)
    public String rest(){
        //1.添加订单(Post请求)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("userId", USER_ID);
            map.add("commodityCode", COMMODITY_CODE);
            map.add("orderCount", ORDER_COUNT + "");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity("http://seata-order/order", request, String.class);
        }
            catch (Exception e) {
            throw new RuntimeException("----------》》》 远程调用【订单】接口失败！"+e.getMessage());
        }
        String result = response.getBody();
            if (!SUCCESS.equals(result)) {
            throw new RuntimeException();
        }

        //2.减少库存（Get请求）
        try {
            result = restTemplate.getForObject("http://seata-storage/storage/" + COMMODITY_CODE + "/" + ORDER_COUNT, String.class);
        }catch (Exception ex) {}
            if (!SUCCESS.equals(result)) {
            throw new RuntimeException("----------》》》 远程调用【库存】接口失败！");
        }
            return SUCCESS;
    }

    //@GlobalTransactional(timeoutMills = 300000)
    public String feign() {
        //1.添加订单
        String result = orderFeign.order(USER_ID, COMMODITY_CODE, ORDER_COUNT);
        if (!SUCCESS.equals(result)) {
            throw new RuntimeException();
        }
        //2.减少库存
        result = storageFeign.storage(COMMODITY_CODE, ORDER_COUNT);
        if (!SUCCESS.equals(result)) {
            throw new RuntimeException();
        }
        return SUCCESS;
    }
}
