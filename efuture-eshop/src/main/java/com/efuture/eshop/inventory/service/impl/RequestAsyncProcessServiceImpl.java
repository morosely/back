package com.efuture.eshop.inventory.service.impl;

import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.stereotype.Service;

import com.efuture.eshop.inventory.request.Request;
import com.efuture.eshop.inventory.request.RequestQueues;
import com.efuture.eshop.inventory.service.RequestAsyncProcessService;

@Service("requestAsyncProcessService")  
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {

	@Override
	public void process(Request request) {
		try {
			// 做请求的路由，根据每个请求的商品id，路由到对应的内存队列中去
			ArrayBlockingQueue<Request> queue = getRoutingQueue(request.getGoodsId());
			// 将请求放入对应的队列中，完成路由操作
			queue.put(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取路由到的内存队列
	 * @param goodsId 商品id
	 * @return 内存队列
	 */
	private ArrayBlockingQueue<Request> getRoutingQueue(Long goodsId) {
		RequestQueues requestQueues = RequestQueues.getInstance();
		// 先获取goodsId的hash值
		String key = String.valueOf(goodsId);
		int h;
		int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
		
		// 对hash值取模，将hash值路由到指定的内存队列中，比如内存队列大小8
		// 用内存队列的数量对hash值取模之后，结果一定是在0~7之间
		// 所以任何一个商品id都会被固定路由到同样的一个内存队列中去的
		int index = (requestQueues.queueSize() - 1) & hash;
		return requestQueues.getQueue(index);
	}
}
