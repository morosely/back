package com.shiji.service;

import com.shiji.dao.OrderDao;
import com.shiji.model.Order;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class OrderService {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";
    private static final String USER_ID = "U100001";
    private static final String COMMODITY_CODE = "C00321";

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RestTemplate restTemplate;


    //添加订单
    @Transactional
    public String order(String userId, String commodityCode, int orderCount) {
        log.info("Order Service Begin ... xid: " + RootContext.getXID());
        int orderMoney = calculate(commodityCode, orderCount);
        //扣减账户余额
        invokerAccountService(orderMoney);
        //添加订单
        Order order = new Order();
        order.userId = userId;
        order.commodityCode = commodityCode;
        order.count = orderCount;
        order.money = orderMoney;
        int result = orderDao.insertSelective(order);
        log.info("----------》》》 添加订单受影响函数："+result);
        log.info("Order Service End ... Created " + order);
        if (result == 1) {
            return SUCCESS;
        }
        return FAIL;
    }

    //计算订单价格
    private int calculate(String commodityId, int orderCount) {
        return 2 * orderCount;
    }

    //扣减账户余额
    private void invokerAccountService(int orderMoney) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("userId", USER_ID);
        map.add("money", orderMoney + "");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>( map, headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity("http://seata-account/account", request, String.class);
        }
        catch (Exception e) {
            throw new RuntimeException("----------》》》 远程调用【用户】接口失败！"+e.getMessage());
        }
        String result = response.getBody();
        if (!SUCCESS.equals(result)) {
            throw new RuntimeException();
        }
    }


}
