package com.efuture.component.event.component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.easy.DataFrame;
import com.easy.DataFrame.IndexFunction;
import com.easy.DataFrame.JoinFunction;
import com.easy.DataFrame.TagFunction;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.util.MD5Util;
import com.product.util.MapAs;

public abstract class EventDrivenBaseComponent<T,X> {
	private SqlSessionTemplate template;
	private String collectionName;
	private Integer sleepRecvCount = 0;
	public Integer sleepActionCount = 0;

	private Class<T> modelClass;
	private Logger  logger;    //通用日志
	
	private Integer batchSize; // 一次性写入条数
	private String  insertCmd; // 插入指令
	private Function<T,String> classifier; // 分组函数
	
	static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	
	private TagFunction<Object> tagFunction;
	private IndexFunction<Object> idxFunction;
	
	abstract void onInitModel(T bean,Long batchseq);
	abstract Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException;
	abstract void onDataFrame(DataFrame<Object> df,String code);
	
	public ServiceResponse onTestModel(ServiceSession session, JSONObject params) {
		return ServiceResponse.buildSuccess("测试成功");
	}
    
	@SuppressWarnings("unchecked")
	public EventDrivenBaseComponent(SqlSessionTemplate template, String collectionName,String insertCmd,Integer batchSize,Function<T,String> classifier,String...keyFields) {
		this.template = template;
		this.collectionName = collectionName;
		this.insertCmd = insertCmd;
		this.batchSize = batchSize;
		this.classifier = classifier;
		
        ParameterizedType superclass = (ParameterizedType) this.getClass().getGenericSuperclass();
		Type[] actualTypeArguments = superclass.getActualTypeArguments();
		modelClass = (Class<T>) actualTypeArguments[0];
		logger = LoggerFactory.getLogger((Class<T>) actualTypeArguments[1]);
		
        // 标记方法
		this.tagFunction = new TagFunction<Object>() {
			@Override
			public void tagColumn(List<String> columns) {
				columns.add("md5");
			}

			@Override
			public void tagAction(List<String> columns, List<Object> values) {
				String md5Key = MD5Util.getInstance().md5(JSON.toJSONString(values));
				values.add(md5Key);
			}
	    };
		
	    // 索引方法
	    this.idxFunction = new IndexFunction<Object>() {
			@Override
			public Object indexAction(List<String> columns, List<Object> values) {
				List<String> columnIdx = Arrays.asList(keyFields);
				
				StringBuffer buffer = new StringBuffer();
				boolean first = true;
				for (String action:columnIdx) {
					int indexPos = columns.indexOf(action);
					if (indexPos>=0) {
						Object currentValue = values.get(indexPos);
						
						if (first) {
							first = false;
						} else {
							buffer.append("#");
						}
						buffer.append(TypeUtils.castToString(currentValue));
					} else {
						System.out.println("--->Not found field:"+action);
					}
				}
				return buffer.toString();
			}
	    };
	}
	
	public String getCollectionName() {
		return collectionName;
	}

	public SqlSessionTemplate getTemplate() {
		return template;
	}

	public Integer getBatchSize() {
		return batchSize;
	}

	public Logger getLogger() {
		return logger;
	}

	/**
	 * 获取序列号，如果被更新，则重新获取[没次多延迟一秒]
	 * @return
	 * @throws InterruptedException 
	 */
	public Long onAllotSeqno() throws InterruptedException {
		Map<String,Object> paramsMap = MapAs.of("value", String.format("SELECT eventSeqno FROM eventdriven WHERE eventCode='%1$s' ", collectionName));
		Map<String,Object> resultMap = template.selectOne("mybatis.sql.select", paramsMap);
		
		Long   currentSerial = TypeUtils.castToLong(resultMap.get("eventSeqno"));
		int affectedRows = template.update("mybatis.sql.update", String.format("Update eventdriven Set eventSeqno=eventSeqno+1 WHERE eventCode='%1$s' AND eventSeqno=%2$d", collectionName,currentSerial));
		if (affectedRows <1) {
			sleepRecvCount++;
			
			Thread.sleep(sleepRecvCount*1000);
			return this.onAllotSeqno();
		}
		// 获取完成则计数复位
		sleepRecvCount=0;
		return currentSerial+1;
	}
	
	public String getInsertCmd() {
		return insertCmd;
	}

	public Function<T, String> getClassifier() {
		return classifier;
	}

	/**
	 * 格式：
	 * 
	 * {"collectionname": [{...},...,{...}]}
	 * 
	 */
	public ServiceResponse receive(ServiceSession session, JSONObject params) throws Exception {
		String responseMsg = "";
		long start = System.currentTimeMillis();

		try {
			// 验证格式
			if (!params.containsKey(this.getCollectionName())) {
				return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST,"传入数据格式错误");
			}
			
			// 批量流水号
			final Long serialno;
			try {
				serialno = this.onAllotSeqno();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "获取流水号失败");
			}
	
			// 获取本次列表结果集
			List<T> dataList = JSON.parseArray(JSON.toJSONString(params.get(this.getCollectionName())),modelClass);		
			if (dataList == null || dataList.isEmpty()) {
				return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST,"传入数据空");
			}
	
			if (this.getClassifier() != null) {
				//数据根据门店进行分组
				Map<String,List<T>> groupMap = dataList.stream().collect(Collectors.groupingBy(getClassifier()));
				//---------- 根据Map的Key进行遍历生成门店数据start ----------
				for (String code : groupMap.keySet()) {
					List<T> itemList   = groupMap.get(code);
					List<T> insertList = new ArrayList<>();
					itemList.forEach(bean -> {
						onInitModel(bean,serialno);
						
						insertList.add(bean);
						if (insertList.size()+1>getBatchSize()) {
							this.getTemplate().insert(getInsertCmd(), insertList);
							insertList.clear();
						}
					});
					
					if (insertList.size()>0) {
						this.getTemplate().insert(getInsertCmd(), insertList);
					}
				}
			} else {
				List<T> insertList = new ArrayList<>();
				dataList.forEach(bean -> {
					onInitModel(bean,serialno);
					
					insertList.add(bean);
					if (insertList.size()+1>getBatchSize()) {
						this.getTemplate().insert(getInsertCmd(), insertList);
						insertList.clear();
					}
				});
				
				if (insertList.size()>0) {
					this.getTemplate().insert(getInsertCmd(), insertList);
				}
			}
		} finally {
			long end = System.currentTimeMillis();
			responseMsg = String.format("%1$s One request cost time is ：%2$d", this.getCollectionName(),(end-start));
			this.getLogger().info("{} One request cost time is ：{}",this.getCollectionName(),(end-start));
		}
		
		return ServiceResponse.buildSuccess(responseMsg);
	}
	
	protected void onAction(Connection connection,String sourceQuery,String targetQuery,String tag) throws Exception {
	    Statement  statement  = null;
		String responseMsg = "";

	    long  startTimeMillis = System.currentTimeMillis();
	    try {
		    statement  = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);

	    		long  sectionTimeMillis = System.currentTimeMillis();
	    		long  queryTimeMillis = sectionTimeMillis;
	    		long  joinTimeMillis = sectionTimeMillis;
	    		
		    try {
		    	
			    // 读取结果集
		    		ResultSet resultSet = statement.executeQuery(sourceQuery);
			    DataFrame<Object> df =  DataFrame.readSql(resultSet,tagFunction,idxFunction);
	        		resultSet.close();
				   
			    // 读取结果集
		        ResultSet changeSet = statement.executeQuery(targetQuery);
			    DataFrame<Object> dfChange =  DataFrame.readSql(changeSet,tagFunction,idxFunction);
			    changeSet.close();

		        queryTimeMillis = System.currentTimeMillis();

		        DataFrame<Object> dfcAll = dfChange.join(df, DataFrame.JoinType.LEFT,null,null,new JoinFunction<Object>() {
					@Override
					public boolean compare(List<Object> columns, List<Object> values) {
						int leftIndex  = columns.indexOf("md5_left");
						int rightIndex = columns.indexOf("md5_right");
						
						Object leftValue  = values.get(leftIndex);
						Object rightValue = values.get(rightIndex);
						
						if (leftValue == null || rightValue == null) {
							return true;
						}
						if (leftValue.equals(rightValue)) {
							return false;
						}
						return true;
					}
		        });
		        joinTimeMillis = System.currentTimeMillis();
		        
		        List<Object> columnsDrop = new ArrayList<>();
		        Map<Object,Object> columnsMap = new HashMap<>();
		        dfcAll.columns().forEach(action -> {
	        			String columnName = action.toString();
		        		if (columnName.endsWith("_right") || "md5_left".equals(columnName)) {
	        				columnsDrop.add(action);
		        		} else if (columnName.endsWith("_left")) {
	        				columnsMap.put(action, columnName.substring(0, columnName.length()-5));
		        		}
		        });
		        // 删除右边列即md5列
		        DataFrame<Object> dfResult =  dfcAll.drop(columnsDrop.toArray());
		        // 将左边列重命名
		        dfResult.rename(columnsMap);
		    
		        // 比较结果
		        this.onDataFrame(dfResult,tag);
		    } catch(Exception e) {
		    		System.out.println(e.getMessage());
		    		e.printStackTrace();
		    } finally {
		        long  endTimeMillis = System.currentTimeMillis();
		        long  elapsed = endTimeMillis - startTimeMillis;
		        
		        System.out.println(String.format("当前时间:%1$s 处理分片：%2$s 查询时间：%3$d 连接时间:%4$d 累计耗时时间:%5$d",formatter.format(new Date()), tag, (queryTimeMillis - sectionTimeMillis), (joinTimeMillis - queryTimeMillis) ,elapsed));
		    }
	        statement.close();
	        connection.close();
	    } catch(Exception e) {
	    		System.out.println(e.getMessage());
	    		e.printStackTrace();
	    } finally {
	        long  endTimeMillis = System.currentTimeMillis();
	        long  elapsed = endTimeMillis - startTimeMillis;
	        
	        responseMsg = String.format("当前时间:%1$s 累计耗时时间:%2$d",formatter.format(new Date()), elapsed);
	        System.out.println(responseMsg);
	    }
	}
}
