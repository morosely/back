package com.efuture.executor.jobhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.efuture.convert.HelloApiService;
import com.product.model.ServiceSession;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler("helloHandler")
@Component
public class HelloHandler extends IJobHandler {
	
	@Autowired
	private HelloApiService HelloService;

	@Override
	public ReturnT<String> execute(String s) throws Exception {
		String result = HelloService.execute(new ServiceSession(), s);
		return new ReturnT<String>(result);
	}

    /*private static final Logger LOG = LoggerFactory.getLogger(HelloHandler.class);
    private static final int DEF_PAGE_SIZE = 4000;
    

    @SuppressWarnings("rawtypes")
	@Override
    public ReturnT<String> execute(String s) throws Exception {
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
        	return new ReturnT<>("暂无待更新数据");
        }
        
        int groupSize = getGroupSize(dataCount, pageSize);
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(groupSize);
        for (int i = 0; i < groupSize; i++) {
        	queue.put(i * pageSize);
        }
        
        // 执行线程数
        int threadCount = 16;
        List<Future<Long>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        
        
        for (int i = 0; i < threadCount; i++) {
        	Future<Long> future = executorService.submit(new MyThread(queue, extReceiveService, convertService, pageSize));
        	futures.add(future);
        }
        
        long updateCount = 0L;
        for (Future<Long> future : futures) {
        	// 获取线程执行结果
        	updateCount += future.get();
        } 
        
    	if (dataCount == updateCount) {
            extReceiveService.updateDealStatus(param);
        } else {
            return new ReturnT<>(String.format("转换出错, 需要转换总数{%1$s}, 成功条数{%1$s}", dataCount, updateCount));
        }
    	
    	LOG.info("耗时: {}秒", (System.currentTimeMillis() - startTime) / 1000);
        return new ReturnT<>("success");
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
	
	private ArrayBlockingQueue<Integer> queue;
	private BaseExtReceiveService extReceiveService;
	private BaseConvertService convertService;
	private int pageSize;
	
	public MyThread(ArrayBlockingQueue<Integer> queue, BaseExtReceiveService extReceiveService,
			BaseConvertService convertService, int pageSize) {
		super();
		this.queue = queue;
		this.extReceiveService = extReceiveService;
		this.convertService = convertService;
		this.pageSize = pageSize;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Long call() {
		Long updateCount = 0L;
		
		JSONObject param = new JSONObject();
		try {
			while (!queue.isEmpty()) {
				Integer pageNo = queue.take();
				param.put("pageNo", pageNo);
				param.put("pageSize", pageSize);
				param.put("tableName", extReceiveService.getTableName());
				List<JSONObject> data = extReceiveService.search(param);
				updateCount += convertService.receive(extReceiveService, data);
			}
			return updateCount;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0L;
	}
	*/
	
	
}
