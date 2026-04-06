package com.jd.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jd.common.pojo.JDReturnResult;
import com.jd.common.utils.CookieUtils;
import com.jd.common.utils.JsonUtils;
import com.jd.order.pojo.OrderInfo;
import com.jd.order.service.OrderService;
import com.jd.pojo.TbItem;
import com.jd.pojo.TbUser;

@Controller
public class OrderController {
	
	@Value("${CART_COOKIE}")
	private String CART_COOKIE;
	@Autowired
	private OrderService orderService;

	@RequestMapping(value="/order/create", method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo, HttpServletRequest request) {
		// 1、接收表单提交的数据OrderInfo。
		// 2、补全用户信息。
		TbUser user = (TbUser) request.getAttribute("user");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		// 3、调用Service创建订单。
		JDReturnResult result = orderService.createOrder(orderInfo);
		//取订单号
		String orderId = result.getData().toString();
		// a)需要Service返回订单号
		request.setAttribute("orderId", orderId);
		request.setAttribute("payment", orderInfo.getPayment());
		// b)当前日期加三天。
		DateTime dateTime = new DateTime();
		dateTime = dateTime.plusDays(3);
		request.setAttribute("date", dateTime.toString("yyyy-MM-dd"));
		// 4、返回逻辑视图展示成功页面
		return "success";
	}
	
	/**
	 * 展示订单确认页面。
	 * <p>Title: showOrderCart</p>
	 * <p>Description: </p>
	 * @return
	 */
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request) {
		//取用户id
		//从cookie中取token，然后根据token查询用户信息。需要调用sso系统的服务。
		//根据用户id查询收货地址列表
		//从cookie中取商品列表
		List<TbItem> cartList = getCartList(request);
		//传递给页面
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "order-cart";
	}
	
	/**
	 * 从cookie中取购物车列表
	 * <p>Title: getCartList</p>
	 * <p>Description: </p>
	 * @param request
	 * @return
	 */
	private List<TbItem> getCartList(HttpServletRequest request) {
		//取购物车列表
		String json = CookieUtils.getCookieValue(request, CART_COOKIE, true);
		//判断json是否为null
		if (StringUtils.isNotBlank(json)) {
			//把json转换成商品列表返回
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			return list;
		}
		return new ArrayList<>();
	}
}
