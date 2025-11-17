package com.efuture.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.efuture.config.DataConfiger;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.common.BaseConvertService;
import com.efuture.common.BaseExtReceiveService;
import com.efuture.common.SpringUtil;
import com.product.model.ServiceSession;

@Service("helloService")
public class HelloApiService {
	private static final Logger LOG = LoggerFactory.getLogger(HelloApiService.class);
	private static final int DEF_PAGE_SIZE = 4000;

	@SuppressWarnings("rawtypes")
	public String execute(ServiceSession session, String s) throws Exception {
		long startTime = System.currentTimeMillis();
		JSONObject executeParam = JSON.parseObject(s, JSONObject.class);
		BaseExtReceiveService extReceiveService = getExtService(executeParam.getString("extTable"));
		BaseConvertService convertService = getConvertService(executeParam.getString("sourceTable"));
		int pageSize;
		if (!executeParam.containsKey("pageSize")) {
			pageSize = DEF_PAGE_SIZE;
		} else {
			int dump = executeParam.getIntValue("pageSize");
			if (dump == 0) {
				pageSize = DEF_PAGE_SIZE;
			} else {
				pageSize = dump;
			}
		}

		JSONObject param = new JSONObject();
		param.put("tableName", extReceiveService.getTableName());

		// 待更新ext表的数据总数
		long dataCount = extReceiveService.dataCount(param);

		if (dataCount == 0L) {
			LOG.info("table[{}]没有待更新数据.", extReceiveService.getTableName());
			return "暂无待更新数据";
		}

		// 分组处理
		int groupSize = getGroupSize(dataCount, pageSize);

		// 存放分页参数
		ArrayBlockingQueue<JSONObject> queue = new ArrayBlockingQueue<>(groupSize);
		JSONObject pageParam;
		for (int i = 0; i < groupSize; i++) {
			pageParam = new JSONObject();
			int pageNo = i * pageSize;
			pageParam.put("pageNo", pageNo);

			if (i == groupSize - 1) {
				pageParam.put("pageSize", dataCount - pageNo);
			} else {
				pageParam.put("pageSize", pageSize);
			}
			queue.put(pageParam);
		}

		// 执行线程数
		int threadCount = 8;
		List<Future<Long>> futures = new ArrayList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

		for (int i = 0; i < threadCount; i++) {
			Future<Long> future = executorService
					.submit(new MyThread(queue, extReceiveService, convertService));
			futures.add(future);
		}

		long updateCount = 0L;
		for (Future<Long> future : futures) {
			// 获取线程执行结果
			updateCount += future.get();
		}

		if (dataCount == updateCount) {
			LOG.info("总数:{}, 成功条数:{}", dataCount, updateCount);
			//extReceiveService.updateDealStatus(param);
		} else {
			LOG.error("转换出错,  需要转换总数{},  成功条数{}", dataCount, updateCount);
			return String.format("转换出错, 需要转换总数{%1$s}, 成功条数{%1$s}", dataCount, updateCount);
		}

		LOG.info("耗时: {}ms", (System.currentTimeMillis() - startTime));
		return "success";
	}



	private int getGroupSize(long dataCount, int pageSize) throws Exception {
		if (dataCount < pageSize) {
			return 1;
		} else if (dataCount % pageSize == 0) {
			return (int) (dataCount / pageSize);
		} else {
			return (int) (dataCount / pageSize + 1);
		}
	}

	@SuppressWarnings("rawtypes")
	private BaseExtReceiveService getExtService(String beanName) {
		return SpringUtil.getBean(beanName, BaseExtReceiveService.class);
	}

	@SuppressWarnings("rawtypes")
	private BaseConvertService getConvertService(String beanName) {
		return SpringUtil.getBean(beanName, BaseConvertService.class);
	}
}

@SuppressWarnings("rawtypes")
class MyThread implements Callable<Long> {

	private ArrayBlockingQueue<JSONObject> queue;
	private BaseExtReceiveService extReceiveService;
	private BaseConvertService convertService;

	public MyThread(ArrayBlockingQueue<JSONObject> queue, BaseExtReceiveService extReceiveService,
			BaseConvertService convertService) {
		super();
		this.queue = queue;
		this.extReceiveService = extReceiveService;
		this.convertService = convertService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long call() {
		Long updateCount = 0L;

		try {
			while (!queue.isEmpty()) {
				JSONObject param = queue.take();
				param.put("tableName", extReceiveService.getTableName());

				List<JSONObject> data = extReceiveService.search(param);
				updateCount += convertService.receive(extReceiveService, data);
			}
			return updateCount;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0L;
	}

}