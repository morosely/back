package com.efuture.omdmain.component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.AeonTransLogModel;
import com.product.component.CommonServiceImpl;
import com.product.model.BeanConstant;
import com.product.model.RowMap;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class AeonTransLogServiceImpl extends CommonServiceImpl<AeonTransLogModel, AeonTransLogServiceImpl>{

	public AeonTransLogServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
		super(mybatisTemplate, collectionName, keyfieldName);
	}

	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		String date = paramsObject.getString("rj_date");
		String shopCode = paramsObject.getString("shop");
		if(StringUtils.isEmpty(date)){
			JSONObject dateJson = new JSONObject();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateJson.put(">=",java.sql.Date.valueOf(simpleDateFormat.format(new java.util.Date())));
			paramsObject.put("rj_date", dateJson);
		}
		if(!StringUtils.isEmpty(shopCode)){
			JSONObject shopJson = new JSONObject();
			shopJson.put("in",Arrays.asList(shopCode,"txt"));
			paramsObject.put("shop",shopJson);
		}
		ServiceResponse response = this.onQuery(session, paramsObject);
		Map<String,List<RowMap>> map = (Map<String,List<RowMap>>)response.getData();
		List<RowMap> rowMapList = map.get(this.getCollectionName());
		List<AeonTransLogModel> list = JSON.parseArray(JSON.toJSONString(rowMapList), AeonTransLogModel.class);
		paramsObject.clear();
		paramsObject.put(this.getCollectionName(),list);
		paramsObject.put("total_results",map.get("total_results"));
		return ServiceResponse.buildSuccess(paramsObject);
//		JSONObject obj1 = (JSONObject) paramsObject.get("rj_date");
//		String start = (String) obj1.get("rj_dateStart");
//		String end = (String) obj1.get("rj_dateEnd");
//		if (!"".equals(start) && !"".equals(end)) {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = sdf.parse(start);
//			Date date2 = sdf.parse(end);
//			if (date2.getTime() - date.getTime() < 0) {
//				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "日结日期结束时间必须大于日结日期开始时间!");
//			}
//		}
//
//		FMybatisTemplate template = this.getTemplate();
//		template.onSetContext(session);
//		this.parsePage(paramsObject);
//		// 查询当前页
//		List<Object> list = template.getSqlSessionTemplate().selectList("beanmapper.AeonTransLogModelMapper.get",
//				paramsObject);
//		// 查询总条数
//		List<Object> count = template.getSqlSessionTemplate()
//				.selectList("beanmapper.AeonTransLogModelMapper.getCount", paramsObject);
//
//		JSONObject js = new JSONObject();
//		js.put(getCollectionName(), list);
//		js.put("total_results", count.get(0));
//		return ServiceResponse.buildSuccess(js);
	}
	
	public void parsePage(JSONObject paramsObject) {
		final int defLimit = 0;
		final int defOffset = 10;
		if (paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_PAGENO)
				&& paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_PAGESIZE)) {
			int page_no = paramsObject.getIntValue(BeanConstant.QueryField.PARAMKEY_PAGENO);
			int page_size = paramsObject.getIntValue(BeanConstant.QueryField.PARAMKEY_PAGESIZE);
			int limit = page_no <= 0 ? defLimit : (page_no - 1) * page_size;
			int offset = page_size <= 0 ? defOffset : page_size;
			paramsObject.put("limit", limit);
			paramsObject.put("offset", offset);
		} else {
			paramsObject.put("limit", defLimit);
			paramsObject.put("offset", defOffset);
		}
	}
}
