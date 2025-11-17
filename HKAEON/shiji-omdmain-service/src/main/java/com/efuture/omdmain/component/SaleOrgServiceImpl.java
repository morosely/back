package com.efuture.omdmain.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.efuture.omdmain.utils.CommonUtils;
import com.product.component.AuthorityBaseServiceImpl;

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
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.SaleOrgModel;
import com.efuture.omdmain.model.SaleOrgTreeBean;
import com.efuture.omdmain.model.ShopModel;
import com.efuture.omdmain.service.SaleOrgService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.BeanConstant;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.ParamValidateUtil;

public class SaleOrgServiceImpl extends JDBCCompomentServiceImpl<SaleOrgModel> implements SaleOrgService{

	@Autowired
	AuthorityBaseServiceImpl authorityBaseService;
	
	@Autowired
	private SaleOrgRedisServiceImpl saleOrgRedisServiceImpl;
	
	private static final Logger logger = LoggerFactory.getLogger(SaleOrgServiceImpl.class);
	public SaleOrgServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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
	
	// 查柜组最末级--权限使用
	public ServiceResponse searchByLeafFlag(ServiceSession session, JSONObject paramsObject) throws Exception {

		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		if (!paramsObject.containsKey("leafFlag")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "leafFlag"));
		}

		if (!paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_PAGENO)) {
			paramsObject.put(BeanConstant.QueryField.PARAMKEY_PAGENO,1);
		}
		if (!paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_PAGESIZE)) {
			paramsObject.put(BeanConstant.QueryField.PARAMKEY_PAGESIZE,1000);
		}
		
		return this.onQuery(session, paramsObject);
	}

	// orgCode逐级查柜组---pos专用
	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) {
		
		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject, "orgCode");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
//		if (!paramsObject.containsKey("orgCode")) {
//			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "orgCode"));
//		}
//		if (!paramsObject.containsKey("erpCode")) {
//			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "erpCode"));
//		}
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		
		this.getTemplate().onSetContext(session);
		Long entId = session.getEnt_id();
//		Criteria criteria = Criteria.where("entId").is(entId).and("parentCode").is(paramsObject.get("orgCode")).and("erpCode").is(paramsObject.get("erpCode"));
		Criteria criteria = Criteria.where("entId").is(entId).and("parentCode").is(paramsObject.get("orgCode"));
		Query query = new Query(criteria);
		List<SaleOrgModel> list = this.getTemplate().select(query, SaleOrgModel.class, "saleorg");
		paramsObject.clear();
		paramsObject.put("saleorg",list);
		return ServiceResponse.buildSuccess(paramsObject);
	}
	
	// erpCode 和  orgCode逐级查柜组---pos专用
	public ServiceResponse query(ServiceSession session, JSONObject paramsObject) {
		
		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject, "erpCode", "orgCode");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
//		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		paramsObject.put("entId",session.getEnt_id());
		paramsObject.put("parentCode",paramsObject.getString("orgCode"));
		
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		List saleorgList = template.getSqlSessionTemplate().selectList("beanmapper.SaleOrgModelMapper.getSaleOrgList", paramsObject);
		paramsObject.clear();
		paramsObject.put("saleorgList",saleorgList);
		return ServiceResponse.buildSuccess(paramsObject);
	}
	
	@Autowired
	private ShopServiceImpl shopServiceImpl;
	
	/**
	 *  封装返回--柜组树形结构展示
	 *  门店柜组树-由经营公司和门店编码来查询
	 * @param session
	 * @param paramsObject
	 * @return
	 * @throws Exception
	 */
	public ServiceResponse getSaleOrgTree(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (!paramsObject.containsKey("erpCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "erpCode"));
		}
		if (!paramsObject.containsKey("shopCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "shopCode"));
		}
		
		// 校验门店存在与否
		ServiceResponse shopQuery = shopServiceImpl.onQuery(session, paramsObject);
		JSONObject shopData = (JSONObject) shopQuery.getData();
		JSONArray shopList = shopData.getJSONArray(shopServiceImpl.getCollectionName());
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
		Criteria criteria = Criteria.where("entId").is(entId).and("erpCode").is(paramsObject.get("erpCode")).and("shopCode").is(paramsObject.get("shopCode"));// 查询当前选中shopId
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
			System.out.println(e.getMessage());
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
		if(isAuth)
			allSaleOrgs = shopServiceImpl.getAuthSaleOrg(session, allSaleOrgs);
//		List<SaleOrgTreeBean> newAllSaleOrgs = new ArrayList<>();// 存可显示的柜组信息
//
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
		return shopServiceImpl.getsaleOrgTree(session, paramsObject, allSaleOrgs);
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
	
	// 从顶级节点开始递归构建树--（按parentId递归）
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

	
	/**
	 * 组织机构查询
	 */
	public ServiceResponse onSaleOrgTree(ServiceSession session, JSONObject paramsObject) throws Exception {
		List<SaleOrgTreeBean> saleOrgTree = saleOrgTree(session, paramsObject);
		paramsObject.clear();
		paramsObject.put("saleOrg", saleOrgTree);
		return ServiceResponse.buildSuccess(paramsObject);
	}

	// 展示所有门店信息 （树形结构）
	public List<SaleOrgTreeBean> saleOrgTree(ServiceSession session, JSONObject paramsObject) {
		// 1.查询所有门店数据
		Long entId = session.getEnt_id();
		Criteria criteria = Criteria.where("entId").is(entId);
		Query query = new Query(criteria);
		this.getTemplate().onSetContext(session);
		Field fields = query.fields();
		fields.include("saleOrgId,orgCode,orgName,orgSName,shopId,parentId,parentCode,level,leafFlag,erpCode");
		boolean isAuth = false; // 默认不走权限控制
		String tableName =  "saleorg";
		List<String>orgIds = null;
		try {
			orgIds = authorityBaseService.getDataRangeOrg(session);
		}catch (Exception e){
			System.out.println(e.getMessage());
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
//		在这里 做处理
		if(isAuth)
			allSaleOrgs = shopServiceImpl.getAuthSaleOrg(session, allSaleOrgs);
		CommonUtils<SaleOrgTreeBean> commonUtils = new CommonUtils<>();
		HashMap<Long, List<SaleOrgTreeBean>> pidSaleOrgTree = commonUtils.getPidEnty(allSaleOrgs);
//		柜组 要去掉第一级
		List<SaleOrgTreeBean> topTree = pidSaleOrgTree.get(-1l);
		if(topTree == null || topTree.size() == 0) return new ArrayList<>();
		System.out.println("开始递归----" + new Date().getTime());
//		这里调用门店中的方法buildSaleOrgTree1
		shopServiceImpl.buildSaleOrgTree1(topTree, pidSaleOrgTree);//从顶级节点开始往下递归
		System.out.println("结束递归----" + new Date().getTime());
		return topTree;
	}

	// 从顶级节点开始递归构建树
	private List<SaleOrgTreeBean> buildTree(List<SaleOrgTreeBean> root, List<SaleOrgTreeBean> allSaleOrgs) {
		for (SaleOrgTreeBean saleOrg : root) {
			Long parentId = saleOrg.getSaleOrgId();
			String parentName = saleOrg.getOrgName();
			List<SaleOrgTreeBean> children = findChildren(parentId, parentName, allSaleOrgs);
			buildTree(children, allSaleOrgs);
			saleOrg.setChildren(children);
		}
		return root;
	}

	// 查询该节点的所有儿子节点
	private List<SaleOrgTreeBean> findChildren(Long parentId, String parentName, List<SaleOrgTreeBean> allSaleOrgs) {
		List<SaleOrgTreeBean> childrenSaleOrg = new ArrayList<SaleOrgTreeBean>();
		for (SaleOrgTreeBean saleOrg : allSaleOrgs) {
			if (parentId.equals(saleOrg.getParentId())) {
				saleOrg.setParentName(parentName);// 增加父亲节点名称
				childrenSaleOrg.add(saleOrg);
			}
		}
		return childrenSaleOrg;
	}
	
	 public ServiceResponse sychSaleOrg(ServiceSession session,JSONObject jsonParam) {
		 this.logger.info("同步【柜组】Start----->>> param:"+jsonParam);
		// 参数转换
		SaleOrgModel saleOrg = JSON.toJavaObject(jsonParam, SaleOrgModel.class);
		if(saleOrg==null){
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY,"传入参数格式不对");
		}
		// 查询条件
		try {
			Criteria criteria = Criteria.where("entId").is(jsonParam.getLong("entId")).and("erpCode").is(jsonParam.getString("erpCode")).and("status").is(1);
			Query query = new Query(criteria);
			query.limit(Integer.MAX_VALUE);
			List<SaleOrgModel> newOrgList = this.onFind(session, this.getTemplate(), query, SaleOrgModel.class, this.getCollectionName());
			List<Long> newOrgIdList = newOrgList.stream().map(s->s.getSaleOrgId()).collect(Collectors.toList());
			List<SaleOrgModel> oldOrgList = saleOrgRedisServiceImpl.querySaleOrg(session, jsonParam);
			List<Long> oldOrgIdList = oldOrgList.stream().map(s->s.getSaleOrgId()).collect(Collectors.toList());
			oldOrgIdList.removeAll(newOrgIdList);
			if(oldOrgIdList!=null&&oldOrgIdList.size()>0){
				saleOrgRedisServiceImpl.deleteSaleOrg(session, oldOrgIdList);
			}
			JSONArray array = new JSONArray();
			for(SaleOrgModel orgModel : newOrgList){
				JSONObject json = new JSONObject();
				json.put("saleOrgId", orgModel.getSaleOrgId());
				json.put("entId", orgModel.getEntId());
				json.put("erpCode", orgModel.getErpCode());
				json.put("orgCode", orgModel.getOrgCode());
				json.put("parentCode", orgModel.getParentCode());
				json.put("parentId", orgModel.getParentId());
				json.put("shopId", orgModel.getShopId());
				array.add(json);
			}
			saleOrgRedisServiceImpl.insertSaleOrg(session, array);
			return ServiceResponse.buildSuccess("同步成功");
			// 在这里加入业务处理
		} catch (Exception ex) {
			logger.error(String.format("signInNoEnt->%1$s -> %2$s","返回登录异常",ex.getMessage()));
			return ServiceResponse.buildFailure(session,ex.getMessage());
		}
	 }
}
