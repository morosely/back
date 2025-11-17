package com.efuture.omdmain.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.efuture.omdmain.model.BaseDaemonBean;
import com.efuture.omdmain.model.BaseUserData;
import com.efuture.omdmain.service.BaseServiceCallback;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;

public abstract class JDBCCompomentDeamonServiceImpl<T extends BaseDaemonBean> extends
		JDBCCompomentServiceImpl<T> {
	
	private List<Object> resultData; 

	public List<Object> getResultData() {
		return resultData;
	}

	public void setResultData(List<Object> resultData) {
		this.resultData = resultData;
	}
	
	public void clearResultData() {
		if (this.resultData!=null) {
			this.resultData.clear();
		}
	}
	
	public void addResultData(Object data) {
		if (this.resultData==null) {
			this.resultData=new ArrayList<Object>();
		}
		this.resultData.add(data);
	}

	public JDBCCompomentDeamonServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName,
			String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

//	@Override
//	protected FMybatisTemplate getTemplate() {
//		return this.getBean("StorageOperation", FMybatisTemplate.class);
//	}

	public ServiceResponse receive(ServiceSession session,JSONObject params) throws Exception {
		String     infoName = this.getClass().getSimpleName()+".receive";
		BaseUserData userData = new BaseUserData();
		
		return this.onAction(session,params,infoName,userData,new BaseServiceCallback() {
			private long rowCount = 0l;
			
			@Override
			public ServiceResponse onCallback(ServiceSession session,Long requestID,BaseUserData threadObject,JSONObject data) {
				JSONArray  dataList=null;
				Object dataObject = data.get(getCollectionName());
				if (dataObject instanceof List) {
					dataList = data.getJSONArray(getCollectionName());
				} else {

					Object dataRow=data.get(getCollectionName());
					if (dataRow!=null) {
						dataList = new JSONArray();
						dataList.add(dataRow);
					}
				}
				
				if (dataList == null||dataList.size()==0) {
					//调试期间输出请求数据
					setLastErrMessage("请求数据无数据行!");
					
					return ServiceResponse.buildFailure(session,ResponseCode.Exception.PARAM_IS_EMPTY);
				}
				
				//通知
				rowCount = dataList.size();
				//执行处理工作
				List<Map<String,Object>> returnList=onAction(session,requestID,threadObject,dataList);

				//执行善后工作
				onSubmit(session, requestID,threadObject);

				if (returnList.size()>0) {
					
					setLastErrMessage("数据请求发生异常，详细见明细!");

					ServiceResponse response = new ServiceResponse();
				    Map<String,Object> dataMessage=new HashMap<String,Object>();
				    dataMessage.put("message", getLastErrMessage());
				    dataMessage.put("detailmessage", returnList);
				
				    response.setData(dataMessage);
				    response.setReturncode(ResponseCode.FAILURE);
				    return response;
				} 

				// 返回应答对象
				if (getResultData()!=null && getResultData().size()>0) {
					return ServiceResponse.buildSuccess(getResultData());
				}
				JSONObject response = new JSONObject();
				return ServiceResponse.buildSuccess(response);
			}
			@Override
			public long getRowCount() {
				return this.rowCount;
			}

		});
	}
	protected void onSubmit(ServiceSession session,Long requestID,BaseUserData userData) {};
	
	private ServiceResponse onAction(ServiceSession session, JSONObject params,
			String infoName, BaseUserData userData,
			BaseServiceCallback callback) {
		// 回调为空则直接返回异常
				if (callback == null) {
					return ServiceResponse.buildFailure(session,ResponseCode.Exception.PARAM_IS_EMPTY);
				}

				Long requestID=UniqueID.getUniqueID();

				// 回调通知唯一ID
//				String     callBackGuid    = "";
//				long       startTimeMillis = System.currentTimeMillis();
				long       rowCount        = 0l;
				boolean    successful      = true;
				JSONObject data            = null;
				
				try {
					// 执行日志开始处理
					data         = params;
					// 执行并返回结果
					ServiceResponse response = callback.onCallback(session, requestID, userData, data);
					rowCount = callback.getRowCount();
					return response;
				} catch(Exception e) {
					// 调试期间输出请求数据
					successful = false;
					this.setLastErrMessage(String.format("请求数据发生异常:%1$s --->%2$s", params+"",e.getMessage()));
					
					System.out.println(e.getMessage());
					e.printStackTrace();
				    ServiceResponse response = new ServiceResponse();
				    Map<String,Object> dataMessage=new HashMap<String,Object>();
				    dataMessage.put("message", e.getMessage());
				    dataMessage.put("detailmessage", new ArrayList<String>());
				
				    response.setData(dataMessage);
				    response.setReturncode(ResponseCode.FAILURE);
				    return response;
				}
	}
	
	
	/**
	 * 活动处理
	 * @param session
	 * @param dataList
	 * @return
	 */
	protected List<Map<String,Object>> onAction(ServiceSession session,Long requestID,BaseUserData userData,JSONArray dataList) {
		List<Map<String,Object>> response=new ArrayList<Map<String,Object>>();
		for (int i=0;i<dataList.size();i++) {
//			this.onRowTrace(session, requestID, dataList.get(i));
			
			boolean success=true;
			//T row=dataList.getObject(i, this.getBeanClass());
			try {
				T row=this.getObject(dataList, i, this.getBeanClass());
				if (row!=null) {
					row.setEntId(session.getEnt_id());
					row.setProcessed(false);
					success=this.onAction(session,requestID, userData, row);
					
					if (!success) {
						this.setLastErrMessage(row.getMessage());
					}
				} else { 
					success=false;
					this.setLastErrMessage("转换数据行为对象失败!");
				}
			} catch(RuntimeException re) {
				re.printStackTrace();
				success=false;
				this.setLastErrMessage(String.format("转换数据行为对象失败:%1$s",re.getMessage()));
			}
			
			if (!success) {
				@SuppressWarnings("unchecked")
				Map<String,Object> dataMap=this.getObject(dataList, i, Map.class);
				
				Map<String,Object> rowMap=this.getRowErrorMessage(i,dataMap);
				rowMap.put("message", this.getLastErrMessage());

				response.add(rowMap);
			}
		}
		return response;
	}
	
	protected abstract boolean onAction(ServiceSession session,Long requestID,BaseUserData userData,T bean);
	
	/**
	 * 处理SAP数值使用字符型传输，并且1位数字传输带空格解析的问题，如4传成了"4 "
	 * @param params
	 * @param classDefine
	 * @return
	 */
	@SuppressWarnings("hiding")
	public <T> T getObject(JSONArray dataList,int index, Class<T> clazz) {
        Object result = dataList.get(index);
        try {
        	return TypeUtils.castToJavaBean(result, clazz);
        } catch(JSONException je) {
        	System.out.println(je.getMessage()+"->"+JSON.toJSONString(result));
        	je.printStackTrace();
        	throw je;
        }
	}
	
	/**
	 * 入口效验
	 * @param session
	 * @param method
	 * @param bean
	 * @return
	 */
	protected abstract boolean onValid(ServiceSession session,Long requestID,BaseUserData userData,T bean);
	
	/**
	 * 获取行异常信息
	 * @param rowIndex
	 * @param dataMap
	 * @return
	 */
	protected Map<String,Object> getRowErrorMessage(int rowIndex,Map<String,Object> dataMap) {
		return this.getRowErrorMessage(null,rowIndex, dataMap);
	}
	
	/**
	 * 获取行异常信息
	 * @param classzz
	 * @param rowIndex
	 * @param dataMap
	 * @return
	 */
	protected Map<String,Object> getRowErrorMessage(Class<?> classzz,int rowIndex,Map<String,Object> dataMap) {
		Map<String,Object> rowMap=new HashMap<String,Object>();
		try {
			String[] uniquekeys = {"erpCode"};
			for(String key:uniquekeys) {
				String[] subKeys=key.split(",");
				for(String subKey:subKeys) {
					rowMap.put(subKey, dataMap.containsKey(subKey)?dataMap.get(subKey):"None");
				}
			}
		} catch (IllegalArgumentException e) {
			rowMap.put("key", String.format("第%1$d行", rowIndex));
		} 
		return rowMap;
	}
	
}
