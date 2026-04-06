package com.efuture.eshop.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efuture.eshop.inventory.model.Goods;
import com.efuture.eshop.inventory.request.GoodsCacheRefreshRequest;
import com.efuture.eshop.inventory.request.GoodsDBUpdateRequest;
import com.efuture.eshop.inventory.request.Request;
import com.efuture.eshop.inventory.service.GoodsService;
import com.efuture.eshop.inventory.service.RequestAsyncProcessService;
import com.efuture.eshop.inventory.vo.ResponseData;

/**
 * 商品库存Controller
 * @author Administrator
 */
@Controller
public class GoodsController {

	@Autowired
	private RequestAsyncProcessService requestAsyncProcessService;
	@Autowired
	private GoodsService goodsService;
	
	/**
	 * 更新商品库存
	 */
	@RequestMapping("/updateGoods")
	@ResponseBody
	public ResponseData updateProductInventory(Goods goods) {
		ResponseData response = null;
		
		try {
			Request request = new GoodsDBUpdateRequest(goods);
			requestAsyncProcessService.process(request);
			response = ResponseData.success();
		} catch (Exception e) {
			e.printStackTrace();
			response = ResponseData.fail();
		}
		return response;
	}
	
	/**
	 * 获取商品库存
	 */
	@RequestMapping("/getGoods")
	@ResponseBody
	public Goods getGoods(Long goodsId) {
		Goods goods = null;
		
		try {
			Request request = new GoodsCacheRefreshRequest(goodsId,false);
			requestAsyncProcessService.process(request);
			
			// 将请求扔给service异步去处理以后，就需要while(true)一会儿，在这里hang住
			// 去尝试等待前面有商品库存更新的操作，同时缓存刷新的操作，将最新的数据刷新到缓存中
			long startTime = System.currentTimeMillis();
			long endTime = 0L;
			long waitTime = 0L;
			
			// 等待超过200ms没有从缓存中获取到结果
			while(true) {
				if(waitTime > 200) {
					break;
				}
				
				// 尝试去redis中读取一次商品库存的缓存数据
				goods = goodsService.getGoodsCache(goodsId);
				
				// 如果读取到了结果，那么就返回
				if(goods != null) {
					return goods;
				}else {// 如果没有读取到结果，那么等待一段时间
					Thread.sleep(20);
					endTime = System.currentTimeMillis();
					waitTime = endTime - startTime;
				}
			}
			
			// 直接尝试从数据库中读取数据
			goods = goodsService.findGoods(goodsId);
			if(goods != null) {
				// 将缓存刷新一下
				// 这个过程，实际上是一个读操作的过程，但是没有放在队列中串行去处理，还是有数据不一致的问题
				request = new GoodsCacheRefreshRequest(goodsId, true);
				requestAsyncProcessService.process(request);
				
				// 代码会运行到这里，只有三种情况：
				// 1、就是说，上一次也是读请求，数据刷入了redis，但是redis LRU算法给清理掉了，标志位还是false
				// 所以此时下一个读请求是从缓存中拿不到数据的，再放一个读Request进队列，让数据去刷新一下
				// 2、可能在200ms内，就是读请求在队列中一直积压着，没有等待到它执行（在实际生产环境中，基本是比较坑了）
				// 所以就直接查一次库，然后给队列里塞进去一个刷新缓存的请求
				// 3、数据库里本身就没有，缓存穿透，穿透redis，请求到达mysql库
				return goods;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new Goods(goodsId, -1);  
	}
}
