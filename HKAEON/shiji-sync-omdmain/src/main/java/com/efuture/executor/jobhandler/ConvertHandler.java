package com.efuture.executor.jobhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.efuture.convert.ConvertApiService;
import com.product.model.ServiceSession;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler("convertHandler")
@Component
public class ConvertHandler extends IJobHandler {
	
	@Autowired
	private ConvertApiService convertApiService;

	@Override
	public ReturnT<String> execute(String s) throws Exception {
		String result = convertApiService.execute(new ServiceSession(), s);
		return new ReturnT<String>(result);
	}

    /*private static final Logger LOG = LoggerFactory.getLogger(ConvertHandler.class);
    private static final int DEF_PAGE_SIZE = 4000;

    @SuppressWarnings({ "rawtypes", "unchecked" })
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
        long updateCount = 0L;

        // 分组测试一下
        if (dataCount == 0L) {
            return new ReturnT<>("暂无待更新数据");
        } else {
            int groupSize = getGroupSize(dataCount, pageSize);
            for (int i = 0; i < groupSize; i++) {
                int pageNo = i * pageSize;
                param.put("pageNo", String.valueOf(pageNo));
                param.put("pageSize", String.valueOf(pageSize));

                List<JSONObject> data  = extReceiveService.search(param);
                updateCount += convertService.receive(extReceiveService, data);
            }
        }

        if (dataCount == updateCount) {
            extReceiveService.updateDealStatus(param);
        } else {
        	LOG.error("转换出错,  需要转换总数{},  成功条数{}", dataCount, updateCount);
            return new ReturnT<>(String.format("转换出错, 需要转换总数{%1$s}, 成功条数{%1$s}", dataCount, updateCount));
        }

        LOG.info("耗时: {}秒", (System.currentTimeMillis() - startTime) / 1000);
        return new ReturnT<>("success");
    }

    private int getGroupSize(long dataCount, int pageSize) {
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
    }*/
}
