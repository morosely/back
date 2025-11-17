package com.efuture.omdmain.component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.GoodsProcessModel;
import com.efuture.omdmain.service.GoodsProcessService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

/**
 * 档口加工方法定义Service
 * 
 * @author chenp
 *
 */
public class GoodsProcessServiceImpl extends JDBCCompomentServiceImpl<GoodsProcessModel> implements GoodsProcessService {

	private Logger logger = LoggerFactory.getLogger(GoodsProcessServiceImpl.class);

	public GoodsProcessServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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

	@Autowired
	private GoodsServiceImpl goodsServiceImpl;

	// 查询商品基础表
	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {

		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);

		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);

		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		// 设置默认分页参数
		if (!paramsObject.containsKey("page_no")) {
			paramsObject.put("page_no", 0);
		} else {
			paramsObject.put("page_no",
					(paramsObject.getInteger("page_no") - 1) * paramsObject.getInteger("page_size"));
		}
		if (!paramsObject.containsKey("page_size")) {
			paramsObject.put("page_size", 10);
		}

		// categoryId传空数组校验
		if (paramsObject.containsKey("categoryId")) {
			Object values = paramsObject.get("categoryId");
			if (values instanceof List) {
				// 如果categoryId为空数组，则为查询全部类别
				if (((List) values).isEmpty()) {
					paramsObject.remove("categoryId");
				}
			}
		}

		List salegoodsList = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.getGoodsList",
				paramsObject);
		long total_results = 0;
		if (salegoodsList != null && salegoodsList.size() > 0) {
			total_results = template.getSqlSessionTemplate().selectOne("beanmapper.GoodsModelMapper.countGoodsList",
					paramsObject);
		}
		JSONObject result = new JSONObject();
		result.put("goods", salegoodsList);
		result.put("total_results", total_results);
		return ServiceResponse.buildSuccess(result);
	}

	// 根据商品sgid查询档口加工方法
	public ServiceResponse getDataBySgid(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onQuery(session, paramsObject);
	}

	// 单个删除
	public ServiceResponse delete(ServiceSession session, JSONObject paramsObject) {
		return this.onDelete(session, paramsObject);
	}

	// 新增
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception {

		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (StringUtils.isEmpty(paramsObject)) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		}

		ServiceResponse serviceResponse = null;
		this.setUpsert(false);

		try {
			BigDecimal processFee = paramsObject.getBigDecimal("processFee").setScale(2, BigDecimal.ROUND_DOWN);
			BigDecimal minProcessFee = paramsObject.getBigDecimal("minProcessFee").setScale(2, BigDecimal.ROUND_DOWN);
			
			if (processFee.compareTo(minProcessFee) < 0) {
				return serviceResponse.buildFailure(session, ResponseCode.FAILURE, "最低加工费不能超过加工费");
			}
			paramsObject.put("processFee", processFee);
			paramsObject.put("minProcessFee", minProcessFee);
			paramsObject.put("createDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			paramsObject.put("creator", session.getUser_name());
		} catch (Exception e) {
			return serviceResponse.buildFailure(session, ResponseCode.EXCEPTION, "所填加工费或最低加工费必须是数字");
		}

		return this.onInsert(session, paramsObject);
	}

	// 编辑(批量)
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {

		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (StringUtils.isEmpty(paramsObject)) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		}

		ServiceResponse serviceResponse = null;

		try {
			this.setUpsert(false);
			JSONArray params = paramsObject.getJSONArray("goodsprocess");
			Iterator<Object> arrays = params.iterator();
			while (arrays.hasNext()) {
				JSONObject obj = (JSONObject) arrays.next();
				BigDecimal processFee = obj.getBigDecimal("processFee").setScale(2, BigDecimal.ROUND_DOWN);
				BigDecimal minProcessFee = obj.getBigDecimal("minProcessFee").setScale(2, BigDecimal.ROUND_DOWN);
				if (processFee.compareTo(minProcessFee) < 0) {
					return serviceResponse.buildFailure(session, ResponseCode.FAILURE, "最低加工费不能超过加工费");
				}
				obj.put("processFee", processFee);
				obj.put("minProcessFee", minProcessFee);
				obj.put("modifier", session.getUser_name());
				obj.put("updateDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}
			JSONObject paramMap = new JSONObject();
			paramMap.put("goodsprocess", params);
			return this.onUpdate(session, paramMap);

		} catch (Exception e) {
			logger.error(e.getMessage());
			return serviceResponse.buildFailure(session, ResponseCode.EXCEPTION, "所填加工费或最低加工费必须是数字" + e.getMessage());
		}
	}

	// 导出 后期补充

}
