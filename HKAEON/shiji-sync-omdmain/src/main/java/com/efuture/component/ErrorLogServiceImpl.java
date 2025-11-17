package com.efuture.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.common.SpringUtil;
import com.efuture.executor.jobhandler.DeleteGoodsHandler;
import com.efuture.executor.jobhandler.DeleteSaleGoodsHandler;
import com.efuture.model.ErrorLogModel;
import com.product.util.UniqueID;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ErrorLogServiceImpl {
	
	public Logger logger = LoggerFactory.getLogger(ErrorLogServiceImpl.class);
	public SqlSessionTemplate getTemplate() {
        return SpringUtil.getBean("sqlSessionTemplate", SqlSessionTemplate.class);
    }
	
	public void insertLog(String tableName, String param, String errorMsg,String operatorType, String paramType) {
		Long errorId = UniqueID.getUniqueID();
		ErrorLogModel errorLogModel = new ErrorLogModel(errorId, tableName, param, errorMsg, operatorType, paramType);
		try{
			 this.getTemplate().insert("ErrorLogModelMapper.insert", errorLogModel);
		}catch (Exception e) {
			logger.info("==========》》》 插入日志表报错 ==========》》》" + e.getMessage());
		}
	}

	public List<String> queryShopCode(){
		return this.getTemplate().selectList("ErrorLogModelMapper.queryShopCode");
	}

	public Integer deleteSaleGoods(JSONObject param){
		List ids = new ArrayList();
		Integer deleteSum = 0 ;
		do{
			if(DeleteSaleGoodsHandler.stopFlag){
				break;
			}
			ids = this.getTemplate().selectList("ErrorLogModelMapper.querySaleGoods",param);
			if(!ids.isEmpty()) {
				param.put("values", ids);
				Integer result = this.getTemplate().delete("ErrorLogModelMapper.deleteSaleGoods", param);
				logger.info("【deleteSaleGoods 无效状态status:-1】【当前批次】----->>> 当前门店【{}】删除无效数据条数: {}",param.getString("shopCode"), result);
				deleteSum = deleteSum + result;
			}
		}while (!ids.isEmpty());
		return deleteSum;
	}

	public void deleteGoods(JSONObject param){
		try {
			Integer totalCount = param.getInteger("totalCount") ;
			Integer pageSize = param.getInteger("pageSize");
			Integer pageCount = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
			logger.info("【deleteGoods】 ----->>> 执行参数:待删除goods总条数【{}】-总页数【{}】-每次删除条数【{}】",totalCount,pageCount,pageSize);
			for(int i = 1; i <= pageCount ; i++){
				if(DeleteGoodsHandler.stopFlag){
					break;
				}
				//1.获取pageSize的待删除数据
				List<String> values = this.getTemplate().selectList("ErrorLogModelMapper.queryDeleteGoodsCode",param);
				logger.info("【deleteGoods】 ----->>> 1.查询待删除goods实际条数:【{}】",values.size());
				if(values == null || values.isEmpty()){
					break;
				}
				//2.删除三张核心表 goods、goodsshopref、aeonmorebarno 以及删除salegoods，并且将删除的数据备份到salegoods_log_delete
				param.put("values",values);
				Integer result = this.getTemplate().delete("ErrorLogModelMapper.deleteGoods", param);
				logger.info("【deleteGoods】 ----->>> 2.第【{}】页:完成删除商品，价格，多条码，可售商品",i);
				//3.更新goods_delete处理状态
				result = this.getTemplate().update("ErrorLogModelMapper.updateStatus", param);
				logger.info("【deleteGoods】 ----->>> 3.更新goods_delete的状态条数:【{}】",result);
				Integer sleepTime = param.getInteger("sleepTime");
				if(sleepTime != null && sleepTime > 0){
					logger.info("【deleteGoods】 ----->>>4.休眠{}s，后再处理下一批数据",sleepTime);
						TimeUnit.SECONDS.sleep(sleepTime);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【deleteGoods】----->>> 发生异常",e);
		}
	}

}
