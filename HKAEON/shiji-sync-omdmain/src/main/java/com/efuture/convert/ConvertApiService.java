package com.efuture.convert;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.common.CommonExtSyncService;
import com.efuture.common.CommonSyncService;
import com.efuture.common.SpringUtil;
import com.product.model.ServiceSession;
import com.product.util.UniqueID;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("convertApiService")
public class ConvertApiService {
	private static final Logger logger = LoggerFactory.getLogger(ConvertApiService.class);
	private static final int DEF_PAGE_SIZE = 5000;

	protected SqlSessionTemplate getOmdTemplate() {
		return SpringUtil.getBean("sqlSessionTemplate", SqlSessionTemplate.class);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String execute(ServiceSession session, String str) throws Exception {
		logger.info(" ----------==========》》》》》》》》》》 【DX】2023-04-28 14:30 最新DX同步代码（兼容文件解析同步代码）");
		long startTime = System.currentTimeMillis();
		Date startDate = new Date();
		JSONObject executeParam = JSON.parseObject(str, JSONObject.class);
		CommonExtSyncService extReceiveService = getExtService( "sync."+executeParam.getString("extTable"));
		CommonSyncService convertService = getConvertService("sync."+executeParam.getString("sourceTable"));
		int pageSize = executeParam.get("pageSize") == null ? DEF_PAGE_SIZE : executeParam.getInteger("pageSize");

		//海外购标识
		Boolean abroadBuyFlag = executeParam.getBoolean("abroadBuyFlag");
		abroadBuyFlag = abroadBuyFlag == null ? false : abroadBuyFlag;
		convertService.setAbroadBuyFlag(abroadBuyFlag);

		//商品受限标识
		Boolean seasonFlag = executeParam.getBoolean("seasonFlag");
		seasonFlag = seasonFlag == null ? false : seasonFlag;
		convertService.setSeasonFlag(seasonFlag);
		
		JSONObject param = new JSONObject();
		param.put("tableName", extReceiveService.getTableName());

		// 待更新ext表的数据总数
		long dataCount = extReceiveService.dataCount(param);
		logger.info("==========>>> table[{}]待转换数据[{}]条.", extReceiveService.getTableName(), dataCount);
		long updateCount = 0L;

		// 分组测试一下
		if (dataCount == 0L) {
			//通过Code设置Id
			convertService.resetIdByCode();
			long costTime  = System.currentTimeMillis() - startTime;
			logger.info("table[{}]没有待转换数据.处理总耗时{}", extReceiveService.getTableName(),costTime);
			//insertFinishLog(startDate, convertService, dataCount);
			return "暂无待更新数据。处理总耗时："+costTime;
		} else {
		    JSONObject p;
			while (true) {
			    p = new JSONObject();
			    p.put("tableName", extReceiveService.getTableName());
			    p.put("pageSize", pageSize);
			    List<JSONObject> data = extReceiveService.search(p); 
				if (data == null || data.size() == 0) {
					break;
				}
				updateCount += convertService.receive(extReceiveService,data);
			}
		}

//		if (dataCount != updateCount) {
//			logger.error("转换出错, table[{}]需要转换总数{},  成功条数{}", extReceiveService.getTableName(), dataCount, updateCount);
//			insertFinishLog(startDate, convertService, dataCount);
//			return String.format("转换出错, 需要转换总数{%1$s}, 成功条数{%1$s}", dataCount, updateCount);
//		}

		String sourceTable = executeParam.getString("sourceTable");
		if (StringUtils.equalsIgnoreCase("shop", sourceTable)
				|| StringUtils.equalsIgnoreCase("category", sourceTable)
				|| StringUtils.equalsIgnoreCase("saleorg", sourceTable)) {
			convertService.onSyncRedis();
		}
		
		//通过Code设置Id
		convertService.resetIdByCode();
		//如果存在增量数据
		if(dataCount>0){
			convertService.changeDataAfter();
		}
		long entTime = System.currentTimeMillis();
		long costTime  = entTime - startTime;
		logger.info("---------- >>> 处理总耗时{}ms",costTime);
		insertFinishLog(startDate, convertService, dataCount);
		return "success! cost time is "+costTime+"ms";
	}

	public void insertFinishLog(Date startDate, CommonSyncService convertService, long dataCount) {
		Map paramMap = new HashMap();
		paramMap.put("syncId",UniqueID.getUniqueID(true));
		paramMap.put("startTime",startDate);
		paramMap.put("endTime",new Date());
		paramMap.put("finishFlag",1);
		String className = convertService.getClass().getSimpleName();
		String returnMsg = className.length() > 64 ? className.substring(0, className.length()):className;
		paramMap.put("returnMsg",returnMsg);
		paramMap.put("modelName",dataCount);
		getOmdTemplate().insert("AdvancedQueryMapper.insertSyncDataTime", paramMap);
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
	private CommonExtSyncService getExtService(String beanName){
		return SpringUtil.getBean(beanName, CommonExtSyncService.class);
	}

	@SuppressWarnings("rawtypes")
	private CommonSyncService getConvertService(String beanName){
		return SpringUtil.getBean(beanName, CommonSyncService.class);
	}

}