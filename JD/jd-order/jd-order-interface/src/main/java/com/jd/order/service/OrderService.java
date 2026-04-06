package com.jd.order.service;

import com.jd.common.pojo.JDReturnResult;
import com.jd.order.pojo.OrderInfo;

public interface OrderService {

	JDReturnResult createOrder(OrderInfo orderInfo);
}
