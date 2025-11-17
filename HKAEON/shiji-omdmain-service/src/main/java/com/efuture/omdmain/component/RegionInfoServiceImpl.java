package com.efuture.omdmain.component;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.BusinessCompanyModel;
import com.efuture.omdmain.model.RegionInfoModel;
import com.efuture.omdmain.model.RegionInfoTreeBean;
import com.efuture.omdmain.service.RegionInfoService;
import com.efuture.omdmain.utils.CommonUtils;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.ParamValidateUtil;

public class RegionInfoServiceImpl extends JDBCCompomentServiceImpl<RegionInfoModel> implements RegionInfoService {

	public RegionInfoServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	/**
	 * 只展示香港行政区域树
	 */
	public ServiceResponse reegionTreeHK(ServiceSession session, JSONObject paramsObject) {
		// 1.查询所有门店数据
		Criteria criteria = null;
		criteria = Criteria.where("entId").is(session.getEnt_id()).and("status").is(1);
		Query query = new Query(criteria);
		this.getTemplate().onSetContext(session);
		Field fields = query.fields();
		fields.include("regionId,regionCode,regionName,parentId,parentCode,level,leafFlag,status");
		List<RegionInfoTreeBean> allRegionInfo = this.getTemplate().select(query, RegionInfoTreeBean.class,"regioninfo");
		//2.借助HashMap生成树(剔除其余的树，只保留香港数据)
		CommonUtils<RegionInfoTreeBean> commonUtils = new CommonUtils<>();
		HashMap<Long, List<RegionInfoTreeBean>> pidRegionTree = commonUtils.getPidEnty(allRegionInfo);
		List<RegionInfoTreeBean> topTree = pidRegionTree.get(-1l);
		List<RegionInfoTreeBean> topTreeHK = new ArrayList<>();
		if(!topTree.isEmpty() && topTree.size()>0) {
			String regionCode = paramsObject.getString("regionCode") == null ? "810000" : paramsObject.getString("regionCode");
			topTree.forEach(action ->{
				if(regionCode.equals(action.getRegionCode())){
					topTreeHK.add(action);
					buildTree(topTreeHK, pidRegionTree);// 从顶级节点开始往下递归
				}
			});
		}
		return ServiceResponse.buildSuccess(topTreeHK);
	}

	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onQuery(session, paramsObject);
	}
	
	
	@Override
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "regionId","regionCode");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;		
		this.setUpsert(false);
		paramsObject.put("updateDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		paramsObject.put("creator", session.getUser_name());
		return this.onUpdate(session, paramsObject);
	}

	@Override
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception {

		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (!paramsObject.containsKey("regionCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "regionCode"));
		}

		// 行政区域编码去重校验
		JSONObject paramMap = new JSONObject();
		paramMap.put("regionCode", paramsObject.getString("regionCode"));// 行政区域编码
		ServiceResponse regionInfoQuery = this.onQuery(session, paramMap);
		JSONObject regionInfoData = (JSONObject) regionInfoQuery.getData();
		JSONArray regionInfoList = regionInfoData.getJSONArray(this.getCollectionName());
		if (regionInfoList.size() > 0) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "该行政区域编码已存在，请换一个编码再试");
		}

		this.setUpsert(false);
		paramsObject.put("createDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		paramsObject.put("creator", session.getUser_name());
		paramsObject.put("updateDate", paramsObject.get("createDate"));
		return this.onInsert(session, paramsObject);
	}
	
	
	public ServiceResponse OnRegionTree(ServiceSession session, JSONObject paramsObject) {
		System.out.println("start------" + new Date().getTime());
		List<RegionInfoTreeBean> RegionInfo = onRegionTreeForCompany(session,paramsObject);
		paramsObject.clear();
		paramsObject.put("regioninfo", RegionInfo);
		System.out.println("end------" + new Date().getTime());
		return ServiceResponse.buildSuccess(paramsObject);
	}

	// 增加经营公司的树
	public List<RegionInfoTreeBean> onRegionTreeForCompany(ServiceSession session, JSONObject paramsObject) {
		this.getTemplate().onSetContext(session);
		Long entId = session.getEnt_id();
		Criteria criteria = Criteria.where("entId").is(entId).and("status").is("1");
		// 如果传入参数有erpCode，只查当前的erpCode
		String erpCode = paramsObject.getString("erpCode");
		criteria = erpCode != null ? criteria.and("erpCode").is(erpCode) : criteria;
		Query query = new Query(criteria);
		// 查询经营公司
		List<BusinessCompanyModel> allBusinessCompany = this.getTemplate().select(query, BusinessCompanyModel.class,
				"businesscompany");
		// 保存虚拟树的List
		List<RegionInfoTreeBean> companyRegionTrees = new ArrayList<RegionInfoTreeBean>();
		for (BusinessCompanyModel businessCompanyModel : allBusinessCompany) {
			paramsObject.put("erpCode", businessCompanyModel.getErpCode());
			List<RegionInfoTreeBean> regionTrees = regionTree(session, paramsObject);
			// 虚拟构造树
			RegionInfoTreeBean RegionInfoModel = new RegionInfoTreeBean();
			RegionInfoModel.setRegionName(businessCompanyModel.getErpName());
			RegionInfoModel.setRegionCode("B" + businessCompanyModel.getErpCode());
			RegionInfoModel.setLevel((short) -1);
			RegionInfoModel.setChildren(regionTrees);
			companyRegionTrees.add(RegionInfoModel);
		}
		return companyRegionTrees;
	}
		
	// 展示所有工业分类信息 （树形结构）
	public List<RegionInfoTreeBean> regionTree(ServiceSession session, JSONObject paramsObject) {
		// 1.查询所有门店数据
		Criteria criteria = null;

		criteria = Criteria.where("entId").is(session.getEnt_id()).and("status").is(1);
		Query query = new Query(criteria);
		this.getTemplate().onSetContext(session);
		Field fields = query.fields();
		fields.include("regionId,regionCode,regionName,parentId,parentCode,level,leafFlag,status");

		List<RegionInfoTreeBean> allRegionInfo = this.getTemplate().select(query, RegionInfoTreeBean.class,
				"regioninfo");
		// 借助HashMap生成树
		CommonUtils<RegionInfoTreeBean> commonUtils = new CommonUtils<>();
		HashMap<Long, List<RegionInfoTreeBean>> pidRegionTree = commonUtils.getPidEnty(allRegionInfo);
		List<RegionInfoTreeBean> topTree = pidRegionTree.get(-1l);
		if (topTree == null || topTree.size() == 0)
			return new ArrayList<>();
		System.out.println("开始递归----" + new Date().getTime());
		buildTree(topTree, pidRegionTree);// 从顶级节点开始往下递归
		System.out.println("结束递归----" + new Date().getTime());
		return topTree;
	}
		
	// 从顶级节点开始递归构建树
	private void buildTree(List<RegionInfoTreeBean> root, HashMap<Long, List<RegionInfoTreeBean>> pidRegionTree) {

		for (RegionInfoTreeBean regionInfoTreeBean : root) {
			if (pidRegionTree.get(regionInfoTreeBean.getRegionId()) == null)
				continue;
			regionInfoTreeBean.setChildren(pidRegionTree.get(regionInfoTreeBean.getRegionId()));
			pidRegionTree.remove(regionInfoTreeBean.getRegionId());
			buildTree(regionInfoTreeBean.getChildren(), pidRegionTree);
		}
	}
	
	/**
	 * 由末级区域id查询上一级区域id和名称 ---2019-10-31
	 */
	public ServiceResponse getParentInfoById(ServiceSession session, JSONObject paramsObject) {

		ParamValidateUtil.paramCheck(session, paramsObject, "regionId");
		try {
			// 1.查询末级区域数据
			String regionId = paramsObject.getString("regionId");
			Criteria criteria = Criteria.where("regionId").is(regionId);
			Query query = new Query(criteria);
			this.getTemplate().onSetContext(session);
			Field fields = query.fields();
			fields.include("regionId,regionCode,regionName,parentId,parentCode,level,leafFlag,status");
			List<RegionInfoModel> regionInfo = this.getTemplate().select(query, RegionInfoModel.class, "regioninfo");

			if (regionInfo == null || regionInfo.size() == 0) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "id为[{0}]的区域不存在！", regionId);
			} else {
				RegionInfoModel regionInfoModel = regionInfo.get(0);
				if (regionInfoModel.getLevel() != null && regionInfoModel.getLevel() == 1) {
					return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "id为[{0}]的区域没有上级！", regionId);
				}
				String parentId = regionInfoModel.getParentId() == null ? "-11"
						: regionInfoModel.getParentId().toString();

				// 2.查询上一级区域数据
				criteria = Criteria.where("regionId").is(parentId);
				query = new Query(criteria);
				fields = query.fields();
				fields.include("regionId,regionCode,regionName,parentId,parentCode,level,status");
				regionInfo = this.getTemplate().select(query, RegionInfoModel.class, "regioninfo");
				if (regionInfo == null || regionInfo.size() == 0) {
					return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "该区域的上一级区域没找到！");
				}
				regionInfoModel = regionInfo.get(0);
				JSONObject result = new JSONObject();
				result.put("regionId", parentId);
				result.put("regionCode", regionInfoModel.getRegionCode());
				result.put("regionName", regionInfoModel.getRegionName());
				return ServiceResponse.buildSuccess(result);
			}
		} catch (Exception e) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询区域失败");
		}
	}
		
}
