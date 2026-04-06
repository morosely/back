package com.efuture.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efuture.domain.User;
import com.efuture.redis.RedisService;
import com.efuture.result.Result;
import com.efuture.service.GoodsService;
import com.efuture.service.UserService;
import com.efuture.vo.GoodsDetailVo;
import com.efuture.vo.GoodsVo;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	UserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;

    @RequestMapping(value="/to_list")
    public String list(Model model,User user) {
    	model.addAttribute("user", user);
    	List<GoodsVo> goodsList = goodsService.listGoodsVo();
    	model.addAttribute("goodsList", goodsList);
    	 return "goods_list";
    }
    
//    @RequestMapping("/to_detail/{goodsId}")
//    public String detail(Model model,User user,
//    		@PathVariable("goodsId")long goodsId) {
//    	model.addAttribute("user", user);
//    	
//    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//    	model.addAttribute("goods", goods);
//    	
//    	long startAt = goods.getStartDate().getTime();
//    	long endAt = goods.getEndDate().getTime();
//    	long now = System.currentTimeMillis();
//    	
//    	int miaoshaStatus = 0;
//    	int remainSeconds = 0;
//    	if(now < startAt ) {//秒杀还没开始，倒计时
//    		miaoshaStatus = 0;
//    		remainSeconds = (int)((startAt - now )/1000);
//    	}else  if(now > endAt){//秒杀已经结束
//    		miaoshaStatus = 2;
//    		remainSeconds = -1;
//    	}else {//秒杀进行中
//    		miaoshaStatus = 1;
//    		remainSeconds = 0;
//    	}
//    	model.addAttribute("miaoshaStatus", miaoshaStatus);
//    	model.addAttribute("remainSeconds", remainSeconds);
//        return "goods_detail";
//    }
    
    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model,User user,
    		@PathVariable("goodsId")long goodsId) {
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	System.out.println(goods.getStartDate().toLocaleString() + " --- " +goods.getEndDate().toLocaleString());
    	long startAt = goods.getStartDate().getTime();
    	long endAt = goods.getEndDate().getTime();
    	long now = System.currentTimeMillis();
    	int miaoshaStatus = 0;
    	int remainSeconds = 0;
    	if(now < startAt ) {//秒杀还没开始，倒计时
    		miaoshaStatus = 0;
    		remainSeconds = (int)((startAt - now )/1000);
    	}else  if(now > endAt){//秒杀已经结束
    		miaoshaStatus = 2;
    		remainSeconds = -1;
    	}else {//秒杀进行中
    		miaoshaStatus = 1;
    		remainSeconds = 0;
    	}
    	GoodsDetailVo vo = new GoodsDetailVo();
    	vo.setGoods(goods);
    	vo.setUser(user);
    	vo.setRemainSeconds(remainSeconds);
    	vo.setMiaoshaStatus(miaoshaStatus);
    	return Result.success(vo);
    }
}
