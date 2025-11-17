package com.efuture.omdmain.component;

import java.util.Arrays;
import java.util.List;

import com.product.component.CommonServiceImpl;
import org.springframework.data.mongodb.core.query.Query;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.AeonJDTokenModel;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class AeonJDTokenServiceImpl extends CommonServiceImpl<AeonJDTokenModel, AeonJDTokenServiceImpl> {

	public AeonJDTokenServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
		super(mybatisTemplate, collectionName, keyfieldName);
	}

	public ServiceResponse queryAll(ServiceSession session, JSONObject paramsObject) throws Exception{
		Query query = new Query();
	    List list = this.getTemplate().select(query, AeonJDTokenModel.class, "aeon_jdtoken");
	    return ServiceResponse.buildSuccess(list);
	}
	
	//增加或者更新
	public ServiceResponse saveOrUpdate(ServiceSession session, JSONObject paramsObject) throws Exception{
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"aeon_jdtoken");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		//前端只会传一条数据
		JSONArray array = paramsObject.getJSONArray("aeon_jdtoken");
		JSONObject data = (JSONObject)array.get(0);
		String itemCode1 = data.getString("itemCode1");//销售游戏币编码
		String flag = paramsObject.getString("flag");//新增或者修改标识"U","I"
		if("I".equals(flag)) {//新增时做查询校验
			JSONObject selectParam = new JSONObject();
			selectParam.put("itemCode1",itemCode1);
			AeonJDTokenModel model = wrapQueryBean(session,selectParam);
			if(model == null) {
				//return this.onInsert(session, paramsObject);
				this.getTemplate().getSqlSessionTemplate().insert("beanmapper.AeonJDTokenModelMapper.insert",data);
				return ServiceResponse.buildSuccess(Arrays.asList(itemCode1));
			}else{
				return ServiceResponse.buildFailure(session, ResponseCode.Failure.ALREADY_EXISTS, "銷售遊戲幣編碼有重複:"+itemCode1);
			}
		}else if("U".equals(flag)) {//更新操作
			//return this.onUpdate(session, paramsObject);
			this.getTemplate().getSqlSessionTemplate().insert("beanmapper.AeonJDTokenModelMapper.update",data);
			return ServiceResponse.buildSuccess(Arrays.asList(itemCode1));
		}else {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PRIMARY_IS_ERROR, "參數錯誤缺少新增修改標識flag:"+flag);
		}
	}
	
	//分页查询
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception{
		return this.onQuery(session, paramsObject);
	}
	
	//删除
	public ServiceResponse deleteCodes(ServiceSession session, JSONObject paramsObject) throws Exception{
		paramsObject.put("table","aeon_jdtoken");
		paramsObject.put("key","itemCode1");
		paramsObject.put("values",paramsObject.getJSONArray("itemCode1"));
		Integer deleteCount = this.getTemplate().getSqlSessionTemplate().delete("beanmapper.AdvancedQueryMapper.deleteModel",paramsObject);
		return ServiceResponse.buildSuccess(deleteCount);
	}
}
