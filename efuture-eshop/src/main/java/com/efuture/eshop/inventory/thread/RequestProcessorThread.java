package com.efuture.eshop.inventory.thread;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

import com.efuture.eshop.inventory.request.GoodsCacheRefreshRequest;
import com.efuture.eshop.inventory.request.GoodsDBUpdateRequest;
import com.efuture.eshop.inventory.request.Request;
import com.efuture.eshop.inventory.request.RequestQueues;

public class RequestProcessorThread implements Callable<Boolean>{

	private ArrayBlockingQueue<Request> queue;

	public RequestProcessorThread(ArrayBlockingQueue<Request> queue) {
		this.queue = queue;
	}
	
	@Override
	public Boolean call() throws Exception {
		try {
			while(true) {
				// ArrayBlockingQueue
				// Blocking就是说明，如果队列满了，或者是空的，那么都会在执行操作的时候，阻塞住
				Request request = queue.take();
				boolean forceRfresh = request.isForceRefresh();
				
				// 先做读请求的去重
				if(!forceRfresh) {
					RequestQueues requestQueues = RequestQueues.getInstance();
					Map<Long, Boolean> flagMap = requestQueues.getFlagMap();
					if(request instanceof GoodsDBUpdateRequest) {
						// 如果是一个更新数据库的请求，那么就将那个goodsId对应的标识设置为true
						flagMap.put(request.getGoodsId(), true);
					} else if(request instanceof GoodsCacheRefreshRequest) {
						Boolean flag = flagMap.get(request.getGoodsId());
						
						// 如果flag是null
						if(flag == null) {
							flagMap.put(request.getGoodsId(), false);
						}
						
						// 如果是缓存刷新的请求，那么就判断，如果标识不为空，而且是true，就说明之前有一个这个商品的数据库更新请求
						if(flag != null && flag) {
							flagMap.put(request.getGoodsId(), false);
						}
						
						// 如果是缓存刷新的请求，而且发现标识不为空，但是标识是false
						// 说明前面已经有一个数据库更新请求+一个缓存刷新请求了，大家想一想
						if(flag != null && !flag) {
							// 对于这种读请求，直接就过滤掉，不要放到后面的内存队列里面去了
							return true;
						}
					}
				}
				
				System.out.println("===========日志===========: 工作线程处理请求，商品id=" + request.getGoodsId()); 
				// 执行这个request操作
				request.process();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
