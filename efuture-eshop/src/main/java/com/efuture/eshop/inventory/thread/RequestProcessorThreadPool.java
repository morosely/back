package com.efuture.eshop.inventory.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.efuture.eshop.inventory.request.Request;
import com.efuture.eshop.inventory.request.RequestQueues;

public class RequestProcessorThreadPool {

	private Integer capacity = 10;
	
	private ExecutorService threadPool = Executors.newFixedThreadPool(capacity);
	
	private RequestProcessorThreadPool(){
		RequestQueues requestQueues = RequestQueues.getInstance();
		for (int i = 0; i < capacity; i++) {
			ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<Request>(100);
			requestQueues.addQueue(queue);
			threadPool.submit(new RequestProcessorThread(queue));
		}
	}
	
	private static class RequestProcessorThreadPoolHold{
		private static RequestProcessorThreadPool pool = new RequestProcessorThreadPool();
	}
	
    public static RequestProcessorThreadPool getInstance(){
    	return RequestProcessorThreadPoolHold.pool;
    }
}
