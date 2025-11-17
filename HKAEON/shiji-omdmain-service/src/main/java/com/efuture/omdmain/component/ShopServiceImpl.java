package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.BusinessCompanyModel;
import com.efuture.omdmain.model.SaleOrgTreeBean;
import com.efuture.omdmain.model.ShopModel;
import com.efuture.omdmain.model.ShopTreeBean;
import com.efuture.omdmain.service.ShopService;
import com.efuture.omdmain.utils.CommonUtils;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.mongodb.DBObject;
import com.product.component.AuthorityBaseServiceImpl;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.BeanConstant;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.ParamValidateUtil;
import com.product.util.TypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.product.util.ParamValidateUtil.checkParam;

/**
 * 门店信息Service
 * 
 */
public class ShopServiceImpl extends CommonServiceImpl<ShopModel,ShopServiceImpl> implements ShopService {
	private static final Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);
	
	@Autowired
	private StallInfoServiceImpl stallInfoService;
	
	@Autowired
	private SaleOrgServiceImpl saleOrgService;
	
	@Autowired
	private ShopServiceRedisImpl shopServiceRedisImpl;
	
	public ShopServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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
	private ShopChannelRefServiceImpl shopChannelRefServiceImpl;
	@Autowired
	private ChannelInfoServiceImpl channelInfoServiceImpl;
	@Autowired
	private RegionInfoServiceImpl regionInfoServiceImpl;

	@Autowired
	AuthorityBaseServiceImpl authorityBaseService;
	
	//当前登陆者所属门店如果有DC仓，返回所有门店。否则返回登录者的所属门店
	public ServiceResponse judgeDC(ServiceSession session, JSONObject paramsObject) throws Exception {
		//1.校验
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"erpCode");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		Long entId = session.getEnt_id();
	
		//2.通过登陆者所属权限门店ID查询是否有有DC仓
		Criteria criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(entId).and("leafFlag").is(1);
		Query query = new Query(criteria);
		this.getTemplate().onSetContext(session);
		List<ShopModel>  shops = this.getTemplate().select(query, ShopModel.class, "shop");	
		for (ShopModel shopModel : shops) {
			if("3".equals(shopModel.getShopTypex())){
				//门店类型 0-普通店/1-中心店/2-小店、3-DC（用于门店补货）
				paramsObject.put("isAllShopFlag",true);
				criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(entId).and("leafFlag").is(1);
				query = new Query(criteria);
				paramsObject.put("shops",this.getTemplate().select(query, ShopModel.class, "shop_view"));
				return ServiceResponse.buildSuccess(paramsObject);
			}
		}
		paramsObject.put("isAllShopFlag",false);
		paramsObject.put("shops",shops);
		return ServiceResponse.buildSuccess(paramsObject);
	}
	
	
	// 通过regionId查询上层结构
	public ServiceResponse searchBottomToRoot(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"regionId");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		
		ServiceResponse response = regionInfoServiceImpl.onQuery(session, paramsObject);
		if (!response.getReturncode().equals(ResponseCode.SUCCESS)) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询行政区域表失败");
		}
		
		List<JSONObject> regionList = new ArrayList<>();
		JSONObject dataObject = (JSONObject) response.getData();
		JSONArray regiondata = dataObject.getJSONArray("regionInfo");
		if(regiondata!=null && regiondata.isEmpty()){
			this.logger.info(" ---------->>>>> 没有查询到查询行政区域:regionId:"+paramsObject.getString("regionId"));
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "没有查询到查询行政区域:regionId:"+paramsObject.getString("regionId")); 
		}
		
		JSONObject region = (JSONObject) regiondata.get(0);
		regionList.add(region);
		
		int level = region.getInteger("level");
		String parentId = region.getString("parentId");
		
		JSONObject param = new JSONObject();
		while(level - 1 > 0 ){
			param.clear();
			param.put("regionId", parentId);
			response = regionInfoServiceImpl.onQuery(session, param);
			if (!response.getReturncode().equals(ResponseCode.SUCCESS)) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询行政区域表失败");
			}
			
			dataObject = (JSONObject) response.getData();
			regiondata = dataObject.getJSONArray("regionInfo");
			if(regiondata!=null && regiondata.isEmpty()){
				this.logger.info(" ---------->>>>> 没有查询到查询行政区域:regionId:"+parentId);
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "没有查询到查询行政区域:regionId:"+parentId); 
			}
			region = (JSONObject) regiondata.get(0);
			regionList.add(region);
		    parentId = region.getString("parentId");
		    level = region.getInteger("level");
		}
	
		Collections.reverse(regionList);
		return ServiceResponse.buildSuccess(regionList);
	}

	// 查询
	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {

		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		if (!paramsObject.containsKey("shopId")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "shopId"));
		}

		// 查门店
		ServiceResponse shopQuery = this.onQuery(session, paramsObject);
		if (!shopQuery.getReturncode().equals(ResponseCode.SUCCESS)) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询门店表失败");
		}
		
		JSONObject shopData = (JSONObject) shopQuery.getData();
		JSONArray shopList = shopData.getJSONArray("shop");
		if (shopList.size() == 0) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "所选门店不存在");
		}

		JSONObject json = (JSONObject) shopList.get(0);
		String regionId = json.get("regionId") + "";// 区域id
		Long shopparentId = json.getLong("parentId"); // 上级id
//		String erpName = json.getString("erpName");// 公司名称

		// 查上级机构
		List<JSONObject> parentName = new ArrayList<>();// 存上级机构
		JSONObject jsonParentName = new JSONObject();

		if (StringUtils.isEmpty(shopparentId) || shopparentId == 0 ) {
			jsonParentName.put("shopName", "");
		} else {
			/*JSONObject parentparams = new JSONObject();
			parentparams.put("shopId", shopparentId);
			ServiceResponse parentQuery = this.onQuery(session, parentparams);
			
			if (!parentQuery.getReturncode().equals(ResponseCode.SUCCESS)) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询门店表失败");
			}
			
			JSONObject parentData = (JSONObject) parentQuery.getData();
			JSONArray parentdata = parentData.getJSONArray("shop");
			
			JSONObject parent = (JSONObject) parentdata.get(0);
			String shopName = parent.getString("shopName");
			jsonParentName.put("shopId", shopparentId);
			jsonParentName.put("shopName", shopName);*/
			
			//上面注释代码本身没问题，因设计权限问题无法查询上级数据。应该查询视图（查询表走了权限）
			Criteria criteria = Criteria.where("shopId").is(shopparentId);
			Query query = new Query(criteria);
			ShopModel shopModel = this.getTemplate().selectOne(query, ShopModel.class, "shop_view");
			if(shopModel!=null){
				jsonParentName.put("shopId", shopparentId);
				jsonParentName.put("shopName", shopModel.getShopName());
			}
		}
		parentName.add(jsonParentName);

		// 查行政区域
		JSONObject regionParams = new JSONObject();
		regionParams.put("regionId", regionId);
		ServiceResponse regionQuery = regionInfoServiceImpl.search(session, regionParams);
		JSONObject regionData = (JSONObject) regionQuery.getData();
		JSONArray regionDataList = regionData.getJSONArray("regionInfo");

		List<JSONObject> tempList = new ArrayList<>();
		List<JSONObject> regionList = new ArrayList<>();

		if (!StringUtils.isEmpty(regionId) && regionDataList.size() > 0) {

			JSONObject regionjson = (JSONObject) regionDataList.get(0);
			int level = regionjson.getInteger("level");// 层级

			for (int i = level; i > 0; i--) {

				JSONObject param = new JSONObject();
				param.put("regionId", regionId);
				ServiceResponse ssData = regionInfoServiceImpl.onQuery(session, param);
				
				if (!ssData.getReturncode().equals(ResponseCode.SUCCESS)) {
					return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询行政区域表失败");
				}
				
				JSONObject dataObject = (JSONObject) ssData.getData();
				JSONArray regiondata = dataObject.getJSONArray("regionInfo");
				JSONObject region = (JSONObject) regiondata.get(0);

				String regionName1 = region.getString("regionName");
				int level1 = region.getInteger("level");
				String parentId1 = region.get("parentId") + "";

				JSONObject jsonChannel = new JSONObject();
				jsonChannel.put("regionId", regionId);
				jsonChannel.put("regionName", regionName1);// 区域名称
				jsonChannel.put("level", level1);// 层级
				jsonChannel.put("parentId", parentId1);// 上级id
				tempList.add(jsonChannel);

				regionId = parentId1;
			}

			for (int i = tempList.size() - 1; i >= 0; i--) {
				regionList.add(tempList.get(i));
			}
		}

		shopData.put("parentshop", parentName);// 上级机构
		shopData.put("shop", shopList); // 门店信息
		shopData.put("regionInfo", regionList);// 行政机构

		return shopQuery;
	}

	// 根据门店查询同一个组和 "00"组 的所有门店
	@Override
	public ServiceResponse searchByGroupCode(ServiceSession session, JSONObject paramsObject) {
		try {
			ServiceResponse check = checkParam(session, paramsObject, "erpCode", "shopCode");
			if(!ResponseCode.SUCCESS.equals(check.getReturncode())) return check;
			List<String> groupCodes = new ArrayList<>();
			List<ShopModel> shopModels = new ArrayList<>();
			//		默认加入组0000
			groupCodes.add("00");
			Query queryGroupCode = new Query();
			queryGroupCode.fields().include("shopCode").include("groupCode");
			ShopModel shopModel = this.getTemplate().selectOne(queryGroupCode.addCriteria(Criteria.where("shopCode")
					.is(paramsObject.get("shopCode")).and("erpCode").is(paramsObject.get("erpCode")).and("entId")
					.is(session.getEnt_id())), ShopModel.class, this.getCollectionName());
			if(shopModel == null) return ServiceResponse.buildSuccess(toJsonObject(shopModels));
			if(shopModel.getGroupCode() != null && !"00".equals(shopModel.getGroupCode()))
				groupCodes.add(shopModel.getGroupCode());
			Query query = new Query();
			query.fields().include("shopCode").include("shopId").include("shopName").include("shopSName");
			shopModels = this.getTemplate().select(query.addCriteria(Criteria.where("groupCode")
					.in(groupCodes).and("erpCode").is(paramsObject.get("erpCode")).and("entId")
					.is(session.getEnt_id())), ShopModel.class, this.getCollectionName());
			shopModels = shopModels != null ? shopModels: new ArrayList<>();

			return ServiceResponse.buildSuccess(toJsonObject(shopModels));
		}catch (Exception e){
			logger.error(e.getMessage());
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询失败");
		}
	}

//	格式化返回结果
	private  JSONObject toJsonObject(Object object){
		if (object == null) return  null;
		JSONObject rtObject = new JSONObject();
		rtObject.put(this.getCollectionName(), object);
		return  rtObject;
	}

	// 根据shopId查门店渠道
	public ServiceResponse getShopChannelByShopId(ServiceSession session, JSONObject paramsObject) throws Exception {

		// 参数校验
		if (!paramsObject.containsKey("shopId")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "shopId"));
		}
		ParamValidateUtil.paramCheck(session, paramsObject, "shopId");

		// 查全部线上渠道
		JSONObject paramsAll = new JSONObject();
		paramsAll.put("status", 1);
		paramsAll.put("channelType", 1);
		paramsAll.put("order_field", "channelId");// 排序字段
		paramsAll.put("order_direction", "desc"); // 排序方法
		if(paramsObject.containsKey("selfBuildFlag")) {
		  paramsAll.put("selfBuildFlag", paramsObject.get("selfBuildFlag"));
		}
		boolean showFlag = true;
		if(paramsObject.containsKey("flag")){
			showFlag = TypeUtils.castToBoolean(paramsObject.get("flag"));
		}
		
		ServiceResponse channelAllQuery = channelInfoServiceImpl.onQuery(session, paramsAll);
		
		if(!channelAllQuery.getReturncode().equals(ResponseCode.SUCCESS)){
			return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST,"渠道数据不存在");
		}

		JSONObject dataAllChannel = (JSONObject) channelAllQuery.getData();
		JSONArray dataAllList = dataAllChannel.getJSONArray("channelInfo");

		// 经营渠道
		ServiceResponse shopChannelQuery = shopChannelRefServiceImpl.onQuery(session, paramsObject);
		
		if(!shopChannelQuery.getReturncode().equals(ResponseCode.SUCCESS)){
			return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST,"渠道关系数据不存在");
		}
		
		JSONObject shopChannelData = (JSONObject) shopChannelQuery.getData();
		JSONArray dataShopChannelList = shopChannelData.getJSONArray("shopChannelRef");

		List<JSONObject> result = new ArrayList<JSONObject>();

		if (dataAllList.size() > 0) {

			for (int i = 0; i < dataAllList.size(); i++) {
				JSONObject json1 = (JSONObject) dataAllList.get(i);
				Long channelId1 = json1.getLong("channelId");

				boolean falgState = false;
				long ocrid = 0L;

				JSONObject jsonChannel = new JSONObject();

				if (dataShopChannelList.size() > 0) {

					for (int j = 0; j < dataShopChannelList.size(); j++) {
						JSONObject json2 = (JSONObject) dataShopChannelList.get(j);
						Long channelId2 = json2.getLong("channelId");
						Long ocrid2 = json2.getLong("ocrid");

						if (channelId1.equals(channelId2)) {
							falgState = true;
							ocrid = ocrid2;
						}
					}
				}

				jsonChannel.put("ocrid", ocrid);// 机构渠道关联ID
				jsonChannel.put("channelId", json1.getLong("channelId"));
				jsonChannel.put("flag", falgState);// 标记门店渠道关联表有没有是否有渠道
				jsonChannel.put("channelName", json1.getString("channelName"));
				jsonChannel.put("status", json1.getString("status"));
				jsonChannel.put("channelCode", json1.getString("channelCode"));
				if(showFlag){
					if(falgState){
						result.add(jsonChannel);
					}
				}else{
					result.add(jsonChannel);
				}
			}
		}

		// 渠道信息
		dataAllChannel.put("channelInfo", result);
		dataAllChannel.put("total_results", result.size());
		return channelAllQuery;
	}

	// 编辑
	@Transactional(rollbackFor = Exception.class)
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {

		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}

//		this.setUpsert(false);
		JSONObject shopUpdate = new JSONObject();
		shopUpdate.put("shopId", paramsObject.getLong("shopId"));
		if (StringUtils.isEmpty(paramsObject.get("regionId"))) {
			shopUpdate.put("regionId", -1); // 行政区域ID
		} else {
			shopUpdate.put("regionId", paramsObject.getString("regionId")); // 行政区域ID
		}
		shopUpdate.put("address", paramsObject.getString("address"));     // 详细地址
		shopUpdate.put("enAddress", paramsObject.getString("enAddress")); // 详细地址(英文)（用户清空英文地址无法保存）
		shopUpdate.put("longitude", paramsObject.getString("longitude")); // 精度
		shopUpdate.put("latitude", paramsObject.getString("latitude"));   // 纬度
		shopUpdate.put("serviceRadiu", paramsObject.getInteger("serviceRadiu")); // 服务半径
		shopUpdate.put("modifier", session.getUser_code());
		shopUpdate.put("updateDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		shopUpdate.put("shopName",paramsObject.getString("shopName"));//中文名称
		shopUpdate.put("shopEnName",paramsObject.getString("shopEnName"));//英文名称
		shopUpdate.put("shopSName",paramsObject.getString("shopSName"));//简称
		shopUpdate.put("shopSize",paramsObject.getString("shopSize"));//门店规模
		shopUpdate.put("shopTypex",paramsObject.getString("shopTypex"));//门店类型
		shopUpdate.put("companyName",paramsObject.getString("companyName"));//公司名称
		shopUpdate.put("status",paramsObject.getString("status"));//状态
		shopUpdate.put("telephone",paramsObject.getString("telephone"));//联系电话
//		ServiceResponse shopResponse = this.onUpdate(session, shopUpdate);// 修改shop
//		if (!ResponseCode.SUCCESS.equals(shopResponse.getReturncode())) {
//			throw new RuntimeException("保存失败，" + shopResponse.getData().toString());
//		}
		// 修改清空英文地址等无法保存问题
		this.getTemplate().getSqlSessionTemplate().update("beanmapper.ShopModelMapper.updateShop",shopUpdate);
		
		JSONArray dataList = paramsObject.getJSONArray("shopChannelRef");
		if(dataList == null ) dataList = new JSONArray();
		if (dataList.size() > 0) {
			for (int i = 0; i < dataList.size(); i++) {
				JSONObject dataMap = dataList.getJSONObject(i);
				Boolean dataFlag = (Boolean) dataMap.get("flag");

				JSONObject parentparams = new JSONObject();
				parentparams.put("shopId", dataMap.getLong("shopId"));
				parentparams.put("channelId", dataMap.getLong("channelId"));
				ServiceResponse shopChannelQuery = shopChannelRefServiceImpl.onQuery(session, parentparams);
				JSONObject shopChannelData = (JSONObject) shopChannelQuery.getData();
				JSONArray shopChanneldata = shopChannelData.getJSONArray("shopChannelRef");

				if (shopChanneldata.size() < 1) {// 数据库没有数据时，可新增
					if (dataFlag == true) {
						this.setUpsert(false);
						dataMap.put("createDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						dataMap.put("updateDate",dataMap.get("createDate"));
						dataMap.put("creator", session.getUser_name());
						dataMap.put("status", 1);//新增增加状态
						shopChannelRefServiceImpl.onInsert(session, dataMap);
					}
				} else {// 数据库有数据时，可删除
					if (dataFlag == false) {
						dataMap.remove("shopCode");
						dataMap.remove("channelCode");
						shopChannelRefServiceImpl.onDelete(session, dataMap);
					}
				}
			}
		}
		return ServiceResponse.buildSuccess("success");
	}

	// 封装返回格式 （门店树形结构展示）
	public ServiceResponse onShopTree(ServiceSession session, JSONObject paramsObject) throws Exception {
		List<ShopTreeBean> shopTree = shopTreeForCompany(session, paramsObject);
		paramsObject.clear();
		paramsObject.put("shop", shopTree);
		return ServiceResponse.buildSuccess(paramsObject);
	}

	// 增加经营公司的树
	public List<ShopTreeBean> shopTreeForCompany(ServiceSession session, JSONObject paramsObject) {
		this.getTemplate().onSetContext(session);
		Long entId = session.getEnt_id();
		Criteria criteria = Criteria.where("entId").is(entId).and("status").is("1");
		Query query = new Query(criteria);
		// 查询经营公司
		List<BusinessCompanyModel> allBusinessCompany = this.getTemplate().select(query, BusinessCompanyModel.class,"businesscompany");
		// 保存虚拟树的List
		List<ShopTreeBean> companyShopTrees = new ArrayList<ShopTreeBean>();
		for (BusinessCompanyModel businessCompanyModel : allBusinessCompany) {
			paramsObject.put("erpCode", businessCompanyModel.getErpCode());
			List<ShopTreeBean> shopTrees = shopTree(session, paramsObject);// 获取某个经营公司的树
			// 虚拟构造树
			ShopTreeBean companyShopTreeBean = new ShopTreeBean();
			companyShopTreeBean.setShopName(businessCompanyModel.getErpName());
			//Bug：经营公司和门店两张表数据构成的树，出现编码重复的情况。经营公司编码前加上字母B
			companyShopTreeBean.setShopCode("B"+businessCompanyModel.getErpCode());
			companyShopTreeBean.setErpCode(businessCompanyModel.getErpCode());
			companyShopTreeBean.setLevel((short)-1);
			companyShopTreeBean.setChildren(shopTrees);
			companyShopTrees.add(companyShopTreeBean);
		}
		return companyShopTrees;
	}
	
	// 展示所有门店信息 （树形结构）
	public List<ShopTreeBean> shopTree(ServiceSession session, JSONObject paramsObject) {
		// 1.查询所有门店数据
		Long entId = session.getEnt_id();
		//增加门店状态判断。默认是有效
		String status = paramsObject.getString("status");
		Criteria criteria = null;
		if(StringUtils.isEmpty(status)) {
			criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(entId);
		}else {
			criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(entId).and("status").is(status);
		}
		Query query = new Query(criteria);
		query.with(new Sort(Direction.ASC, "shopCode "));//增加门店编码排序
		this.getTemplate().onSetContext(session);
		Field fields = query.fields();
		fields.include("shopId,shopCode,shopName,shopSName,parentId,parentCode,level,leafFlag,erpCode,shopTypex,shopForm");
		boolean isAuth = false; // 默认不走权限控制
		String tableName =  "shop";
		List<String>shopIds = null;
		try {
			shopIds = authorityBaseService.getDataRangeShop(session);
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		logger.info("==========>>> 【shopTree】Redis shopIds : {}",shopIds);
		if((!paramsObject.containsKey("auth") || "1".equals(paramsObject.get("auth"))) && shopIds != null && shopIds.size() > 0){
//			走权限控制
			isAuth = true;
		}
		logger.info("==========>>> 【shopTree】 isAuth: {}",isAuth);
		List<ShopTreeBean> allShops = new ArrayList<>();
		try {
			allShops = this.getTemplate().select(query, ShopTreeBean.class, "shop_view");
		}catch (Exception e){
			logger.error(e.getMessage());
			allShops = this.getTemplate().select(query, ShopTreeBean.class, "shop");
		}

		logger.info("allShops【" + allShops.size() + "】:"+ allShops.stream().map(ShopTreeBean::getShopId).collect(Collectors.toList()));
		//		得到权限范围内的树
		if(isAuth){
			allShops =  getAuthShops(session, allShops);
		}
		CommonUtils<ShopTreeBean> commonUtils = new CommonUtils<>();
		HashMap<Long, List<ShopTreeBean>> pidShopTree = commonUtils.getPidEnty(allShops);
		List<ShopTreeBean> topTree = pidShopTree.get(-1l);
		if(topTree == null || topTree.size() == 0) return new ArrayList<>();
		System.out.println("开始递归new----" + new Date().getTime());
		buildTree(topTree, pidShopTree);//从顶级节点开始往下递归
		System.out.println("结束递归new----" + new Date().getTime());
		return topTree;
	}

	// 通过权限得到属于权限内的门店
	private List<ShopTreeBean> getAuthShops(ServiceSession session,  List<ShopTreeBean> allShops) {
		List<String> shopIds = authorityBaseService.getDataRangeShop(session);
		logger.info("redis get shopIds" + shopIds.size() + shopIds.toString());
//        List<String> shopIds = Arrays.asList("476");
		List<ShopTreeBean> lastShops = new ArrayList<>();
		List<ShopTreeBean> rtShops = new ArrayList<>();
		getLastShops(allShops, lastShops, shopIds);
		if(lastShops.size() < 1) {
			return new ArrayList<>();
		}
		getShopList(lastShops, allShops, rtShops);
		allShops.clear();
		return rtShops;
	}

	//	得到权限范围内的shop
	private void getLastShops(List<ShopTreeBean> allShops, List<ShopTreeBean> lastShops, List<String> shopIds) {
		for (ShopTreeBean allShop : allShops) {
			if(allShop.getLeafFlag()){
				for (String shopId : shopIds) {
					if(String.valueOf(allShop.getShopId()).equals(shopId)){
						lastShops.add(allShop);
					}
				}
			}
		}
	}


	private void getShopList(List<ShopTreeBean>chidList, List<ShopTreeBean> allShops, List<ShopTreeBean> rtShops){
		while (chidList.size() > 0){
			List<ShopTreeBean> newChidList = new ArrayList<>();
			for (ShopTreeBean shopTreeBean : chidList) {
				rtShops.add(shopTreeBean);
//				最顶级 就直接返回
				if(shopTreeBean.getLevel() == 1 ||shopTreeBean.getParentCode() == null ||
						"0".equals(shopTreeBean.getParentCode())) continue;
				for (ShopTreeBean allShop : allShops) {
					if(shopTreeBean.getParentId().equals(allShop.getShopId())){
						if(!newChidList.contains(allShop)){
							newChidList.add(allShop);
						}
						break;
					}
				}
			}
			chidList = newChidList;
		}
	}

	//从顶级节点开始递归构建树
	private void buildTree(List<ShopTreeBean> root, HashMap<Long, List<ShopTreeBean>> pidShopTree) {

		for (ShopTreeBean shopTreeBean : root) {
			if (pidShopTree.get(shopTreeBean.getShopId()) == null) continue;
			shopTreeBean.setChildren(pidShopTree.get(shopTreeBean.getShopId()));
			pidShopTree.remove(shopTreeBean.getShopId());
			buildTree(shopTreeBean.getChildren(), pidShopTree);
		}
	}

//	// 从顶级节点开始递归构建树
//	private List<ShopTreeBean> buildTree(List<ShopTreeBean> root, List<ShopTreeBean> allShops) {
//		for (ShopTreeBean shop : root) {
//			Long parentId = shop.getShopId();
//			String parentName = shop.getShopName();
//			List<ShopTreeBean> children = findChildren(parentId, parentName, allShops);
//			buildTree(children, allShops);
//			shop.setChildren(children);
//		}
//		return root;
//	}

	// 查询该节点的所有儿子节点
	private List<ShopTreeBean> findChildren(Long parentId, String parentName, List<ShopTreeBean> allShops) {
		List<ShopTreeBean> childrenShop = new ArrayList<ShopTreeBean>();
		for (ShopTreeBean shop : allShops) {
			if (parentId.equals(shop.getParentId())) {
				shop.setParentName(parentName);// 增加父亲节点名称
				childrenShop.add(shop);
			}
		}
		return childrenShop;
	}

	public ServiceResponse serachByLeaf(ServiceSession session, JSONObject paramsObject) throws Exception {

		if (session == null) return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject)) return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);

		if (!paramsObject.containsKey("leafFlag")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "leafFlag"));
		}

//		Criteria criteria = StringUtils.isEmpty(paramsObject.get("erpCode")) ? Criteria.where("leafFlag").is(paramsObject.get("leafFlag")):
//							Criteria.where("leafFlag").is(paramsObject.get("leafFlag")).and("erpCode").is(paramsObject.get("erpCode"));
//		Query query = new Query(criteria);
//		List<ShopModel> shopList = this.getTemplate().select(query, ShopModel.class, "shop");
//		paramsObject.clear();
//		paramsObject.put(this.getCollectionName(), shopList);
//		return ServiceResponse.buildSuccess(paramsObject);

		if (!paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_PAGENO)) {
			paramsObject.put(BeanConstant.QueryField.PARAMKEY_PAGENO,1);
		}
		if (!paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_PAGESIZE)) {
			paramsObject.put(BeanConstant.QueryField.PARAMKEY_PAGESIZE,1000);
		}
		paramsObject.put(BeanConstant.QueryField.PARAMKEY_ORDERFLD, "shopCode");//增加门店编码排序
		paramsObject.put("status", 1);//查询出有效状态的门店
		return this.onQuery(session, paramsObject);
	}

	
	// 订单中心使用(废弃)
	public ServiceResponse getSonShopByErpCode(ServiceSession session, JSONObject paramsObject) {
		
		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (!paramsObject.containsKey("erpCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "erpCode"));
		}
		
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		this.getTemplate().onSetContext(session);
		Long entId = session.getEnt_id();
		String erpCode = paramsObject.getString("erpCode");
		Criteria criteria = Criteria.where("entId").is(entId).and("erpCode").is(erpCode).and("leafFlag").is(true);
		Query query = new Query(criteria);
		List<ShopModel> shopList = this.getTemplate().select(query, ShopModel.class, "shop");
		
		long total_results = shopList.size(); // 总数
		paramsObject.clear();
		paramsObject.put(this.getCollectionName(),shopList);
		paramsObject.put("total_results",total_results);
		return ServiceResponse.buildSuccess(paramsObject);
	}
			
	
	// 封装返回--柜组树形结构展示
	public ServiceResponse onSaleOrgTree(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (!paramsObject.containsKey("shopId")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "shopId"));
		}
		
		// 校验门店存在与否
		ServiceResponse shopQuery = this.onQuery(session, paramsObject);
		JSONObject shopData = (JSONObject) shopQuery.getData();
		JSONArray shopList = shopData.getJSONArray("shop");
		if (shopList.size() == 0) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "所选门店不存在");
		}
		
		List<SaleOrgTreeBean> saleOrgTree = saleOrgTreeForShop(session, paramsObject);
		paramsObject.clear();
		paramsObject.put("saleorg", saleOrgTree);
		return ServiceResponse.buildSuccess(paramsObject);
	}

	// 增加柜组的树
	public List<SaleOrgTreeBean> saleOrgTreeForShop(ServiceSession session, JSONObject paramsObject) {

		this.getTemplate().onSetContext(session);
		Long entId = session.getEnt_id();
		Criteria criteria = Criteria.where("entId").is(entId).and("shopId").is(paramsObject.getLong("shopId"));// 查询当前选中shopId
		Query query = new Query(criteria);
		// 查询组织机构
		ShopModel shopModel = this.getTemplate().selectOne(query, ShopModel.class, "shop");

		// 保存虚拟树的List
		List<SaleOrgTreeBean> shopSaleOrgTrees = new ArrayList<SaleOrgTreeBean>();

		paramsObject.put("shopId", shopModel.getShopId());
		paramsObject.put("erpCode", shopModel.getErpCode());
		paramsObject.put("shopName", shopModel.getShopName());
		paramsObject.put("shopCode", shopModel.getShopCode());
		paramsObject.put("order_field", "parentId");
		List<SaleOrgTreeBean> saleOrgTrees = saleOrgTrees(session, paramsObject);// 获取某个门店柜组树

		// 虚拟构造树
		SaleOrgTreeBean shopSaleOrgTreeBean = new SaleOrgTreeBean();
		if (saleOrgTrees.size() > 0) {// 有柜组

			shopSaleOrgTreeBean.setOrgCode(shopModel.getShopCode());// 编码
			shopSaleOrgTreeBean.setOrgName(shopModel.getShopName());// 名称
			shopSaleOrgTreeBean.setLevel(shopModel.getLevel());// 层级
			shopSaleOrgTreeBean.setChildren(saleOrgTrees);
			shopSaleOrgTreeBean.setParentCode(shopModel.getParentCode());// 上级编码
			shopSaleOrgTreeBean.setParentId(shopModel.getParentId());// 上级Id
			shopSaleOrgTreeBean.setShopId(shopModel.getShopId());
			shopSaleOrgTreeBean.setLeafFlag(false);

			shopSaleOrgTrees.add(shopSaleOrgTreeBean);
		}

		return shopSaleOrgTrees;
	}
	
	// 展示所有柜组信息 （树形结构）
	public List<SaleOrgTreeBean> saleOrgTrees(ServiceSession session, JSONObject paramsObject) {

		// 1.查询所有柜组数据
		Long entId = session.getEnt_id();
		Long shopId = paramsObject.getLong("shopId");
		String shopCode = paramsObject.getString("shopCode");
		String shopName = paramsObject.getString("shopName");
		Criteria criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(entId).and("shopId").is(shopId);
		Query query = new Query(criteria);
		this.getTemplate().onSetContext(session);
		Field fields = query.fields();
		fields.include("saleOrgId,orgCode,orgName,orgSName,shopId,parentId,parentCode,level,leafFlag,erpCode");
		Sort sort = new Sort(new Order(Direction.DESC, paramsObject.getString("order_field")));
		query.with(sort);
		boolean isAuth = false; // 默认不走权限控制
		String tableName =  "saleorg";
		List<String>orgIds = null;
		try {
			orgIds = authorityBaseService.getDataRangeOrg(session);
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		if((!paramsObject.containsKey("auth") || "1".equals(paramsObject.get("auth"))) && orgIds != null && orgIds.size() > 0){
//			走权限控制
			isAuth = true;
		}
		List<SaleOrgTreeBean> allSaleOrgs = new ArrayList<>();
		try {
			allSaleOrgs = this.getTemplate().select(query, SaleOrgTreeBean.class, "saleorg_view");
		}catch (Exception e){
			logger.error(e.getMessage());
			allSaleOrgs = this.getTemplate().select(query, SaleOrgTreeBean.class, "saleorg");

		}

		List<SaleOrgTreeBean> newAllSaleOrgs = new ArrayList<>();// 存可显示的柜组信息
		if(isAuth){
//            对saleOrg 剪枝
			allSaleOrgs = getAuthSaleOrg(session, allSaleOrgs);

		}
//		// 2.获得顶级树数据
//		List<SaleOrgTreeBean> topSaleOrgs = new ArrayList<>();
//
//		boolean falg = true; // 标记所有parentId能不能用于递归，默认可以
//		if (allSaleOrgs.size() > 0) {
//			SaleOrgTreeBean saleOrgTreeBean = allSaleOrgs.get(0);
//			if (StringUtils.isEmpty(saleOrgTreeBean.getParentId())) {
//				falg = false;
//			} else {
//				falg = true;
//			}
//		}
//
//		Long saleOrgId = null;
//		for (SaleOrgTreeBean saleOrg : allSaleOrgs) {
//
//			if (falg) {
//				if(StringUtils.isEmpty(saleOrg.getParentId()) || "0".equals(saleOrg.getParentId().toString())){
//					saleOrgId = saleOrg.getSaleOrgId();
//				}else{
//					newAllSaleOrgs.add(saleOrg);
//				}
//
//			} else {
//				if (!"0".equals(saleOrg.getParentCode().toString()) && !StringUtils.isEmpty(saleOrg.getParentCode())) {
//					newAllSaleOrgs.add(saleOrg);
//				}
//
//				if (StringUtils.isEmpty(saleOrg.getParentCode()) || shopCode.equals(saleOrg.getParentCode().toString())) {
//					saleOrg.setParentName(shopName);// 上级名称
//					topSaleOrgs.add(saleOrg);
//				}
//			}
//		}
//
//		if(falg) {
//			for (SaleOrgTreeBean saleOrg : newAllSaleOrgs) {
//				if ((saleOrgId.toString()).equals(saleOrg.getParentId().toString())) {
//					saleOrg.setParentName(shopName);// 上级名称
//					topSaleOrgs.add(saleOrg);
//				}
//			}
//		}
//
//		List<SaleOrgTreeBean> saleOrgTree =null;
//		if (falg) {
//			saleOrgTree = buildSaleOrgTree1(topSaleOrgs, newAllSaleOrgs);// 从顶级节点开始往下递归--(按parentId递归)
//		} else {
//			saleOrgTree = buildSaleOrgTree(topSaleOrgs, newAllSaleOrgs);// 从顶级节点开始往下递归--(按parentCode递归)
//		}
		return getsaleOrgTree(session, paramsObject, allSaleOrgs);
	}

	public List<SaleOrgTreeBean> getsaleOrgTree(ServiceSession session, JSONObject paramsObject, List<SaleOrgTreeBean> allSaleOrgs) {
		/**
		 * 生成柜组的树，将原来的方法抽取出来，可以提供给柜组中使用
		 * @param session
		 * @param paramsObject
		 * @param allSaleOrgs
		 * @return java.util.List<com.efuture.omdmain.model.SaleOrgTreeBean>
		 * @throws
		 */
		CommonUtils<SaleOrgTreeBean> commonUtils = new CommonUtils<>();
		HashMap<Long, List<SaleOrgTreeBean>> pidSaleOrgTree = commonUtils.getPidEnty(allSaleOrgs);
//		柜组 要去掉第一级
		List<SaleOrgTreeBean> topTree = new ArrayList<>();
		List<SaleOrgTreeBean> topTmpTree = pidSaleOrgTree.get(-1l);
		if(topTmpTree!=null){
			for (SaleOrgTreeBean saleOrgTreeBean : topTmpTree) {
				if(saleOrgTreeBean != null && saleOrgTreeBean.getSaleOrgId() != null) {
					List<SaleOrgTreeBean> saleOrgTreeBeans = pidSaleOrgTree.get(saleOrgTreeBean.getSaleOrgId());
							if(saleOrgTreeBeans != null){
								topTree.addAll(saleOrgTreeBeans);
					}
				}
			}
		}
		if(topTree == null || topTree.size() == 0) return new ArrayList<>();
		System.out.println("开始递归----" + new Date().getTime());
		buildSaleOrgTree1(topTree, pidSaleOrgTree);//从顶级节点开始往下递归
		System.out.println("结束递归----" + new Date().getTime());
		return topTree;
	}

	// 从顶级节点开始递归构建树--（按parentId递归）
	public void buildSaleOrgTree1(List<SaleOrgTreeBean> root, HashMap<Long, List<SaleOrgTreeBean>> pidSaleOrgTree) {
		for (SaleOrgTreeBean saleOrgTreeBean : root) {
			if (pidSaleOrgTree.get(saleOrgTreeBean.getSaleOrgId()) == null) continue;
			saleOrgTreeBean.setChildren(pidSaleOrgTree.get(saleOrgTreeBean.getSaleOrgId()));
			pidSaleOrgTree.remove(saleOrgTreeBean.getSaleOrgId());
			buildSaleOrgTree1(saleOrgTreeBean.getChildren(), pidSaleOrgTree);
		}
	}

	//	根据权限获得剪枝的saleOrg列表
	public List<SaleOrgTreeBean> getAuthSaleOrg(ServiceSession session, List<SaleOrgTreeBean> allSaleOrgs) {
		//            根据权限做出校验
//            首先获得 最末级的 柜组
		List<SaleOrgTreeBean> lastSaleOrgs = new ArrayList<>();
//           获得权限的柜组
		List<String> saleOrgIds = authorityBaseService.getDataRangeOrg(session);
		if (lastSaleOrgs.size() < 1) return lastSaleOrgs;
		getLastSaleOrgs(lastSaleOrgs, allSaleOrgs, saleOrgIds);
//            针对 权限柜组 对柜组 删减
		allSaleOrgs =  getSaleOrgList(lastSaleOrgs, allSaleOrgs);
		return allSaleOrgs;
	}

	// 针对最末级的的柜组对柜组树剪枝
	private List<SaleOrgTreeBean> getSaleOrgList(List<SaleOrgTreeBean> lastSaleOrgs, List<SaleOrgTreeBean> allSaleOrgs) {
		List<SaleOrgTreeBean> rtList = new ArrayList<>();
		List<SaleOrgTreeBean> newLastSaleOrgs = new ArrayList<>();
		while (lastSaleOrgs.size() < 1){
			for (SaleOrgTreeBean lastSaleOrg : lastSaleOrgs) {
				rtList.add(lastSaleOrg);
				if(lastSaleOrg.getLevel() == 1 || lastSaleOrg.getParentId() == null){
					continue;
				}
				for (SaleOrgTreeBean allSaleOrg : allSaleOrgs) {
					if(lastSaleOrg.getParentId().equals(allSaleOrg.getSaleOrgId()))
						newLastSaleOrgs.add(allSaleOrg);
				}
			}
			lastSaleOrgs = newLastSaleOrgs;
		}

		return newLastSaleOrgs;
	}

	// 获得最末级的柜组
	private void getLastSaleOrgs(List<SaleOrgTreeBean> lastSaleOrgs, List<SaleOrgTreeBean> allSaleOrgs, List<String> saleOrgIds) {
		if(saleOrgIds == null || saleOrgIds.size() < 1) return;
		for (SaleOrgTreeBean allSaleOrg : allSaleOrgs) {
			if(!allSaleOrg.getLeafFlag()) continue;
			if(saleOrgIds.contains(allSaleOrg.getSaleOrgId()))
				lastSaleOrgs.add(allSaleOrg);
		}
	}
	
	// 从顶级节点开始递归构建树--（按parentCode递归）
	private List<SaleOrgTreeBean> buildSaleOrgTree(List<SaleOrgTreeBean> root, List<SaleOrgTreeBean> allSaleOrgs) {
		for (SaleOrgTreeBean saleOrg : root) {
			String orgCode = saleOrg.getOrgCode();
			String parentName = saleOrg.getOrgName();
			List<SaleOrgTreeBean> children = findSaleOrgChildren(orgCode, parentName, allSaleOrgs);
			buildSaleOrgTree(children, allSaleOrgs);
			saleOrg.setChildren(children);
		}
		return root;
	}

	// 查询该节点的所有儿子节点--（按parentCode递归）
	private List<SaleOrgTreeBean> findSaleOrgChildren(String orgCode, String parentName,List<SaleOrgTreeBean> allSaleOrgs) {

		List<SaleOrgTreeBean> childrenSaleOrg = new ArrayList<SaleOrgTreeBean>();
			for (SaleOrgTreeBean saleOrg : allSaleOrgs) {
				if (orgCode.equals(saleOrg.getParentCode())) {
					saleOrg.setParentName(parentName);// 增加父亲节点名称
					childrenSaleOrg.add(saleOrg);
				}
			}
		return childrenSaleOrg;
	}

	// 展示所有柜组信息 （树形结构）-------------------备用
	public List<SaleOrgTreeBean> saleOrgTrees1(ServiceSession session, JSONObject paramsObject) {

		// 1.查询所有柜组数据
		Long entId = session.getEnt_id();
		Long shopId = paramsObject.getLong("shopId");
		String shopName = paramsObject.getString("shopName");
		Criteria criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(entId).and("shopId").is(shopId);
		Query query = new Query(criteria);
		this.getTemplate().onSetContext(session);
		Field fields = query.fields();
		fields.include("saleOrgId,orgCode,orgName,orgSName,shopId,parentId,parentCode,level,leafFlag,erpCode");
		List<SaleOrgTreeBean> allSaleOrgs = this.getTemplate().select(query, SaleOrgTreeBean.class, "saleorg");
		
		List<SaleOrgTreeBean> newAllSaleOrgs = new ArrayList<>();

		// 2.获得顶级树数据
		List<SaleOrgTreeBean> topSaleOrgs = new ArrayList<>();
		
		Long saleOrgId = null;
		for (SaleOrgTreeBean saleOrg : allSaleOrgs) {
			
			if(StringUtils.isEmpty(saleOrg.getParentId()) || "0".equals(saleOrg.getParentId().toString())){
				saleOrgId = saleOrg.getSaleOrgId();
			}else{
				newAllSaleOrgs.add(saleOrg);
			}
		}

		for (SaleOrgTreeBean saleOrg : newAllSaleOrgs) {
			
			if ((saleOrgId.toString()).equals(saleOrg.getParentId().toString())) {
				saleOrg.setParentName(shopName);// 上级名称
				topSaleOrgs.add(saleOrg);
			}
		}
		
		return buildSaleOrgTree1(topSaleOrgs, newAllSaleOrgs);// 从顶级节点开始往下递归
	}

	private List<SaleOrgTreeBean> buildSaleOrgTree1(List<SaleOrgTreeBean> root, List<SaleOrgTreeBean> allSaleOrgs) {
		for (SaleOrgTreeBean saleOrg : root) {
			Long parentId = saleOrg.getSaleOrgId();
			String parentName = saleOrg.getOrgName();
			List<SaleOrgTreeBean> children = findSaleOrgChildren1(parentId, parentName, allSaleOrgs);
			buildSaleOrgTree1(children, allSaleOrgs);
			saleOrg.setChildren(children);
		}
		return root;
	}
	
	// 查询该节点的所有儿子节点--（按parentId递归）
	private List<SaleOrgTreeBean> findSaleOrgChildren1(Long parentId, String parentName,List<SaleOrgTreeBean> allSaleOrgs) {

		List<SaleOrgTreeBean> childrenSaleOrg = new ArrayList<SaleOrgTreeBean>();
			for (SaleOrgTreeBean saleOrg : allSaleOrgs) {
				if (parentId.equals(saleOrg.getParentId())) {
					saleOrg.setParentName(parentName);// 增加父亲节点名称
					childrenSaleOrg.add(saleOrg);
				}
			}
		return childrenSaleOrg;
	}

	 /*
	  * @Description: 提供给pos的服务，根据门店code 查询 门店名称
	  * @param {"shopCode":"001", "erpCode":"002"}
	  * @return:
	  */
	public ServiceResponse searchNameByCode(ServiceSession session, JSONObject paramsObject){
//	    if(!paramsObject.containsKey("mkt")){
//	    	logger.error("参数错误，必须含有门店code");
//	    	return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误，必须含有门店code");
//		}
		if(paramsObject.containsKey("mkt")){
			paramsObject.put("shopCode", paramsObject.get("mkt"));
			paramsObject.remove("mkt");
		}

//		if(!paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_FIELDS)){
//	    	paramsObject.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "shopCode,shopName,erpCode");
//		}
		return onQuery(session, paramsObject);
	}
	
	//库存中心调用
	public ServiceResponse searchByErpCode(ServiceSession session, JSONObject paramsObject) {
	  if(!paramsObject.containsKey("erpCode")) {
	    return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "erpCode不能为空.");
	  }
	  String fields = paramsObject.getString("fields");
	  
	  FMybatisTemplate template = this.getTemplate();
	  template.onSetContext(session);
	  paramsObject.put("entId", session.getEnt_id());
	  if("0".equals(paramsObject.getString("erpCode"))) {
	    paramsObject.remove("erpCode");
	  }
	  List<Map<String, Object>> shopInfo = template.getSqlSessionTemplate().selectList("beanmapper.ShopModelMapper.selectByErpCode", paramsObject);
	  
	  JSONObject result = new JSONObject();
	  result.put("total_results", shopInfo.size());
	  result.put(this.getCollectionName(), DefaultParametersUtils.filterByFields(shopInfo, fields));
	  return ServiceResponse.buildSuccess(result);
	}
	
	/**
	 * @Title: 			searchStallInfoAndSaleOrg
	 * @Description: 	门店作业系统-搜索店内部门
	 * @param: 			@param session
	 * @param: 			@param paramsObject
	 * @param: 			@return   
	 * @return: 		ServiceResponse   
	 * @throws
	 */
	public ServiceResponse searchSaleOrgAndStallInfo(ServiceSession session, JSONObject paramsObject){
		//柜组信息
		ServiceResponse response = this.saleOrgService.onQuery(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}
		
		JSONObject obj = (JSONObject) response.getData();
		JSONArray array = obj.getJSONArray(this.saleOrgService.getCollectionName());
		
		JSONArray resultArray = new JSONArray();
		for(int i=0; i<array.size(); i++){
			JSONObject tmp = array.getJSONObject(i);
			JSONObject transfer = new JSONObject();
			transfer.put("id", tmp.get("saleOrgId"));
			transfer.put("text", tmp.get("orgName"));
			transfer.put("code", tmp.get("orgCode"));
			transfer.put("type", "saleOrg");
			resultArray.add(transfer);
		}
		
		//档口信息
		response = this.stallInfoService.onQuery(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}
		obj = (JSONObject) response.getData();
		array = obj.getJSONArray(this.stallInfoService.getCollectionName());
		for(int i=0; i<array.size(); i++){
			JSONObject tmp = array.getJSONObject(i);
			JSONObject transfer = new JSONObject();
			transfer.put("id", tmp.get("siid"));
			transfer.put("text", tmp.get("stallName"));
			transfer.put("code", tmp.get("stallCode"));
			transfer.put("type", "stallInfo");
			resultArray.add(transfer);
		}
		
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), resultArray);
		result.put("total_results", resultArray.size());
		return ServiceResponse.buildSuccess(result);
	}
	
	 public ServiceResponse sychShop(ServiceSession session,JSONObject jsonParam) {
		 this.logger.info("同步【门店】Start----->>> param:"+jsonParam);
		// 参数转换
		ShopModel shop = JSON.toJavaObject(jsonParam, ShopModel.class);
		if(shop==null){
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY,"传入参数格式不对");
		}
		// 查询条件
		try {
			Criteria criteria = Criteria.where("entId").is(jsonParam.getLong("entId")).and("erpCode").is(jsonParam.getString("erpCode")).and("status").is(1);
			Query query = new Query(criteria);
			query.limit(Integer.MAX_VALUE);
			List<ShopModel> newShopList = this.onFind(session, this.getTemplate(), query, ShopModel.class, this.getCollectionName());
			List<Long> newShopIdList = newShopList.stream().map(s->s.getShopId()).collect(Collectors.toList());
			List<ShopModel> oldShopList = shopServiceRedisImpl.queryShop(session, jsonParam);
			List<Long> oldShopIdList = oldShopList.stream().map(s->s.getShopId()).collect(Collectors.toList());
			oldShopIdList.removeAll(newShopIdList);
			if(oldShopIdList!=null&&oldShopIdList.size()>0){
				shopServiceRedisImpl.deleteShop(session, oldShopIdList);
			}
			JSONArray array = new JSONArray();
			for(ShopModel shopModel : newShopList){
				JSONObject json = new JSONObject();
				json.put("shopId", shopModel.getShopId());
				json.put("entId", shopModel.getEntId());
				json.put("erpCode", shopModel.getErpCode());
				json.put("shopCode", shopModel.getShopCode());
				json.put("parentCode", shopModel.getParentCode());
				json.put("parentId", shopModel.getParentId());
				array.add(json);
			}
			shopServiceRedisImpl.insertShop(session, array);
			return ServiceResponse.buildSuccess("同步成功");
			// 在这里加入业务处理
		} catch (Exception ex) {
			logger.error(String.format("signInNoEnt->%1$s -> %2$s","返回登录异常",ex.getMessage()));
			return ServiceResponse.buildFailure(session,ex.getMessage());
		}
	 }
	 
	 /**
	   *  查全部门店业态
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse getALLshopForm(ServiceSession session, JSONObject paramsObject) {
		
		ParamValidateUtil.paramCheck(session, paramsObject);
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		paramsObject.put("entId", session.getEnt_id());
		// 查询门店业态
		List shopFormList = template.getSqlSessionTemplate().selectList("beanmapper.ShopModelMapper.selectALLshopForm", paramsObject);
		if (shopFormList == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "门店业态查询失败！");
		}
		JSONArray array = JSONArray.parseArray(JSON.toJSONString(shopFormList));
		List<JSONObject> list = JSONArray.parseArray(array.toJSONString(), JSONObject.class);
		// 去掉list里面shopForm为空的值
		for (JSONObject object : list) {
			String shopForm = object.get("shopForm") == null ? null : object.getString("shopForm").trim();
			if(StringUtils.isEmpty(shopForm)) {
				shopFormList.remove(object);
			}
		}
		
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), shopFormList);
		result.put("total_results", shopFormList.size());
		return ServiceResponse.buildSuccess(result);
	}
	
	/**
	   *  查业态下门店
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse getCodeByShopForm(ServiceSession session, JSONObject paramsObject) {
		
		if (!paramsObject.containsKey("shopForms")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "shopForms"));
		}
		JSONArray shopForms = paramsObject.getJSONArray("shopForms");
		if(shopForms == null) 	shopForms = new JSONArray();
		if (shopForms.isEmpty()) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "请至少选一个业态!");
		}
		ParamValidateUtil.paramCheck(session, paramsObject);
		
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		paramsObject.put("entId", session.getEnt_id());
		// 查询门店业态
		List shopFormList = template.getSqlSessionTemplate().selectList("beanmapper.ShopModelMapper.getCodeByShopForm", paramsObject);
		if (shopFormList == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "门店业态查询失败！");
		}
		
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), shopFormList);
		result.put("total_results", shopFormList.size());
		return ServiceResponse.buildSuccess(result);
	}
	
	/**
	 * 查询业态门店树
	 */
	public ServiceResponse onShopFromTree(ServiceSession session, JSONObject paramsObject) {
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		paramsObject.put("entId", session.getEnt_id());
		List<Map<String, Object>> shopFromTree = new ArrayList<>();
		JSONObject allShopFromObject = new JSONObject();
		allShopFromObject.put("shopName", "全部业态");
		allShopFromObject.put("shopCode", 0);
		allShopFromObject.put("erpCode", session.getErpCode());
		allShopFromObject.put("entId", session.getEnt_id());
		allShopFromObject.put("level", -1);
		
		List<Map<String, Object>> shopFromList = new ArrayList<Map<String,Object>>();
		
		//查询所有业态
		List<Map<String, Object>> shopFormList = template.getSqlSessionTemplate().selectList("beanmapper.ShopModelMapper.selectALLshopFormDis", paramsObject);
		if(shopFormList != null && shopFormList.size() > 0) {
			for(Map<String, Object> map : shopFormList) {
				String shopFrom = (String) map.get("shopForm");
				map.put("shopName", shopFrom);
				map.put("shopCode", shopFrom);
				map.put("erpCode", session.getErpCode());
				map.put("entId", session.getEnt_id());
				map.put("level", 1);
				map.put("leafFlag", false);
				List<Map<String, Object>> shopList = template.getSqlSessionTemplate().selectList("beanmapper.ShopModelMapper.selectShopByShopFrom", map);
				map.put("children", shopList);
				shopFromList.add(map);
			}
		}
		allShopFromObject.put("children", shopFormList);
		shopFromTree.add(allShopFromObject);
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), shopFromTree);
		return ServiceResponse.buildSuccess(result);
	}


}
