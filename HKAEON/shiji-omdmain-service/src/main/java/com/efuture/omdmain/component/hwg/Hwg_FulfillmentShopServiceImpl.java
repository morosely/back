package com.efuture.omdmain.component.hwg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.hwg.Hwg_FulfillmentShopModel;
import com.efuture.omdmain.service.hwg.Hwg_FulfillmentShopService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.exception.ServiceRuntimeException;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.ParamValidateUtil;
import com.product.util.UniqueID;

public class Hwg_FulfillmentShopServiceImpl extends JDBCCompomentServiceImpl<Hwg_FulfillmentShopModel>
		implements Hwg_FulfillmentShopService {
	public Hwg_FulfillmentShopServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName,
			String keyfieldName) {
		super(mybatisTemplate, collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	private static final Logger logger = LoggerFactory.getLogger(Hwg_FulfillmentShopServiceImpl.class);

//	@Resource
//	ConfigurableEnvironment environment;

	// private static final int OFCSTATE = 6;

	/**
	 * 保存
	 * 
	 * @param session
	 * @param paramsObject {}
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ServiceResponse save(ServiceSession session, JSONObject paramsObject) throws Exception {
		ParamValidateUtil.paramCheck(session, paramsObject, "erpCode", "shopList");
		JSONArray shopList = paramsObject.getJSONArray("shopList");
		// 1. 商品在海外购平台收卖则不能取消 (与蔡师傅确认,暂不进行此判断)

		// 2. 有需要履约或者在履约的订单 ，该门店就不许取消 ,只有海外购门店有订单的履约门店数据,并且不是完成状态的 (与蔡师傅确认,暂不进行此判断)
//		if (!shopList.isEmpty()) {
//			String url = (String) environment.getProperty("fulfillment.search");
//			JSONObject shop = new JSONObject();
//			shop.put("ofcState", OFCSTATE);
//			for (int i = 0; i < shopList.size(); i++) {
//				shop.put("ofcMarketCode", shopList.getJSONObject(i).getString("shopCode"));
//
//				JSONArray jsonArray = HttpUtils.doPost(url, session, shop.toJSONString(), "fulfillment");
//
//				if (null != jsonArray && !jsonArray.isEmpty()) {
//					throw new ServiceRuntimeException(
//							String.format("门店号:%s中存在正在履约中订单,取消失败!", shop.getString("ofcMarketCode")));
//				}
//			}
//		}

		// 需求
		// 1、需要新增表
		// 2、取消的门店打标记，而不是删除。
		// template.getSqlSessionTemplate().update("beanmapper.hwg.Hwg_FulfillmentShopModelMapper.updateAll");

		// 判断是否为初始化设置
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);

		long count = template.getSqlSessionTemplate()
				.selectOne("beanmapper.hwg.Hwg_FulfillmentShopModelMapper.searchCount", paramsObject);
		if (0 == count) {
			for (int i = 0; i < shopList.size(); i++) {
				JSONObject insert = shopList.getJSONObject(i);
				this.insert(session, insert, paramsObject);
			}
		} else {
			ArrayList<String> shopCodeList = new ArrayList<>();
			StringBuffer buff = new StringBuffer();
			buff.append("(");
			for (int i = 0; i < shopList.size(); i++) {
				JSONObject shopJson = shopList.getJSONObject(i);
				shopCodeList.add((shopJson.getString("shopCode") + ":" + shopJson.getString("shopId")));
				buff.append("(\'");
				buff.append(shopJson.getString("shopCode"));
				buff.append("\',\'");
				buff.append(shopJson.getString("shopId"));
				if (i != shopList.size() - 1) {
					buff.append("\'),");
				} else {
					buff.append("\')");
				}
			}
			buff.append(")");
			paramsObject.put("params", buff.toString());

			paramsObject.put("modifier", session.getUser_name());
			paramsObject.put("status", 1);
			paramsObject.put("updateDate", new Date());
			template.getSqlSessionTemplate().update("beanmapper.hwg.Hwg_FulfillmentShopModelMapper.update",
					paramsObject);

			paramsObject.put("status", 0);
			template.getSqlSessionTemplate().update("beanmapper.hwg.Hwg_FulfillmentShopModelMapper.updateNotIn",
					paramsObject);

			List<JSONObject> search = template.getSqlSessionTemplate()
					.selectList("beanmapper.hwg.Hwg_FulfillmentShopModelMapper.search", paramsObject);

			if (null != search && !search.isEmpty()) {
				ArrayList<String> temp = new ArrayList<>();
				for (JSONObject jsonObject : search) {
					temp.add((jsonObject.getString("shopCode") + ":" + jsonObject.getString("shopId")));
				}

				shopCodeList.removeAll(temp);

				JSONObject insert = new JSONObject();
				for (String string : shopCodeList) {
					String[] strArr = string.split(":");
					insert.put("shopCode", strArr[0]);
					insert.put("shopId", strArr[1]);
					this.insert(session, insert, paramsObject);
				}
			}
		}

		return ServiceResponse.buildSuccess(ResponseCode.SUCCESS);
	}

	private void insert(ServiceSession session, JSONObject insert, JSONObject paramsObject) throws Exception {
		insert.put("hfsid", UniqueID.getUniqueID());
		insert.put("erpCode", paramsObject.getString("erpCode"));
		insert.put("status", 1);
		insert.put("creator", session.getUser_name());
		insert.put("createDate", new Date());
		insert.put("modifier", session.getUser_name());
		insert.put("updateDate", new Date());

		ServiceResponse onInsert = this.onInsert(session, insert);
		if (!ResponseCode.SUCCESS.equals(onInsert.getReturncode())) {
			logger.error(String.format("参数----->:%1$s,新增失败,错误信息----->%2$s", insert.toJSONString(),
					onInsert.getData().toString()));
			throw new ServiceRuntimeException("修改失败!");
		}
	}

	/**
	 * 查询
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		ParamValidateUtil.paramCheck(session, paramsObject, "erpCode");
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		List<JSONObject> selectList = template.getSqlSessionTemplate()
				.selectList("beanmapper.hwg.Hwg_FulfillmentShopModelMapper.searchList", paramsObject);
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), selectList);
		return ServiceResponse.buildSuccess(result);
	}

}
