package com.efuture.omdmain.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

/**
 * 高级查询字段支持前台传参控制，也支持后台直接定义
 * @author yihaitao
 *
 */
public class QueryAdvanceField extends JDBCCompomentServiceImpl<Object>{

	public QueryAdvanceField(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return null;
	}

//	@Override
//	protected FMybatisTemplate getTemplate() {
//		return this.getBean("StorageOperation", FMybatisTemplate.class);	
//	}
	
	//高级查询动态加载表的字段和注释
	public List<Map<String,String>> queryAdvanceField(ServiceSession session, JSONObject paramsObject) throws Exception {
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        return template.getSqlSessionTemplate().selectList("beanmapper.AdvancedQueryMapper.advancedQuery",paramsObject);
	}
	
	/**
	 * 高级查询显示的字段（如果数据库描述和页面描述不一致，可以在查询出来的map进行key-value覆盖 ）
	 * (前端调用示例代码)
	 * {
	 *	  "fields": [//fields非必填项。自定义显示哪些字段。不传次参数显示表的所有字段
	 *	    "goodsId",
	 *	    "goodsCode",
	 *	    "goodsName"
	 *	  ],
	 *	  "table": "goods",//table参数必填项
	 *	  "schema": "omdmain"//schema参数必填项
	 *	}
	 */
	public ServiceResponse showAdvanceField(ServiceSession session, JSONObject paramsObject) throws Exception {
        List<Map<String,String>> showList = this.queryAdvanceField(session, paramsObject);;
        ServiceResponse serviceResponse = ServiceResponse.buildSuccess(showList);
		return serviceResponse;
	}
	
	//高级查询显示的字段(后端调用示例代码)
	public ServiceResponse backAdvanceField(ServiceSession session, JSONObject paramsObject) throws Exception {
		paramsObject.put("table","goods");
		paramsObject.put("schema","omdmain");
		Object[] fields = {"goodsId","goodsCode","goodsName"};
		paramsObject.put("fields",new JSONArray(Arrays.asList(fields)));
		return this.showAdvanceField(session, paramsObject);
	}

}
