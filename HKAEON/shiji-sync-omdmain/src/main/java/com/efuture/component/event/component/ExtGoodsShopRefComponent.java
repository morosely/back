package com.efuture.component.event.component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.easy.DataFrame;
import com.efuture.model.ExtGoodsShopRefModel;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.util.MapAs;
import com.product.util.UniqueID;

public class ExtGoodsShopRefComponent extends EventDrivenBaseComponent<ExtGoodsShopRefModel,ExtGoodsShopRefComponent> {
	static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	Date date = new Date();

	public ExtGoodsShopRefComponent(SqlSessionTemplate template) {
		super(template, "extgoodsshopref","ExtGoodsShopRefModelMapper.insertAll",100,ExtGoodsShopRefModel::getShopCode,"entId", "erpCode","shopCode","goodsCode");
	}

	@Override
	void onInitModel(ExtGoodsShopRefModel bean,Long batchseq) {
		bean.setEgsrid(UniqueID.getUniqueID(true));//设置主键
	    	//t.setDealStatus(0);
		bean.setDealStatus(2);   //和以往方式处理不同。同步完后调用同步正式表接口：更新表中ID字段
		bean.setCreateDate(date);
		bean.setUpdateDate(bean.getCreateDate());
		bean.setBatchno(batchseq);
	}

	/**
	 * curl -X POST -H "Content-Type: text/plain" -d '{}' "http://localhost:8214/rest?method=sync.ExtGoodsShopRef.onTestModel&ent_id=0"
	 */
	@Override
	public ServiceResponse onTestModel(ServiceSession session, JSONObject params) {
		//		this.getTemplate().select("a", new ResultHandler() {
		//		@Override
		//		public void handleResult(ResultContext context) {
		//			Map<String,Object> resultObject = (Map<String, Object>)context.getResultObject();
		//			
		//		}
		//	});
	
		String responseMsg="";
		long start = System.currentTimeMillis();

		try {
			Map<String,Object> paramsMap = MapAs.of("value", "select distinct shopCode from extgoodsshopref where shopCode='023'");
			List<Map<String,Object>> listMap = this.getTemplate().selectList("mybatis.sql.select",paramsMap);
			long end = System.currentTimeMillis();
			System.out.println(String.format("%1$s group request cost time is ：%2$d", this.getCollectionName(),(end-start)));
	
			listMap.forEach(dataMap -> {
				String code = TypeUtils.castToString(dataMap.get("shopCode"));
				long startItem = System.currentTimeMillis();
				
				Map<String,Object> currentMap = MapAs.of("value", String.format("select * from extgoodsshopref where shopCode='%1$s'",code));
				List<Map<String,Object>> dataList = this.getTemplate().selectList("mybatis.sql.select",currentMap);

				long endItem = System.currentTimeMillis();
				System.out.println(String.format("%1$s -> %2$s group request cost time is ：%3$d", this.getCollectionName(),code,(endItem-startItem)));
				
				// 调用写入
				List<Map<String,Object>> insertList = new ArrayList<>();
				dataList.forEach(row -> {
					insertList.add(row);
					if (insertList.size()+1>5000) {
						Map<String,List<Map<String,Object>>> paramsValue = new HashMap<>();
						paramsValue.put("extgoodsshopref", insertList);
	
						JSONObject jsonValue = JSON.parseObject(JSON.toJSONString(paramsValue));
						try {
							this.receive(session, jsonValue);
						} catch(Exception e) {
							System.out.println(e.getMessage());
							e.printStackTrace();
							
							this.getLogger().debug(e.getMessage());
						}
						insertList.clear();
					}
				});
	
				if (insertList.size()>0) {
					Map<String,List<Map<String,Object>>> paramsValue = new HashMap<>();
					paramsValue.put("extgoodsshopref", insertList);
	
					JSONObject jsonValue = JSON.parseObject(JSON.toJSONString(paramsValue));
					try {
						this.receive(session, jsonValue);
					} catch(Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}
			});
		} finally {
			long end = System.currentTimeMillis();
			responseMsg = String.format("%1$s all request cost time is ：%2$d", this.getCollectionName(),(end-start));
		}

		return ServiceResponse.buildSuccess(responseMsg);
	}

	/**
	 * curl -X POST -H "Content-Type: text/plain" -d '{}' "http://localhost:8214/rest?method=sync.ExtGoodsShopRef.action&ent_id=0"
	 */
//	@Override
//	public ServiceResponse action(ServiceSession session, JSONObject params) throws Exception {
//		long  startTimeMillis = System.currentTimeMillis();
//		String responseMsg = "";
//
//		//Map<String,Object> paramsMap = MapAs.of("value", String.format("SELECT eventSeqno,actionSeqno, FROM eventdriven WHERE eventCode='%1$s' ", this.getCollectionName()));
//		//Map<String,Object> resultMap = this.getTemplate().selectOne("mybatis.sql.select", paramsMap);
//		
//		//final Long currentSerial = TypeUtils.castToLong(resultMap.get("actionSeqno"));
//		// Map<String,Object> grpParamsMap = MapAs.of("value", String.format("SELECT distinct shopCode FROM extgoodsshopref1 WHERE batchno='%1$d' ", currentSerial));
//		Map<String,Object> grpParamsMap = MapAs.of("value", "SELECT distinct shopCode FROM extgoodsshopref1 WHERE batchno >=0 AND batchno<10 and shopCode='023' and  goodscode in ('003063492','003063534')");
//		List<Map<String,Object>> groupList = this.getTemplate().selectList("mybatis.sql.select", grpParamsMap);
//		
//        // 标记方法
//    		TagFunction<Object> tagFunction=new TagFunction<Object>() {
//			@Override
//			public void tagColumn(List<String> columns) {
//				columns.add("md5");
//			}
//
//			@Override
//			public void tagAction(List<String> columns, List<Object> values) {
//				String md5Key = MD5Util.getInstance().md5(JSON.toJSONString(values));
//				values.add(md5Key);
//			}
//	    };
//
//	    
//	    Connection connection = null;
//	    Statement  statement  = null;
//
//	    //String sourceQuery = "SELECT entId,erpCode,shopId,shopCode,saleOrgId,orgCode,siid,stallCode,sgid,goodsCode,venderCode,goodStatus,cost,contractCost,costTaxRate,deductRate,salePrice,customPrice,bulkPrice,pcs,partsNum,stepDiff,safeStockDays,minStockDays,maxStockDays,priceRight,continuePurFlag,importantFlag,returnFlag,operateFlag,orderFlag,logistics,dcshopId,fdcShopId,inDate,inOper,offDate,lang,creator,createDate,modifier,updateDate,vid,prcutMode,goodsName FROM goodsshopref WHERE shopCode='%1$s'";
//	    //String targetQuery = "SELECT entId,erpCode,shopId,shopCode,saleOrgId,orgCode,siid,stallCode,sgid,goodsCode,venderCode,goodStatus,cost,contractCost,costTaxRate,deductRate,salePrice,customPrice,bulkPrice,pcs,partsNum,stepDiff,safeStockDays,minStockDays,maxStockDays,priceRight,continuePurFlag,importantFlag,returnFlag,operateFlag,orderFlag,logistics,dcshopId,fdcShopId,inDate,inOper,offDate,lang,creator,createDate,modifier,updateDate,vid,prcutMode,goodsName FROM extgoodsshopref1 WHERE batchno >=0 AND batchno<10 AND shopCode='%1$s'";
//	    String sourceQuery = "SELECT entId,erpCode,shopCode,stallCode,goodsCode,goodsName FROM goodsshopref WHERE shopCode='%1$s' and goodscode in ('003063492','003063534')";
//	    String targetQuery = "SELECT entId,erpCode,shopCode,stallCode,goodsCode,goodsName FROM extgoodsshopref1 WHERE batchno >=0 AND batchno<10 AND shopCode='%1$s'";
//	    
//	    try {
//			String jdbcDriver = "com.mysql.jdbc.Driver";
//			String jdbcURL = "jdbc:mysql://172.17.13.80:3306/omdmain-profit?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false";
//			String jdbcUsername = "root";
//			String jdbcPassword = "123456";
//	    	
//			//1. JDBC连接MYSQL的代码很标准。 
//		    	Class.forName(jdbcDriver).newInstance(); 
//		    connection = DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);
//	    	
//		    // connection = this.getTemplate().getConnection();
//		    statement  = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
//		    
//	        // 索引方法
//	    		IndexFunction<Object> idxFunction=new IndexFunction<Object>() {
//				@Override
//				public Object indexAction(List<String> columns, List<Object> values) {
//					List<String> columnIdx = Arrays.asList("entId", "erpCode","shopCode","goodsCode");
//					
//					StringBuffer buffer = new StringBuffer();
//					boolean first = true;
//					for (String action:columnIdx) {
//						int indexPos = columns.indexOf(action);
//						if (indexPos>=0) {
//							Object currentValue = values.get(indexPos);
//							
//							if (first) {
//								first = false;
//							} else {
//								buffer.append("#");
//							}
//							buffer.append(TypeUtils.castToString(currentValue));
//						}
//					}
//					return buffer.toString();
//				}
//		    };
//		    
//
//		    // 使用原始的jdbc提升处理性能
//		    for (Map<String,Object> dataMap:groupList) {
//		    		long  sectionTimeMillis = System.currentTimeMillis();
//		    		long  queryTimeMillis = sectionTimeMillis;
//		    		long  joinTimeMillis = sectionTimeMillis;
//		    		
//		    		final String code = TypeUtils.castToString(dataMap.get("shopCode"));
//			    try {
//			    	
//				    // 读取结果集
//			    		ResultSet resultSet = statement.executeQuery(String.format(sourceQuery, code));
//				    DataFrame<Object> df =  DataFrame.readSql(resultSet,tagFunction,idxFunction);
//		        		resultSet.close();
//					   
//				    // 读取结果集
//			        ResultSet changeSet = statement.executeQuery(String.format(targetQuery, code));
//				    DataFrame<Object> dfChange =  DataFrame.readSql(changeSet,tagFunction,idxFunction);
//				    changeSet.close();
//	
//			        queryTimeMillis = System.currentTimeMillis();
//	
//			        DataFrame<Object> dfcAll = dfChange.join(df, DataFrame.JoinType.INNER,null,null,new JoinFunction<Object>() {
//						@Override
//						public boolean compare(List<Object> columns, List<Object> values) {
//							int leftIndex  = columns.indexOf("md5_left");
//							int rightIndex = columns.indexOf("md5_right");
//							
//							Object leftValue  = values.get(leftIndex);
//							Object rightValue = values.get(rightIndex);
//							
//							if (leftValue == null || rightValue == null) {
//								return true;
//							}
//							if (leftValue.equals(rightValue)) {
//								return false;
//							}
//							return true;
//						}
//			        });
//			        joinTimeMillis = System.currentTimeMillis();
//			        
//			        List<Object> columnsDrop = new ArrayList<>();
//			        Map<Object,Object> columnsMap = new HashMap<>();
//			        dfcAll.columns().forEach(action -> {
//		        			String columnName = action.toString();
//			        		if (columnName.endsWith("_right") || "md5_left".equals(columnName)) {
//		        				columnsDrop.add(action);
//			        		} else if (columnName.endsWith("_left")) {
//		        				columnsMap.put(action, columnName.substring(0, columnName.length()-5));
//			        		}
//			        });
//			        // 删除右边列即md5列
//			        DataFrame<Object> dfResult =  dfcAll.drop(columnsDrop.toArray());
//			        // 将左边列重命名
//			        dfResult.rename(columnsMap);
//			        // 结果写入文件
//			        dfResult.writeCsv(String.format("out_%1$s.csv",code));
//			        
//			        // readCsv(final String file, final String separator, final NumberDefault numberDefault, final String naString, final boolean hasHeader,Class<?>...columnTypes)
//			        Class<?>[] columnTypes = dfResult.types().toArray(new Class<?>[dfResult.types().size()]);
//			        DataFrame<Object> dfResult2 = DataFrame.readCsv(String.format("out_%1$s.csv",code),",",DataFrame.NumberDefault.DOUBLE_DEFAULT,"",true,columnTypes);
//			        
//			        dfResult2.writeCsv(String.format("out_%1$s_1.csv",code));
//			    } catch(Exception e) {
//			    		System.out.println(e.getMessage());
//			    		e.printStackTrace();
//			    } finally {
//			        long  endTimeMillis = System.currentTimeMillis();
//			        long  elapsed = endTimeMillis - startTimeMillis;
//			        
//			        System.out.println(String.format("当前时间:%1$s 处理门店：%2$s 查询时间：%3$d 连接时间:%4$d 累计耗时时间:%5$d",formatter.format(new Date()), code, (queryTimeMillis - sectionTimeMillis), (joinTimeMillis - queryTimeMillis) ,elapsed));
//			    }
//			}
//	        statement.close();
//	        connection.close();
//	    } catch(Exception e) {
//	    		System.out.println(e.getMessage());
//	    		e.printStackTrace();
//	    } finally {
//	        long  endTimeMillis = System.currentTimeMillis();
//	        long  elapsed = endTimeMillis - startTimeMillis;
//	        
//	        responseMsg = String.format("当前时间:%1$s 累计耗时时间:%2$d",formatter.format(new Date()), elapsed);
//	        System.out.println(responseMsg);
//	    }
//        return ServiceResponse.buildSuccess(responseMsg);
//	}

	@Override
	Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String jdbcDriver = "com.mysql.jdbc.Driver";
		String jdbcURL = "jdbc:mysql://172.17.13.80:3306/omdmain-profit?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false";
		String jdbcUsername = "root";
		String jdbcPassword = "123456";
    	
		//1. JDBC连接MYSQL的代码很标准。 
	    	Class.forName(jdbcDriver).newInstance();
	    	return DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);
	}

	@Override
	void onDataFrame(DataFrame<Object> df,String code) {
        System.out.println();
        // 结果写入文件
        try {
        		df.writeCsv(String.format("out_%1$s.csv",code));
        } catch(Exception e) {
        		e.printStackTrace();
        }

//        // readCsv(final String file, final String separator, final NumberDefault numberDefault, final String naString, final boolean hasHeader,Class<?>...columnTypes)
//        Class<?>[] columnTypes = df.types().toArray(new Class<?>[df.types().size()]);
//        DataFrame<Object> dfResult2 = DataFrame.readCsv(String.format("out_%1$s.csv",code),",",DataFrame.NumberDefault.DOUBLE_DEFAULT,"",true,columnTypes);
//        
//        dfResult2.writeCsv(String.format("out_%1$s_1.csv",code));
	}
	
	/**
	 * curl -X POST -H "Content-Type: text/plain" -d '{"startId":1,"endId":5}' "http://localhost:8214/rest?method=sync.ExtGoodsShopRef.action&ent_id=0"
	 */
	public ServiceResponse action(ServiceSession session, JSONObject params) throws Exception {
		//Map<String,Object> paramsMap = MapAs.of("value", String.format("SELECT eventSeqno,actionSeqno, FROM eventdriven WHERE eventCode='%1$s' ", this.getCollectionName()));
		//Map<String,Object> resultMap = this.getTemplate().selectOne("mybatis.sql.select", paramsMap);
		//final Long currentSerial = TypeUtils.castToLong(resultMap.get("actionSeqno"));
		// Map<String,Object> grpParamsMap = MapAs.of("value", String.format("SELECT distinct shopCode FROM extgoodsshopref1 WHERE batchno='%1$d' ", currentSerial));
	    //String sourceQuery = "SELECT entId,erpCode,shopCode,stallCode,goodsCode,goodsName FROM goodsshopref WHERE shopCode='%1$s' and goodscode in ('003063492','003063534')";
	    //String targetQuery = "SELECT entId,erpCode,shopCode,stallCode,goodsCode,goodsName FROM extgoodsshopref1 WHERE batchno >=0 AND batchno<10 AND shopCode='%1$s'";
	    // connection = this.getTemplate().getConnection();

		long  startTimeMillis = System.currentTimeMillis();
		String responseMsg = "";

		int startId = params.getIntValue("startId");
		int endId   = params.getIntValue("endId");

		// 
		String rangeQuery  = "SELECT shopCode,count(*) as tcount FROM extgoodsshopref1 WHERE batchno>=%1$d AND batchno<%2$d+1 AND goodscode in ('003063492','003063534') group by shopCode";
		String fields      = "entId,erpCode,shopId,shopCode,saleOrgId,orgCode,siid,stallCode,sgid,goodsCode,venderCode,goodStatus,cost,contractCost,costTaxRate,deductRate,salePrice,customPrice,bulkPrice,pcs,partsNum,stepDiff,safeStockDays,minStockDays,maxStockDays,priceRight,continuePurFlag,importantFlag,returnFlag,operateFlag,orderFlag,logistics,dcshopId,fdcShopId,inDate,inOper,offDate,lang,creator,createDate,modifier,updateDate,vid,prcutMode,goodsName";
	    String sourceQuery = "SELECT %1$s FROM extgoodsshopref  WHERE shopCode='%2$s' AND goodscode in ('003063492','003063534')"; // goodsshopref
	    String targetQuery = "SELECT %1$s FROM extgoodsshopref1 WHERE batchno>=%2$d AND batchno<%3$d+1 AND shopCode='%4$s' AND goodscode in ('003063492','003063534')";
		
		Connection connection=null;
		
		try {
			connection = getConnection();
			Map<String,Object> grpParamsMap = MapAs.of("value", String.format(rangeQuery,startId,endId));
			List<Map<String,Object>> groupList = this.getTemplate().selectList("mybatis.sql.select", grpParamsMap);
		    // 使用原始的jdbc提升处理性能
		    for (Map<String,Object> dataMap:groupList) {
				String code   = TypeUtils.castToString(dataMap.get("shopCode"));
				String sQuery = String.format(sourceQuery,fields,code);
				String tQuery = String.format(targetQuery,fields,startId,endId,code);
				this.onAction(connection,sQuery,tQuery,code);
		    }
		} catch(Exception e) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, String.format("action:%1$s", e.getMessage()));
		} finally {
	        long  endTimeMillis = System.currentTimeMillis();
	        long  elapsed = endTimeMillis - startTimeMillis;
	        
	        responseMsg = String.format("当前时间:%1$s 累计耗时时间:%2$d",formatter.format(new Date()), elapsed);
	        System.out.println(responseMsg);
			
			connection.close();
		}
		
		return ServiceResponse.buildSuccess(responseMsg);
	}
}
