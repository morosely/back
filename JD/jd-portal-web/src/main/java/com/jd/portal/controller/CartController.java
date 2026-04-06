package com.jd.portal.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jd.common.pojo.JDReturnResult;
import com.jd.common.utils.CookieUtils;
import com.jd.common.utils.JsonUtils;
import com.jd.pojo.TbItem;
import com.jd.service.ItemService;

@Controller
public class CartController {
	
	@Value("${CART_COOKIE}")
	private String CART_COOKIE;
	@Value("${CART_EXPIER}")
	private Integer CART_EXPIER;

	@Autowired
	private ItemService itemService;

	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request,
			HttpServletResponse response) {
		// 1、从url中取商品id
		// 2、从cookie中取购物车商品列表
		List<TbItem> cartList = getCartList(request);
		// 3、遍历列表找到对应的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId() == itemId.longValue()) {
				// 4、删除商品。
				cartList.remove(tbItem);
				break;
			}
		}
		// 5、把商品列表写入cookie。
		CookieUtils.setCookie(request, response, CART_COOKIE, JsonUtils.objectToJson(cartList), CART_EXPIER, true);
		// 6、返回逻辑视图：在逻辑视图中做redirect跳转。
		return "redirect:/cart/cart.html";
	}
	
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public JDReturnResult updateItemNum(@PathVariable Long itemId, @PathVariable Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartList(request);
		//查询到对应的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId() == itemId.longValue()) {
				//更新商品数量
				tbItem.setNum(num);
				break;
			}
		}
		//把购车列表写入 cookie
		CookieUtils.setCookie(request, response, CART_COOKIE, JsonUtils.objectToJson(cartList),
				CART_EXPIER, true);
		//返回成功
		return JDReturnResult.success();
	}
	
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request, Model model) {
		//取购物车商品列表
		List<TbItem> cartList = getCartList(request);
		//传递给页面
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	
	@RequestMapping("/cart/add/{itemId}")
	public String addCartItem(@PathVariable Long itemId, Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		// 1、从cookie中查询商品列表。
		List<TbItem> cartList = getCartList(request);
		// 2、判断商品在商品列表中是否存在。
		boolean hasItem = false;
		for (TbItem tbItem : cartList) {
			//对象比较的是地址，应该是值的比较
			if (tbItem.getId() == itemId.longValue()) {
				// 3、如果存在，商品数量相加。
				tbItem.setNum(tbItem.getNum() + num);
				hasItem = true;
				break;
			}
		}
		if (!hasItem) {
			// 4、不存在，根据商品id查询商品信息。
			TbItem tbItem = itemService.getItemById(itemId);
			//取一张图片
			String image = tbItem.getImage();
			if (StringUtils.isNoneBlank(image)) {
				String[] images = image.split(",");
				tbItem.setImage(images[0]);
			}
			//设置购买商品数量
			tbItem.setNum(num);
			// 5、把商品添加到购车列表。
			cartList.add(tbItem);
		}
		// 6、把购车商品列表写入cookie。
		CookieUtils.setCookie(request, response, CART_COOKIE, JsonUtils.objectToJson(cartList), CART_EXPIER, true);
		return "cartSuccess";
	}
	
	/**
	 * 从cookie中取购物车列表
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