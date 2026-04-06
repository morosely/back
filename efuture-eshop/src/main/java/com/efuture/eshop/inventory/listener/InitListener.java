package com.efuture.eshop.inventory.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.efuture.eshop.inventory.thread.RequestProcessorThreadPool;

/**
 * 系统初始化监听器
 * @author Administrator
 *
 */
@WebListener
public class InitListener implements ServletContextListener {

	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// 初始化工作线程池和内存队列
		RequestProcessorThreadPool.getInstance();
		System.out.println("==========>>> 初始化工作线程池和内存队列");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}


}
