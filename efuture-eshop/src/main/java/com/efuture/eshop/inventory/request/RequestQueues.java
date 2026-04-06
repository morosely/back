package com.efuture.eshop.inventory.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class RequestQueues {

	private RequestQueues(){
		
	}
	
	private static class RequestQueueHold{
		private static RequestQueues instance = new RequestQueues();
	}
	
	public static RequestQueues getInstance(){
		return RequestQueueHold.instance;
	}
	
	private List<ArrayBlockingQueue<Request>> queues = new ArrayList<ArrayBlockingQueue<Request>>();
	private Map<Long, Boolean> flagMap = new ConcurrentHashMap<Long, Boolean>();
	
	public void addQueue(ArrayBlockingQueue<Request> queue){
		queues.add(queue);
	}
	
	/**
	 * 获取内存队列的数量
	 * @return
	 */
	public int queueSize() {
		return queues.size();
	}
	
	/**
	 * 获取内存队列
	 * @param index
	 * @return
	 */
	public ArrayBlockingQueue<Request> getQueue(int index) {
		return queues.get(index);
	}
	
	/**
	 * 获取标识位Map
	 * @return
	 */
	public Map<Long, Boolean> getFlagMap() {
		return flagMap;
	}
}
