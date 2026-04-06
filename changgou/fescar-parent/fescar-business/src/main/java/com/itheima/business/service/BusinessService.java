package com.itheima.business.service;

import com.itheima.business.feign.OrderFeignClient;
import com.itheima.business.feign.StorageFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 描述
 *
 * @author www.itheima.com
 * @version 1.0
 * @package com.itheima.business.service *
 * @since 1.0
 */
@Service
public class BusinessService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private StorageFeignClient storageFeignClient;

    /**
     * 下采购订单
     *
     * @param userId        商家的ID
     * @param commodityCode 商品编码
     * @param count         购买的数量
     */
    @GlobalTransactional
    public void purchase(String userId, String commodityCode, int count) {
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = sdf.format(calendar.getTime());
        //添加日志
        jdbcTemplate.update("insert into log_info(content,createtime) values ('Test','"+time+"')");
        //调用feign 下单--->扣款
        orderFeignClient.create(userId, commodityCode, count);
        //调用feign 扣减库存
        storageFeignClient.deduct(commodityCode, count);

    }
}
