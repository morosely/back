package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.GoodsModel;
import com.efuture.omdmain.model.GoodsShopRefModel;
import com.efuture.omdmain.model.ShopModel;
import com.efuture.omdmain.model.VenderModel;
import com.efuture.omdmain.service.GoodsShopRefService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.*;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** 
* @author yihaitao
* @time 2018年5月18日 上午11:16:14 
* 
*/
public class GoodsShopRefServiceImpl extends JDBCCompomentServiceImpl<GoodsShopRefModel> implements GoodsShopRefService{

	public GoodsShopRefServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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
	GoodsServiceImpl goodsService;
	@Autowired
	SaleGoodsServiceImpl saleGoodsService;
	
	//商品编码查询是否是DC仓
	@Override
	public ServiceResponse goodsCodeDC(ServiceSession session, JSONObject paramsObject) {
		//0.校验
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"goodsCode","erpCode","entId");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		//1.查询所有DC仓库
		Criteria criteria = Criteria.where("shopTypex").is(3);//'门店类型 0-普通店/1-中心店/2-小店、3-DC（用于门店补货）'
		Query query = new Query(criteria);
		List<ShopModel> shopDCList = this.getTemplate().select(query, ShopModel.class, "shop");
		if(shopDCList == null || shopDCList.isEmpty())
			return ServiceResponse.buildFailure(session,ResponseCode.Exception.SPECDATA_IS_EMPTY,"DC仓为空");
		//2.查询经营配置
		List<Long> dcIds = shopDCList.stream().map(ShopModel::getShopId).collect(Collectors.toList());
		JSONArray array = paramsObject.getJSONArray("goodsCode");
		criteria = Criteria.where("shopId").in(dcIds).and("goodsCode").in(array).and("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(paramsObject.get("entId"));
		query = new Query(criteria);
		List<GoodsShopRefModel> hasDCGoodsCodeList = this.getTemplate().select(query, GoodsShopRefModel.class,"goodsshopref");
		//数据处理 
		Map<Long,ShopModel> shopDCMap = null;
		Map<String,ShopModel> goodsCodeDCMap = null;
		if(hasDCGoodsCodeList!=null && !hasDCGoodsCodeList.isEmpty()){
			shopDCMap = new HashMap<Long,ShopModel>();
			for(ShopModel dc : shopDCList){
				shopDCMap.put(dc.getShopId(),dc);
			}
			goodsCodeDCMap = new HashMap<String,ShopModel>();
			for(GoodsShopRefModel dcref : hasDCGoodsCodeList){
				goodsCodeDCMap.put(dcref.getGoodsCode(),shopDCMap.get(dcref.getShopId()));
			}
		}
		//3.封装返回参数
		JSONArray resultArray = new JSONArray();
		for (Object goodsCode : array) {
			JSONObject json = new JSONObject();
			json.put("goodsCode",goodsCode);
			json.put("shopDC",goodsCodeDCMap==null?"":goodsCodeDCMap.get(goodsCode)==null?"":goodsCodeDCMap.get(goodsCode));
			resultArray.add(json);
		}
		return ServiceResponse.buildSuccess(resultArray);
	}
	
	
	/*
	* @Description: 经营配置插入 1 首先插入商品经营配置表  2 然后插入saleGoods表
	* @param session
	* @param paramsObject
	* @return: com.product.model.ServiceResponse
	*/
	@Override
	public ServiceResponse goodsInsert(ServiceSession session, JSONObject paramsObject) {
		List<JSONObject> shopList = (List<JSONObject>) paramsObject.get("shop");
		List<JSONObject> shopRefList = (List<JSONObject>) paramsObject.get("goodsShopRef");
		List<JSONObject> insertParamsList = new ArrayList();
		List<JSONObject> insertSaleGoods = new ArrayList();
		List<Long> sgidList = new ArrayList();
		for (JSONObject ttmp: shopRefList
			 ) {
			if(ttmp.containsKey("sgid") && ttmp.get("sgid") != null){
				sgidList.add(Long.parseLong(String.valueOf(ttmp.get("sgid"))));
			}
		}
		if(sgidList.size() < 0){
			return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "没有需要经营配置的商品");
		}
		JSONObject tmpGoodsParams = new JSONObject();
		tmpGoodsParams.put("sgid", sgidList);
		ServiceResponse tmpGoodsResponse = goodsService.onQuery(session, tmpGoodsParams);
		if(tmpGoodsResponse.getReturncode() != "0"){
			return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "没有需要经营配置的商品");
		}
		JSONObject tmpGoodsObject = (JSONObject) tmpGoodsResponse.getData();
		List<RowMap> tmpGoodsList = (List<RowMap>) tmpGoodsObject.get(goodsService.getCollectionName());
		for (JSONObject tmpShopObject:shopList
			 ) {
			if (!tmpShopObject.containsKey("shopCode") || !tmpShopObject.containsKey("shopId")){
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误,没有shopId和shopCode");
			}
			for (JSONObject tmpShopRef: shopRefList
				 ) {
				JSONObject tmpShopRef1 = (JSONObject) tmpShopRef.clone();
				if(!tmpShopRef1.containsKey("sgid") || !tmpShopRef1.containsKey("goodsCode")
					|| !tmpShopRef1.containsKey("venderCode")||!tmpShopRef1.containsKey("goodStatus")
						|| !tmpShopRef1.containsKey("price")){
					return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
				}
				long gsrid = UniqueID.getUniqueID(true);
				tmpShopRef1.put("gsrid", gsrid);
				tmpShopRef1.put("shopId", tmpShopObject.get("shopId"));
				tmpShopRef1.put("shopCode", tmpShopObject.get("shopCode"));
				tmpShopRef1.put("price", tmpShopRef.get("price"));
				insertParamsList.add(tmpShopRef1);

			}
			for(RowMap tmpMap:tmpGoodsList ){
				JSONObject tmpSaleGoodsObject = (JSONObject) new JSONObject(tmpMap).clone();
				long ssgid = UniqueID.getUniqueID(true);
				tmpSaleGoodsObject.put("ssgid",ssgid);
				tmpSaleGoodsObject.put("shopId", tmpShopObject.get("shopId"));
				tmpSaleGoodsObject.put("shopCode", tmpShopObject.get("shopCode"));
				insertSaleGoods.add(tmpSaleGoodsObject);
			}
		}
		JSONObject insertParamsObject = new JSONObject();
		JSONObject insertSaleGoodsParams = new JSONObject();
		insertSaleGoodsParams.put(saleGoodsService.getCollectionName(), insertSaleGoods);
		insertParamsObject.put("goodsShopRef", insertParamsList);
		if(onInsert(session, insertParamsObject).getReturncode() != "0"){
			return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "经营配置表插入错误");
		}
		return saleGoodsService.onInsert(session, insertSaleGoodsParams);
	}

	/*
	* @Description: 箱码设置删除 1 修改goods表中的status状态为0 2 然后修改saleGoods状态为0
	* @param session
    * @param paramsObject
	* @return: com.product.model.ServiceResponse
	*/
	@Override
	public ServiceResponse goodsDelete(ServiceSession session, JSONObject paramsObject) {
		if(!paramsObject.containsKey("goodsCode") || !paramsObject.containsKey("sgid")){
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
		}
		JSONObject goodsDeleteParams = new JSONObject();
		JSONObject querySaleGoodsParams = new JSONObject();
		goodsDeleteParams.put("goodsCode", paramsObject.get("goodsCode"));
		goodsDeleteParams.put("sgid", paramsObject.get("sgid"));
		goodsDeleteParams.put("status", 0); // 这里暂定删除为0
		querySaleGoodsParams.put("goodsCode", paramsObject.get("goodsCode"));
		querySaleGoodsParams.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "ssgid");
		ServiceResponse tmpSaleGoodsResponse = saleGoodsService.onQuery(session, querySaleGoodsParams);
		if(tmpSaleGoodsResponse.getReturncode() != "0"){
			return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询salegoods失败");
		}
		JSONObject tmpSaleGoodsObject = (JSONObject) tmpSaleGoodsResponse.getData();
		List<RowMap> tmpSaleGoodsMap = (List<RowMap>) tmpSaleGoodsObject.get(saleGoodsService.getCollectionName());
		List<JSONObject> tmpSaleDeleteList = new ArrayList();
		for (RowMap tmpMap: tmpSaleGoodsMap
			 ) {
			JSONObject tmpObject = (JSONObject) new JSONObject(tmpMap).clone();
			tmpObject.put("status", 0); // 这里暂时设置为0
			tmpSaleDeleteList.add(tmpObject);
		}

		if (goodsService.onUpdate(session, goodsDeleteParams).getReturncode() != "0"){
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "删除goods表失败");
		}
		if(tmpSaleDeleteList.size()>0){
			JSONObject deleteSaleGoodsObject = new JSONObject();
			deleteSaleGoodsObject.put(saleGoodsService.getCollectionName(), tmpSaleDeleteList);
			System.out.println(deleteSaleGoodsObject);
			return saleGoodsService.onUpdate(session, deleteSaleGoodsObject);
		}
		return ServiceResponse.buildSuccess("success");

	}

	/*
	* @Description: 这里需要判断是增加经营配置还是删除
	* @param session
    * @param paramsObject
	* @return: com.product.model.ServiceResponse
	*/
	@Override
	public ServiceResponse goodsSetting(ServiceSession session, JSONObject paramsObject) {
//		根据 goodsFlage 来判断是增加还是修改
		boolean goodsFlage = (boolean) paramsObject.get("goodsFlage");
		List<JSONObject> shopList = (List<JSONObject>) paramsObject.get("shop");
		List<JSONObject> goodsShopRefList = (List<JSONObject>) paramsObject.get("goodsShopRef");
		List<JSONObject> goodsShopRefParams = new ArrayList();
		List<JSONObject> saleGoodsParams = new ArrayList();

		if(goodsFlage){
//			则为修改， 先修改经营配置表， 再修改saleGoods表
			for (JSONObject shopObject: shopList
				 ) {
				if(!shopObject.containsKey("shopId") || !shopObject.containsKey("shopCode")){
					return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
				}
				for (JSONObject goodsShopRefObject:goodsShopRefList
					 ) {
					if(!goodsShopRefObject.containsKey("sgid") || !goodsShopRefObject.containsKey("salePrice") || !goodsShopRefObject.containsKey("goodsCode")){
						return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
					}
					JSONObject tmpGoodsRefObject = new JSONObject();
					JSONObject tmpSaleGoods = new JSONObject();
					tmpGoodsRefObject.put("shopId", shopObject.get("shopId"));
					tmpGoodsRefObject.put("shopCode", shopObject.get("shopCode"));
					tmpGoodsRefObject.put("goodsCode", goodsShopRefObject.get("goodsCode"));
					tmpGoodsRefObject.put("price", goodsShopRefObject.get("salePrice"));
					tmpSaleGoods.put("ssgid", goodsShopRefObject.get("ssgid"));
					tmpSaleGoods.put("salePrice", goodsShopRefObject.get("salePrice"));
					goodsShopRefParams.add(tmpGoodsRefObject);
					saleGoodsParams.add(tmpSaleGoods);
				}
			}
			if(goodsShopRefParams.size()==0 || saleGoodsParams.size()==0){
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");

			}
			FMybatisTemplate template = this.getTemplate();
			template.onSetContext(session);
			int rtData =  template.getSqlSessionTemplate().update("beanmapper.GoodsShopRefModelMapper.updateByGoods", goodsShopRefParams);
			if(rtData == 0){
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "经营配置中没有商品需要修改");

			}
			JSONObject saleGoodsShopRef = new JSONObject();
			saleGoodsShopRef.put(saleGoodsService.getCollectionName(), saleGoodsParams);
			return  saleGoodsService.onUpdate(session, saleGoodsShopRef);


		}else {
			return goodsInsert(session, paramsObject);
		}
	}
	
  /*
   * @Description: 经营配置查询
   * 
   * @param session
   * 
   * @param paramsObject
   * 
   * @return: com.product.model.ServiceResponse
   */
  @Override
  public ServiceResponse search(ServiceSession session, JSONObject paramsObject) {
    if (!paramsObject.containsKey("entId")) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY,
          "零售商ID不能为空");
    }
    if (!paramsObject.containsKey("sgid")) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY,
          "商品ID不能为空");
    }
    this.addSplitPageParams(paramsObject);
    ServiceResponse outRep = this.onQuery(session, paramsObject);
    
    FMybatisTemplate template = this.getTemplate();
    template.onSetContext(session);
    JSONObject outJSON = (JSONObject)outRep.getData();
    JSONArray array = outJSON.getJSONArray("goodsShopRef");
    List<Map> refList = array.parseArray(JSON.toJSONString(array), Map.class);
    JSONObject shopParams = new JSONObject();
    shopParams.put("entId", paramsObject.getString("entId"));
    List<Map<String, Object>> goodsShopRef = this.addShopInfo(session, shopParams, refList);
    
    ServiceResponse response = new ServiceResponse();
    outJSON.put("goodsShopRef", goodsShopRef);
    response.setData(goodsShopRef);
    response.setReturncode(ResponseCode.SUCCESS);
    return response;
  }

  private List<Map<String, Object>> addShopInfo(ServiceSession session, JSONObject paramsObject, List<Map> refList) {
    if(CollectionUtils.isEmpty(refList)) return new ArrayList<>();
    FMybatisTemplate template = this.getTemplate();
    template.onSetContext(session);
    List<ShopModel> shopList = template.getSqlSessionTemplate().selectList("beanmapper.ShopModelMapper.selectByState", paramsObject);
    
    List<Map<String, Object>> result = new ArrayList<>();
    for(Map<String, Object> r: refList) {
      for(ShopModel s: shopList) {
        if(r.get("shopId").equals(s.getShopId())) {
          r.put("shopName", s.getShopName());
        }
      }
      result.add(r);
    }
    
    return result;
  }
  
  private void addSplitPageParams(JSONObject paramsObject) {
    // 设置默认分页参数
    if (!paramsObject.containsKey("page_size")) {
      paramsObject.put("page_size", 10);
    }
    if (!paramsObject.containsKey("page_no")) {
      paramsObject.put("page_no", 0);
    } else {
      paramsObject.put("page_no",
          (paramsObject.getInteger("page_no") - 1) * paramsObject.getInteger("page_size"));
    }
  }
  
  //获取主营供应商
  public ServiceResponse searchMainSupplier(ServiceSession session, JSONObject paramsObject) {
    if(!paramsObject.containsKey("entId")) {
      return  ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "零售商ID不能为空");
    }
    if(!paramsObject.containsKey("sgid")) {
      return  ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "门店销售商品ID不能为空.");
    }
    
    FMybatisTemplate template = this.getTemplate();
    template.onSetContext(session);     
    List<Map<String, Object>> goodsShopRef = template.getSqlSessionTemplate().selectList("beanmapper.GoodsShopRefModelMapper.selectByState", paramsObject);
    if(CollectionUtils.isEmpty(goodsShopRef)) {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("goodsShopRef", new ArrayList<>());
      jsonObj.put("total_result", 0);
      return ServiceResponse.buildSuccess(jsonObj);
    }
    
    long total_result = goodsShopRef.size();
    List<Object> dcShopIds = new ArrayList<>();
    List<Object> venderCodes = new ArrayList<>();
    List<String> goodsCodes = new ArrayList<>();
    for(int i=0; goodsShopRef != null && i<goodsShopRef.size(); i++) {
      Map<String, Object> map = goodsShopRef.get(i);
      if(map.get("dcshopId") != null)
        dcShopIds.add(map.get("dcshopId"));
      if(map.get("venderCode") != null)
        venderCodes.add(map.get("venderCode"));
      if(map.get("goodsCode") != null)
        goodsCodes.add(map.get("goodsCode").toString());
      
      //添加默认值
      map.put("goodsName", "-");
      map.put("primeCost", 0);
      map.put("dcShopName", "-");
      map.put("venderStatus", 0);
      map.put("venderName", "-");
    }
    
    //添加商品信息
    if(CollectionUtils.isNotEmpty(goodsCodes)) {
      DefaultParametersUtils.removeRepeateParams4String(goodsCodes);
      paramsObject.put("goodsCodes", goodsCodes);
      List<GoodsModel> goodsList = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.selectIn", paramsObject);
      paramsObject.remove("goodsCodes");
      if(CollectionUtils.isNotEmpty(goodsList)) {
        for(Map<String, Object> f : goodsShopRef) {
          String goodsName = "-";
          String originArea = "-";
          BigDecimal primeCost = new BigDecimal("0");
          for(GoodsModel g:goodsList) {
            if(f.get("goodsCode") != null && f.get("goodsCode").toString().equals(g.getGoodsCode())) {
              goodsName = g.getGoodsName();
              primeCost = g.getPrimeCost();
              originArea = g.getOriginArea();
              break;
            }
          }
          f.put("originArea", originArea);
          f.put("goodsName", goodsName);
          f.put("primeCost", primeCost);
        }
      }
    }
    
    //添加配送中心
    if(CollectionUtils.isNotEmpty(dcShopIds)) {
      DefaultParametersUtils.removeRepeateParams4Object(dcShopIds);
      List<ShopModel> dsShopInfo = template.getSqlSessionTemplate().selectList("beanmapper.ShopModelMapper.selectIn",dcShopIds);
      for(Map<String, Object> map: goodsShopRef) {
        String dcShopName = map.get("shopName") == null ? "" : map.get("shopName").toString();
        if(map.get("logistics") != null && Long.parseLong(map.get("logistics").toString()) == 3) {
          for(ShopModel s: dsShopInfo) {
            if(s.getShopId().longValue() == Long.parseLong(String.valueOf(map.get("dcShopId")))) {
              dcShopName = s.getShopName();
              break;
            }
          }
        }
        map.put("dcShopName", dcShopName);
      }
    }

    //添加供应商信息
    List<VenderModel> vendorModelList = new ArrayList<>();
    if(CollectionUtils.isNotEmpty(venderCodes)) {
      DefaultParametersUtils.removeRepeateParams4Object(venderCodes);
      vendorModelList = template.getSqlSessionTemplate().selectList("beanmapper.VenderModelMapper.selectIn", venderCodes);
    }
    Map<String, VenderModel> vendorMap = new HashMap<>();
    for(int i=0; vendorModelList != null && i<vendorModelList.size(); i++) {
      VenderModel v = vendorModelList.get(i);
      vendorMap.put(v.getVenderCode(), v);
    }
    
    for(int i=0; goodsShopRef != null && i<goodsShopRef.size(); i++) {
      Map<String, Object> map = goodsShopRef.get(i);
      Integer venderStatus = null;
      String venderName = "";
      
      if(map.get("venderCode") != null) {
        VenderModel vm = vendorMap.get(map.get("venderCode").toString());
        if(vm != null) {
          venderStatus = vm.getVenderStatus();
          venderName = vm.getVenderName();
        }
      }
      map.put("venderStatus", venderStatus);
      map.put("venderName", venderName);
    }
    
    JSONObject jsonObj = new JSONObject();
    jsonObj.put("goodsShopRef", goodsShopRef);
    jsonObj.put("total_result", total_result);
    return ServiceResponse.buildSuccess(jsonObj);
  }
  
  //获取非主营供应商
  public ServiceResponse searchNotMainSupplier(ServiceSession session, JSONObject paramsObject) {
    if(!paramsObject.containsKey("entId")) {
      return  ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "零售商ID不能为空");
    }
    if(!paramsObject.containsKey("sgid")) {
      return  ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "门店销售商品ID不能为空.");
    }
    if(!paramsObject.containsKey("erpCode")) {
      return  ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "经营公司编码不能为空.");
    }
    
    FMybatisTemplate template = this.getTemplate();
    template.onSetContext(session);
    List<Map<String, Object>> goodsShopRef = template.getSqlSessionTemplate().selectList("beanmapper.GoodsShopRefModelMapper.selectByState", paramsObject);
    if(CollectionUtils.isEmpty(goodsShopRef)) {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("goodsShopRef", new ArrayList<>());
      jsonObj.put("total_result", 0);
      return ServiceResponse.buildSuccess(jsonObj);
    }
    
    long total_result = goodsShopRef.size();
    List<Object> dcShopIds = new ArrayList<>();
    List<Object> venderCodes = new ArrayList<>();
    List<String> goodsCodes = new ArrayList<>();
    for(int i=0; goodsShopRef != null && i<goodsShopRef.size(); i++) {
      Map<String, Object> map = goodsShopRef.get(i);
      if(map.get("dcshopId") != null)
        dcShopIds.add(map.get("dcshopId"));
      if(map.get("venderCode") != null)
        venderCodes.add(map.get("venderCode"));
      if(map.get("goodsCode") != null)
        goodsCodes.add(map.get("goodsCode").toString());
      
      //添加默认值
      map.put("dcShopName", "-");
      map.put("goodsName", "-");
      map.put("venderStatus", 0);
      map.put("venderName", "-");
    }
    
    //添加配送中心
    List<ShopModel> dsShopInfo = new ArrayList<>();
    if(CollectionUtils.isNotEmpty(dcShopIds)) {
      DefaultParametersUtils.removeRepeateParams4Object(dcShopIds);
      dsShopInfo = template.getSqlSessionTemplate().selectList("beanmapper.ShopModelMapper.selectIn",dcShopIds);
      for(Map<String, Object> map: goodsShopRef) {
        String dcShopName = map.get("shopName") == null ? "" : map.get("shopName").toString();
        if(map.get("logistics") != null && Long.parseLong(map.get("logistics").toString()) == 3) {
          for(ShopModel s: dsShopInfo) {
            if(s.getShopId().longValue() == Long.parseLong(String.valueOf(map.get("dcShopId")))) {
              dcShopName = s.getShopName();
              break;
            }
          }
        }
        map.put("dcShopName", dcShopName);
      }
    }

    //添加商品信息
    if(CollectionUtils.isNotEmpty(goodsCodes)) {
      DefaultParametersUtils.removeRepeateParams4String(goodsCodes);
      paramsObject.put("goodsCodes", goodsCodes);
      List<GoodsModel> goodsList = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.selectIn", paramsObject);
      paramsObject.remove("goodsCodes");
      if(CollectionUtils.isNotEmpty(goodsList)) {
        for(Map<String, Object> f : goodsShopRef) {
          String goodsName = "-";
          BigDecimal primeCost = new BigDecimal("0.00");
          String originArea = "-";
          for(GoodsModel g:goodsList) {
            if(f.get("goodsCode") != null && f.get("goodsCode").toString().equals(g.getGoodsCode())) {
              goodsName = g.getGoodsName();
              primeCost = g.getPrimeCost();
              originArea = g.getOriginArea();
              break;
            }
          }
          f.put("originArea", originArea);
          f.put("primeCost", primeCost);
          f.put("goodsName", goodsName);
        }
      }
    }
    
    //添加供应商部分
    for(int i=0; goodsShopRef != null && i<goodsShopRef.size(); i++) {
      Map<String, Object> map = goodsShopRef.get(i);
      Integer venderStatus = 0;
      String venderName = "-";
      
      if(map.get("venderCode") != null) {
        JSONObject p = new JSONObject();
        p.put("entId", paramsObject.get("entId"));
        p.put("shopId", map.get("shopId"));
        p.put("goodsCode", map.get("goodsCode"));
        List<Map<String, Object>> vm = template.getSqlSessionTemplate().selectList("beanmapper.GoodsVenderRefModelMapper.selectInfo", p);
        if(CollectionUtils.isNotEmpty(vm)) {
          if(vm.get(0).get("venderStatus") != null)
            venderStatus = Integer.parseInt(vm.get(0).get("venderStatus").toString());
          if(vm.get(0).get("venderName") != null)
            venderName = vm.get(0).get("venderName").toString();
        }
      }
      map.put("venderStatus", venderStatus);
      map.put("venderName", venderName);
    }
    
    JSONObject jsonObj = new JSONObject();
    jsonObj.put("goodsShopRef", goodsShopRef);
    jsonObj.put("total_result", total_result);
    return ServiceResponse.buildSuccess(jsonObj);
  }
  
  //销售范围
  public ServiceResponse saleArea(ServiceSession session, JSONObject paramsObject) {
    if(!paramsObject.containsKey("entId")) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "零售商ID不能为空.");
    }
    if(!paramsObject.containsKey("sgid")) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "商品ID不能为空.");
    }
    if(!paramsObject.containsKey("goodsType")) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "商品类型不能为空.");
    }
    
    FMybatisTemplate template = this.getTemplate();
    template.onSetContext(session);
    this.addSplitPageParams(paramsObject);
    List<Map<String, Object>> ref = template.getSqlSessionTemplate().selectList("beanmapper.GoodsShopRefModelMapper.selectByState", paramsObject);
    List<Long> shopIds = new ArrayList<>();
    List<Map<String, Object>> goodsList = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.search4Like", paramsObject);
    
    for(int i = 0; ref != null && i < ref.size(); i++) {
      Map<String, Object> r = ref.get(i);
      String goodsName = "-";
      String originArea = "-";
      if(CollectionUtils.isNotEmpty(goodsList)) {
        if(goodsList.get(0).get("goodsName") != null) goodsName = goodsList.get(0).get("goodsName").toString();
        if(goodsList.get(0).get("originArea") != null) originArea = goodsList.get(0).get("originArea").toString();
      }
      r.put("goodsName", goodsName);
      r.put("originArea", originArea);
      Long shopId = Long.parseLong(r.get("shopId").toString());
      shopIds.add(shopId);
    }
    /** (优化)注释重复代码 上面的SQL已经返回shopName，无需再处理shopName--add by yihaitao 2022-11-15
    //添加商店名称
    List<ShopModel> shop = null;
    if(!CollectionUtils.isEmpty(shopIds)) {
      shop = template.getSqlSessionTemplate().selectList("beanmapper.ShopModelMapper.selectIn",shopIds);
    }
    for(int i = 0; ref != null && i < ref.size(); i++) {
      Map<String, Object> r = ref.get(i);
      for(int j = 0; shop != null && j < shop.size(); j++) {
        ShopModel s = shop.get(j);
        long shopId = Long.parseLong(r.get("shopId").toString());
        if(shopId == s.getShopId().longValue()) {
          r.put("shopName", s.getShopName());
        }
      }
    }
     */

    
    long total_result = template.getSqlSessionTemplate().selectOne("beanmapper.GoodsShopRefModelMapper.selectByStateCount", paramsObject);
    JSONObject data = new JSONObject();
    data.put("goodsShopRef", ref);
    data.put("total_result", total_result);
    return ServiceResponse.buildSuccess(data);
  }
  
  
  //查商品税率
  public ServiceResponse getGoodsTaxRate(ServiceSession session, JSONObject paramsObject) {
	  ServiceResponse resp = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "entId","erpCode","shopId","goodsIds");
	  if(!ResponseCode.SUCCESS.equals(resp.getReturncode())) {
		  return resp;
	  }
	  FMybatisTemplate template = this.getTemplate();
	  template.onSetContext(session);
	  List<Map<String,Object>> list = template.getSqlSessionTemplate().selectList("beanmapper.GoodsShopRefModelMapper.selectGoodsTaxRate",paramsObject);
	  if(null == list) {
		  return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"查无数据");
	  }
	  JSONObject json = new JSONObject();
	  JSONArray array = new JSONArray();
	  for(Map<String,Object> map : list) {
		  JSONObject obj = new JSONObject();
		  obj.putAll(map);
		  if(null == map.get("inputTax")) {
			  obj.put("inputTax", 0);
		  }
		  array.add(obj);
	  }
	  json.put(this.getCollectionName(), array);
	  return ServiceResponse.buildSuccess(json);
  }
  
}
