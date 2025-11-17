package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.ChannelInfoModel;
import com.efuture.omdmain.model.ShopChannelRefModel;
import com.efuture.omdmain.service.ChannelInfoService;
import com.mongodb.DBObject;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.BeanConstant;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.ParamValidateUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChannelInfoServiceImpl extends CommonServiceImpl<ChannelInfoModel,ChannelInfoServiceImpl> implements ChannelInfoService {

	public ChannelInfoServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onQuery(session, paramsObject);
	}
	
	@Override 
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {
		this.setUpsert(false);
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"channelId","status");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		//1.查询(通过ID查询渠道信息)
		Criteria criteria = Criteria.where("channelId").is(paramsObject.getLong("channelId"));
		Query query = new Query(criteria);

		ChannelInfoModel oldModel = this.getTemplate().selectOne(query, ChannelInfoModel.class,"channelInfo");
		if(oldModel == null) return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST, "查询的渠道不存在（通过渠道ID查询）");

		String posmode = paramsObject.getString("posmode");
		if (posmode != null && !"".equals(posmode)){
			Query q = new Query().addCriteria(Criteria.where("channelId").ne(paramsObject.getLong("channelId")));
			List<ChannelInfoModel> channelinfoList = this.getTemplate().select(q, ChannelInfoModel.class, "channelinfo");
			boolean isExist = isExistPosMode(channelinfoList, posmode);
			if (isExist){
				return ServiceResponse.buildFailure(session, ResponseCode.Failure.ALREADY_EXISTS,"此收银类型已存在");
			}
		}

		//只处理线上渠道
		if(1 == oldModel.getChannelType()){
			Integer oldStatus = oldModel.getStatus();
			Integer newStatus = paramsObject.getInteger("status");
			Integer compare = Integer.compare(newStatus,oldStatus);
			paramsObject.put("updateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			//表明状态有更改（0->1 无效改成有效）
			if(compare == 1){
				//更新门店渠道关联shopchannelref
				paramsObject.put("table","shopchannelref");
				paramsObject.put("setField","status");
				paramsObject.put("setFieldValue",newStatus);
				paramsObject.put("key","channelId");
				paramsObject.put("values",Arrays.asList(paramsObject.getLong("channelId")));
				this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateModel",paramsObject);
				
				List<ShopChannelRefModel> list = this.getTemplate().select(query, ShopChannelRefModel.class, "shopchannelref");
				if(list!=null && !list.isEmpty()){
					List<Long> shopIds = list.stream().map(ShopChannelRefModel::getShopId).collect(Collectors.toList());
					paramsObject.put("shopIds", shopIds);
					paramsObject.put("isValid", newStatus);
					this.getTemplate().getSqlSessionTemplate().update("beanmapper.GoodsUpAndDownModelMapper.updateStatus",paramsObject);
				}
			}
			//表明状态有更改（1->0 有效改成无效）
			if(compare == -1){
				//更新门店渠道关联shopchannelref
				paramsObject.put("table","shopchannelref");
				paramsObject.put("setField","status");
				paramsObject.put("setFieldValue",newStatus);
				paramsObject.put("key","channelId");
				paramsObject.put("values",Arrays.asList(paramsObject.getLong("channelId")));
				this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateModel",paramsObject);
				
				paramsObject.put("table","goodsupanddown");
				paramsObject.put("setField","isValid");
				paramsObject.put("setFieldValue",newStatus);
				paramsObject.put("key","channelId");
				paramsObject.put("values",Arrays.asList(paramsObject.getLong("channelId")));
				this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateModel",paramsObject);
			}
		}

		int updateCount = this.getTemplate().getSqlSessionTemplate().update("beanmapper.ChannelInfoModelMapper.updateModel", paramsObject);
		if (updateCount > 0){
			JSONObject resp = new JSONObject();
			resp.put("channelId",paramsObject.getLong("channelId"));
			return ServiceResponse.buildSuccess(resp);
		}
		return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST,"更新的渠道不存在");
	}
	
	@Override
	public  ServiceResponse add(ServiceSession session, JSONObject paramsObject) {
		String posmode = paramsObject.getString("posmode");
		//如果有对应的posmode入参,则需要查询所有渠道中已存在的posmode去过滤
		if (posmode != null && !"".equals(posmode)){
			List<ChannelInfoModel> data = this.getTemplate().select(new Query(), ChannelInfoModel.class, "channelinfo");
			boolean isExist = isExistPosMode(data, posmode);
			if (isExist){
				return ServiceResponse.buildFailure(session, ResponseCode.Failure.ALREADY_EXISTS,"此收银机类型已存在");
			}
		}
		return this.onInsert(session, paramsObject);
	}


	/**
	 * 遍历渠道中是否已有该posmode存在
	 * @param channelinfoList
	 * @param posmode
	 * @return
	 */
	private boolean isExistPosMode(List<ChannelInfoModel> channelinfoList , String posmode){
		for (ChannelInfoModel model : channelinfoList) {
			boolean existPosMode = isExist(model, posmode);
			if (existPosMode){
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断该渠道是否包含该收银机类型
	 * @param model
	 * @param posmode
	 * @return
	 */
	private boolean isExist(ChannelInfoModel model,String posmode){
		String mode = model.getPosmode();
		if (mode != null && !"".equals(mode)){
			String[] split = mode.split(",");
			for (String s : split) {
				if (posmode.contains(s)){
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * 根据传入的posmode返回对应的channelId
	 * @param session
	 * @param paramsObject
	 * @return
	 * @throws Exception
	 */
	@Override
	public ServiceResponse searchByPosMode(ServiceSession session, JSONObject paramsObject) throws Exception {
		String posmode = paramsObject.getString("posmode");
		Query query = new Query();
		query.fields().include("channelCode");
		query.fields().include("channelName");
		query.fields().include("posmode");
		if (posmode==null || posmode.equals("")){
			List<ChannelInfoModel> channelInfo = this.getTemplate().select(query, ChannelInfoModel.class, "channelInfo");
			JSONObject resp = new JSONObject();
			resp.put("channelInfo",channelInfo);
			resp.put("total_results",channelInfo.size());
			return ServiceResponse.buildSuccess(resp);
		}
		List<ChannelInfoModel> data = this.getTemplate().select(query, ChannelInfoModel.class, "channelinfo");
		List<ChannelInfoModel> list = new ArrayList<>();
		for (ChannelInfoModel model : data) {
			boolean isExist = isExist(model, posmode);
			if (isExist){
				list.add(model);
			}
		}
		JSONObject resp = new JSONObject();
		resp.put("channelInfo",list);
		resp.put("total_results",list.size());
		return ServiceResponse.buildSuccess(resp);
	}


	/**
	 * 查询全部渠道--库存中心--渠道库存商品查询专用
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public  ServiceResponse getAllChannels(ServiceSession session, JSONObject paramsObject) {
		
		// 参数校验
		ParamValidateUtil.paramCheck(session, paramsObject);
		List<ChannelInfoModel> channelList = new ArrayList<>();
		// 初始化特殊的渠道--门店
		ChannelInfoModel model = new ChannelInfoModel();
		model.setEntId(session.getEnt_id());   // 零售商ID
		model.setChannelCode("-1");            // 特殊---渠道编码--门店
		model.setChannelName("门店");            // 特殊--渠道名称
		model.setStatus(1);                    //  特殊--状态
		channelList.add(model);
		
		// 查询全部渠道信息
		Criteria criteria = Criteria.where("entId").is(session.getEnt_id());
		Query query = new Query(criteria);
		Field fields = query.fields();
		fields.include("channelId,entId,channelCode,channelName,status");
		List<ChannelInfoModel> MDMchannelList = this.getTemplate().select(query, ChannelInfoModel.class, "channelInfo");
		if (MDMchannelList != null && MDMchannelList.size() > 0) {
			channelList.addAll(MDMchannelList);
		}
		
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), channelList);
		result.put("total_results", channelList.size());
		return ServiceResponse.buildSuccess(result);
	}

	/**
	 * 查询主数据全部渠道
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public  ServiceResponse getList(ServiceSession session, JSONObject paramsObject) {
		
		// 参数校验
		ParamValidateUtil.paramCheck(session, paramsObject);
		// 查询全部渠道信息
		Criteria criteria = Criteria.where("entId").is(session.getEnt_id());
		Query query = new Query(criteria);
		Field fields = query.fields();
		fields.include("channelId,entId,channelCode,channelName,status");
		List<ChannelInfoModel> channelList = this.getTemplate().select(query, ChannelInfoModel.class, "channelInfo");
		int total_results = 0;
		if (channelList != null && channelList.size() > 0) {
			total_results = channelList.size();
		}
		
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), channelList);
		result.put("total_results", total_results);
		return ServiceResponse.buildSuccess(result);
	}

}
