package com.efuture.omdmain.component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.common.ParamsFormat;
import com.efuture.omdmain.model.GoodsDescModel;
import com.efuture.omdmain.model.GoodsModel;
import com.efuture.omdmain.model.SaleGoodsModel;
import com.efuture.omdmain.service.SaleGoodsService2;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.BeanConstant;
import com.product.model.ResponseCode;
import com.product.model.RowMap;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.ParamValidateUtil;
import com.product.util.UniqueID;

import static com.efuture.omdmain.utils.JSONSerializeUtill.toNameJSONByKey;
import static com.product.util.TypeUtils.castToJavaBean;

/**
 * Created by huangzhengwei on 2018/5/7.
 *
 * @Desciption:
 */
public class SaleGoodsService2Impl extends JDBCCompomentServiceImpl<SaleGoodsModel> implements SaleGoodsService2 {
    private static final Logger logger = LoggerFactory.getLogger(SaleGoodsService2Impl.class);
    private static Map<String, Object> config = new HashMap<>();
    
    static{
        config.put("goodsSpecPrice", "saleGoods");
        config.put("goodsMoreBarCode", "saleGoods");
        };
    public SaleGoodsService2Impl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
        super(mybatisTemplate,collectionName, keyfieldName);
    }

    @Override
    protected DBObject onBeforeRowInsert(Query query, Update update) {
        return this.onDefaultRowInsert(query, update);
    }

//    @Override
//    protected FMybatisTemplate getTemplate() {
//        return this.getBean("StorageOperation", FMybatisTemplate.class);
//    }

    public boolean vaildateExists(ServiceSession session, JSONObject paramsObject){
        ServiceResponse ssData = onQuery(session, paramsObject);
        JSONObject tmpData = (JSONObject) ssData.getData();
        List<RowMap> mapList = (List<RowMap>) tmpData.get(getCollectionName());
        if(mapList.size() == 0){
            return false;
        }
        return true;
    }

    /*
    * @Description: 商品步长查询，查询saleGoods表，并且
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    @Override
    public ServiceResponse goodsStepSizeSearch(ServiceSession session, JSONObject paramsObject) {
        List<String> mustField = Arrays.asList("shopId", "erpCode", "shopId");
        List<String> removeField = new ArrayList<>();
        for (String fields: mustField
                ) {
            if(!paramsObject.containsKey(fields)){
                logger.info(String.format( "参数必须包括: %s", fields));
                return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, String.format( "参数必须包括: %s", fields));
            }
        }
//    去掉 参数中有可能为空的值
        for (String key: paramsObject.keySet()
                ) {
            if(paramsObject.get(key) == null){
                removeField.add(key);
                continue;
            }
            if(String.class.isInstance(paramsObject.get(key)) &&  String.valueOf(paramsObject.get(key)).length() == 0){
                removeField.add(key);
                continue;
            }
            if(List.class.isInstance(paramsObject.get(key)) &&  ((List)paramsObject.get(key)).size() == 0){
                removeField.add(key);
                continue;
            }
        }
        for (String removekey: removeField
                ) {
            paramsObject.remove(removekey);
        }
        long count = 0;
        paramsObject = DefaultParametersUtils.transformParam(session, paramsObject, false, false);
        FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        List<Map>list = template.getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.goodsStepSizeSearch", paramsObject);
        if(list != null || list.size() > 0){
            count = template.getSqlSessionTemplate().selectOne("beanmapper.SaleGoodsModelMapper.goodsStepSizeSearchCount", paramsObject);
        }
        JSONObject resultObject = new JSONObject();
        resultObject.put(this.getCollectionName(), list);
        resultObject.put("total_results", count);
        return ServiceResponse.buildSuccess(resultObject);
    }

    /*
    * @Description: 步长商品设置修改
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    @Override
    public ServiceResponse goodsStepSizeUpdate(ServiceSession session, JSONObject paramsObject) {
        List<JSONObject> paramsList = (List<JSONObject>) paramsObject.get("data");
        if (paramsList.size() == 0){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "没有需要修改的值");
        }
        JSONObject tmpParam = new JSONObject();
        for (JSONObject paramObject: paramsList
                ) {
            if (!paramObject.containsKey("sgid")){
                return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数中没有需要修改的id");

            }
            tmpParam.put("sgid", paramObject.get("sgid"));
            if (!vaildateExists(session, tmpParam)){
                return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "数据库中已经不存在该记录，请刷新页面后再操作");

            }

        }
        return onUpdate(session, paramsObject) ;
    }

    @Override
    public ServiceResponse goodsStepSizeAdd(ServiceSession session, JSONObject paramsObject) {
        if(!paramsObject.containsKey("goodsCode") || !paramsObject.containsKey("skuCode")){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
        }
        return onInsert(session, paramsObject);
    }

    /*
    * @Description: 查询商品列表
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    @Override
    public ServiceResponse goodsListSearch(ServiceSession session, JSONObject paramsObject) {
        ArrayList<String> paramList = new ArrayList();
        paramList.add("goodsName");
        paramsObject = ParamsFormat.formatLike(paramList, paramsObject);
        return onQuery(session, paramsObject);
    }


    /*
    * @Description: 根据母品的id ， 商品类型查询商品明细
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    @Override
    public ServiceResponse goodsDetailsSearch(ServiceSession session, JSONObject paramsObject) {
        if(!paramsObject.containsKey("sgid") || !paramsObject.containsKey("goodsType")){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
        }
        JSONObject tmpQueryParams = new JSONObject();
        tmpQueryParams.put("ssgid", paramsObject.get("sgid"));
        tmpQueryParams.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "goodsCode,shopId");
        ServiceResponse tmpResponse = onQuery(session, tmpQueryParams);
        if(tmpResponse.getReturncode() != "0"){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询错误");
        }
        JSONObject tmpdata = (JSONObject) tmpResponse.getData();
        List<RowMap> tmpList = (List<RowMap>) tmpdata.get(this.getCollectionName());
        if(tmpList.size() == 0){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "商品可能已经被删除了，请刷新页面");

        }
        paramsObject.remove("sgid");
        paramsObject.put("parentGoodsCode", tmpList.get(0).get("goodsCode"));
        paramsObject.put("shopId", tmpList.get(0).get("shopId"));
        return onQuery(session, paramsObject);
    }

    @Autowired
    GoodsShopRefServiceImpl goodsShopRefService;
    /*
    * @Description: 通过母品增加
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    @Override
    public ServiceResponse goodsDetailAdd(ServiceSession session, JSONObject paramsObject) {

        List<JSONObject> paramsList = (List<JSONObject>) paramsObject.get("saleGoods");
        if(paramsList.size()==0){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "没有需要数据更新");
        }
        JSONObject queryParams = new JSONObject();
        queryParams.put("ssgid", paramsList.get(0).get("sgid"));
        ServiceResponse tmpResponse = onQuery(session, queryParams);
        List<JSONObject> tmpInsertList = new ArrayList();
        List<JSONObject> shopRefList = new ArrayList();
        if(tmpResponse.getReturncode() != "0"){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询商品表失败");
        }
        JSONObject tmpData = (JSONObject) tmpResponse.getData();
        List<RowMap> tmpList = (List<RowMap>) tmpData.get(this.getCollectionName());
        if(tmpList.size() == 0){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "母品不存在，请重新刷新页面");
        }
        for (JSONObject paramObject: paramsList
                ) {
            JSONObject insertParamObject = (JSONObject) new JSONObject(tmpList.get(0)).clone();
            if(!paramObject.containsKey("goodsCode") || !paramObject.containsKey("goodsType")){
                return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
            }
            insertParamObject.put("parentGoodsCode", insertParamObject.get("goodsCode"));
            for (String key:paramObject.keySet()
                    ) {
                if(key == "sgid"){
                    continue;
                }
                insertParamObject.put(key, paramObject.get(key));
            }
            //            先插入goods表
            Long sgid = UniqueID.getUniqueID(true);
            insertParamObject.put("sgid", sgid);
//            零售价格设为参考价

            Long ssgid = UniqueID.getUniqueID(true);
            insertParamObject.put("ssgid", ssgid);
            insertParamObject.put("refPrice", insertParamObject.get("salePrice"));
//            设置经营配置的参数
            JSONObject shopRefParams = new JSONObject();
            long gsrid = UniqueID.getUniqueID(true);
            shopRefParams.put("gsrid", gsrid);
            shopRefParams.put("goodsCode", insertParamObject.get("goodsCode"));
            shopRefParams.put("shopId", insertParamObject.get("shopId"));
            shopRefParams.put("shopCode", insertParamObject.get("shopCode"));
            shopRefParams.put("normalPrice", insertParamObject.get("salePrice"));
            tmpInsertList.add(insertParamObject);
            shopRefList.add(shopRefParams);
        }
        JSONObject tmpSaleGoodsInsertObject = new JSONObject();
        JSONObject tmpGoodsInsertObject = new JSONObject();
        JSONObject tmpShopRefInsertObject = new JSONObject();
        tmpSaleGoodsInsertObject.put(this.getCollectionName(), tmpInsertList);
        tmpGoodsInsertObject.put(goodsService.getCollectionName(), tmpInsertList);
        tmpShopRefInsertObject.put(goodsShopRefService.getCollectionName(), tmpInsertList);
        goodsService.onInsert(session, tmpGoodsInsertObject);
        goodsShopRefService.onInsert(session, tmpShopRefInsertObject);
//        onInsert(session, tmpSaleGoodsInsertObject)
        return onInsert(session, tmpSaleGoodsInsertObject);
    }

    /*
    * @Description: 批量修改 首先需要修改 goods， goodsShopRef， saleGoods
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    @Override
    public ServiceResponse goodsDetailUpdate(ServiceSession session, JSONObject paramsObject) {
        List<JSONObject> paramsList = (List<JSONObject>) paramsObject.get("saleGoods");
        List<JSONObject> goodsUpdateList = new ArrayList();
        List<JSONObject> goodsShopRef = new ArrayList();
        JSONObject paramTmp = new JSONObject();
        for (JSONObject paramObject: paramsList
                ) {
            if(!paramObject.containsKey("ssgid")||!paramObject.containsKey("goodsCode")){
                return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
            }
            long ssgid = Long.parseLong(String.valueOf(paramObject.get("ssgid")));
            ServiceResponse tmpResponse = querySgidBySssgid(session, ssgid,"sgid,shopId,goodsCode");
            if(tmpResponse.getReturncode() != "0"){
                return tmpResponse;
            }
            JSONObject tmpData = (JSONObject) tmpResponse.getData();
            List<RowMap> tmpMap = (List<RowMap>) tmpData.get(this.getCollectionName());
            if(!tmpMap.get(0).containsKey("sgid")||!tmpMap.get(0).containsKey("shopId")||!tmpMap.get(0).containsKey("goodsCode")){
                return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "商品可能被删除，请刷新页面重试");
            }
            JSONObject tmpGoodsObject = new JSONObject();
            tmpGoodsObject.put("sgid",tmpMap.get(0).get("sgid"));
            for (String key: paramObject.keySet()
                    ) {
                tmpGoodsObject.put(key, paramObject.get(key));
            }
            tmpGoodsObject.put("refPrice", tmpGoodsObject.get("salePrice"));
//            经营配置参数
            JSONObject tmpShopRef = new JSONObject();
            tmpShopRef.put("goodsCode", tmpMap.get(0).get("goodsCode"));
            tmpShopRef.put("shopId", tmpMap.get(0).get("shopId"));
            tmpShopRef.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "gsrid,goodsCode");
            ServiceResponse shopRefResponse = goodsShopRefService.onQuery(session, tmpShopRef);
            if(shopRefResponse.getReturncode() != "0"){
                return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询经营配置错误");
            }
            JSONObject shopRefData = (JSONObject) shopRefResponse.getData();
            List<RowMap> shopRefList = (List<RowMap>) shopRefData.get(goodsShopRefService.getCollectionName());
            if(shopRefList.size() == 0 || !shopRefList.get(0).containsKey("gsrid")){
                return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "经营配置数据不存在");
            }
            JSONObject tmpGoodsShopRefObject = new JSONObject();
            tmpGoodsShopRefObject.put("gsrid", shopRefList.get(0).get("gsrid"));
            tmpGoodsShopRefObject.put("goodsCode", paramObject.get("goodsCode"));
            tmpGoodsShopRefObject.put("normalPrice", paramObject.get("salePrice"));
            paramTmp.put("ssgid", paramObject.get("ssgid"));
            goodsUpdateList.add(tmpGoodsObject);
            goodsShopRef.add(tmpGoodsShopRefObject);
        }
        JSONObject goodsParams = new JSONObject();
        JSONObject shopRefParams = new JSONObject();
        goodsParams.put(goodsService.getCollectionName(), goodsUpdateList);
        shopRefParams.put(goodsShopRefService.getCollectionName(), goodsShopRef);
        goodsService.onUpdate(session, goodsParams);
        goodsShopRefService.onUpdate(session, shopRefParams);
        return onUpdate(session, paramsObject);
    }

    /*
    * @Description: 商品分割， 生鲜等级设置， 删除
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    @Override
    public ServiceResponse goodsDetaildelete(ServiceSession session, JSONObject paramsObject) {
        if(!paramsObject.containsKey("ssgid") || !paramsObject.containsKey("status")){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
        }
        long ssgid = Long.parseLong(String.valueOf(paramsObject.get("ssgid")));

        ServiceResponse tmpResponse = querySgidBySssgid(session, ssgid,"sgid");
        if(tmpResponse.getReturncode() != "0"){
            return tmpResponse;
        }
        JSONObject tmpData = (JSONObject) tmpResponse.getData();
        List<RowMap> tmpMap = (List<RowMap>) tmpData.get(this.getCollectionName());
        if(!tmpMap.get(0).containsKey("sgid")){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "商品可能被删除，请刷新页面重试");
        }
        JSONObject updateGoodsParams = new JSONObject();
        updateGoodsParams.put("sgid", tmpMap.get(0).get("sgid"));
        updateGoodsParams.put("status", paramsObject.get("status"));
        goodsService.onUpdate(session, updateGoodsParams);
        return onUpdate(session, paramsObject);
    }

    /*
    * @Description: 通过 ssgid 查询sgid
    * @param session
    * @param ssgid
    * @return: com.alibaba.fastjson.JSONObject
    */
    public ServiceResponse querySgidBySssgid(ServiceSession session, long ssgid, String fields){
        JSONObject tmpQuery = new JSONObject();
        tmpQuery.put("ssgid", ssgid);
        tmpQuery.put(BeanConstant.QueryField.PARAMKEY_FIELDS, fields);
        ServiceResponse tmpResponse = onQuery(session, tmpQuery);
        if(tmpResponse.getReturncode() != "0"){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询异常");
        }
        JSONObject tmpData = (JSONObject) tmpResponse.getData();
        List<RowMap> tmpMap = (List<RowMap>) tmpData.get(this.getCollectionName());
        if(tmpMap.size() == 0){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "商品可能被删除，请刷新页面重试");
        }
        return tmpResponse;
    }

    @Autowired
    SaleGoodsImageRefServiceImpl saleGoodsImageRefService;
    /*
    * @Description: 根据id 查询 商品信息，以及母品查询出子品
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    public ServiceResponse channelGoodsGet1(ServiceSession session, JSONObject paramsObject) {
//      1 先查询出母品的信息
        if (!paramsObject.containsKey("sgid") || !paramsObject.containsKey("parentGoodsCode")){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
//             return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "");
        }
        JSONObject queryParamsObject = new JSONObject();
        queryParamsObject.put("sgid", paramsObject.get("sgid"));
        queryParamsObject.put("status", 1);
        ServiceResponse ssData =  goodsService.onQuery(session, queryParamsObject);

//      查询母码对应的所有子码商品 待确认
        JSONObject skuParams = new JSONObject();
        skuParams.put("parentGoodsCode", paramsObject.get("parentGoodsCode"));
        skuParams.put("status", 1);
        skuParams.put(BeanConstant.QueryField.PARAMKEY_FIELDS,"goodsCode,goodsName,barNo,refPrice,salePrice,goodsFromCode,goodsType,sgid");
        ServiceResponse skuData = goodsService.onQuery(session, skuParams);
        if(skuData.getReturncode() !="0"){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询错误" + skuData.getData());
        }
        List tmpSgidList = new ArrayList();
        tmpSgidList.add(paramsObject.get("sgid"));
        JSONObject tmpSkuData = (JSONObject) skuData.getData();
        List<RowMap> tmpSkuList = (List<RowMap>) tmpSkuData.get(goodsService.getCollectionName());
        for (RowMap tmpSkuMap:tmpSkuList) {
            tmpSgidList.add(tmpSkuMap.get("sgid"));
        }
        JSONObject tmpImageQuery = new JSONObject();
        tmpImageQuery.put("sgid", tmpSgidList);
        tmpImageQuery.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "sgid,imageUrl,showTerm,imageType");
        ServiceResponse saleGoodsImageRef =  saleGoodsImageRefService.onQuery(session,tmpImageQuery);
        if(saleGoodsImageRef.getReturncode() != "0"){
            return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询图片错误");
        }
        
        //添加商品描述
        FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        tmpImageQuery.put("entId", session.getEnt_id());
        List<GoodsDescModel> goodsDesc = template.getSqlSessionTemplate().selectList("beanmapper.GoodsDescModelMapper.selectInSGID", tmpImageQuery);
        for(RowMap map: tmpSkuList) {
          for(GoodsDescModel m: goodsDesc) {
            if(Long.parseLong(map.get("sgid").toString()) == m.getSgid() && 0 == m.getShowTerm()) {
              map.put("goodsDescPc", m.getGoodsDesc());
            }else if(Long.parseLong(map.get("sgid").toString()) == m.getSgid() && 1 == m.getShowTerm()) {
              map.put("goodsDesc", m.getGoodsDesc()); 
            }
          }
        }
        
        JSONObject ssObject = (JSONObject) ssData.getData();
        DefaultParametersUtils.numberFormat(tmpSkuList, "#0.00", "salePrice");
        JSONArray goodsArray = ssObject.getJSONArray("goods");
        DefaultParametersUtils.numberFormat(goodsArray, "#0.00", "salePrice");
        
        ssObject.put("skuList", tmpSkuList);
        ssObject.put("goods", goodsArray);
        
        JSONObject saleGoodsImageRefObject = (JSONObject) saleGoodsImageRef.getData();
        List<RowMap> saleGoodsImageRefMap = (List<RowMap>) saleGoodsImageRefObject.get(saleGoodsImageRefService.getCollectionName());
        ssObject.put("saleGoodsImageRef", saleGoodsImageRefMap);
        return ssData;
    }
    
    // 渠道商品资料详情查询
    public ServiceResponse channelGoodsGet2(ServiceSession session, JSONObject paramsObject) {
		
    	if (session == null) {
   			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
   		}
   		if (!paramsObject.containsKey("sgid")) {
   			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "sgid"));
   		}
   		if (!paramsObject.containsKey("parentGoodsCode")) {
   			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "parentGoodsCode"));
   		}
   		
   		// 1 先查询出普通商品的信息
   		JSONObject queryParamsObject = new JSONObject();
   		queryParamsObject.put("sgid", paramsObject.get("sgid"));
   		ServiceResponse goodsQuery = goodsService.onQuery(session, queryParamsObject);
   		JSONObject goodsData = (JSONObject) goodsQuery.getData();
   		JSONArray goodsList = goodsData.getJSONArray(goodsService.getCollectionName());
   		DefaultParametersUtils.numberFormat(goodsList, "#0.00", "salePrice");
   		DefaultParametersUtils.numberFormat(goodsList, "#0.00", "primeCost");
		DefaultParametersUtils.numberFormat(goodsList, "#0.00", "refPrice");
		DefaultParametersUtils.numberFormat(goodsList, "#0.00", "memberPrice");
		DefaultParametersUtils.numberFormat(goodsList, "#0.00", "minSalePrice");
   		JSONObject goodsModel = (JSONObject) goodsList.get(0);
	
		// 查询母码对应的所有子码商品
		JSONObject skuParams = new JSONObject();
		skuParams.put("parentGoodsCode", goodsModel.getString("goodsCode"));
	
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		// 设置默认分页参数
		if (!paramsObject.containsKey("page_no")) {
			skuParams.put("page_no", 0);
		} else {
			skuParams.put("page_no",(paramsObject.getInteger("page_no") - 1) * paramsObject.getInteger("page_size"));
		}
		if (!paramsObject.containsKey("page_size")) {
			skuParams.put("page_size", 10);
		}
		
		List sonGoodsList = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.getGoodsByParentGoodsCode", skuParams);
		long total_results = sonGoodsList.size();
		
		JSONObject skuList = new JSONObject();
		skuList.put(goodsService.getCollectionName(), sonGoodsList);
		skuList.put("skuList_total_results", total_results);
		
		JSONArray skuData = skuList.getJSONArray(goodsService.getCollectionName());
		
		// 查母品的图片资料
		JSONObject tmpImageQuery = new JSONObject();
		tmpImageQuery.put("sgid", paramsObject.get("sgid"));
		tmpImageQuery.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "sgid,imageUrl,showTerm,imageType");
		ServiceResponse saleGoodsImageRef = saleGoodsImageRefService.onQuery(session, tmpImageQuery);
		
		goodsData.put("skuList", skuList);
		
		JSONObject saleGoodsImageRefObject = (JSONObject) saleGoodsImageRef.getData();
		JSONArray saleGoodsImageRefMap = saleGoodsImageRefObject.getJSONArray(saleGoodsImageRefService.getCollectionName());
		goodsData.put("saleGoodsImageRef", saleGoodsImageRefMap);
		
		return goodsQuery;
	}
    
    // 查SKU商品 带分页
    public ServiceResponse getSkuGoodsByParentGoodsCode(ServiceSession session, JSONObject paramsObject) throws Exception {
   		
    	if (session == null) {
   			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
   		}
   		if (!paramsObject.containsKey("parentGoodsCode")) {
   			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "parentGoodsCode"));
   		}
   		
   		// 查询出SKU商品的信息
   		paramsObject.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "sgid,goodsCode,goodsName,barNo,salePrice,goodsType,erpCode,erpName,goodsStatus");
   		ServiceResponse skuQuery = goodsService.onQuery(session, paramsObject);
   		JSONObject skuData = (JSONObject) skuQuery.getData();
   		JSONArray skuGoodsList = skuData.getJSONArray(goodsService.getCollectionName());
   		
   		long total_results1 = skuData.getLong("total_results"); // 总数
   		
		if (skuGoodsList.size() > 0) {
			DefaultParametersUtils.numberFormat(skuGoodsList, "#0.00", "salePrice");
		}
   		// 子品信息
   		skuData.put(goodsService.getCollectionName(), skuGoodsList);  // SKU商品
//   		long total_results = skuGoodsList.size();
   		return skuQuery;
   	}
    
    // 文描来源
    public ServiceResponse channelGoodsGetWM(ServiceSession session, JSONObject paramsObject) throws Exception {
   		
    	if (session == null) {
   			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
   		}
   		if (!paramsObject.containsKey("sgid")) {
   			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "sgid"));
   		}
   		
   		// 1 先查出母品
   		JSONObject queryGoods = new JSONObject();
   		queryGoods.put("sgid", paramsObject.get("sgid"));
   		queryGoods.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "sgid,goodsType,erpCode,goodsName,goodsCode,parentGoodsCode");
   		ServiceResponse goodsQuery = goodsService.onQuery(session, queryGoods);
   		JSONObject goodsData = (JSONObject) goodsQuery.getData();
   		JSONArray goodsList = goodsData.getJSONArray(goodsService.getCollectionName());
   		
   		// 2 查询母码对应的所有子码商品
   		JSONObject goodsModel = (JSONObject) goodsList.get(0);
   	
   		Long entId = session.getEnt_id();
   		String parentGoodsCode = goodsModel.getString("goodsCode");
   		String erpCode = goodsModel.getString("erpCode");
   		Criteria criteria = Criteria.where("erpCode").is(erpCode).and("entId").is(entId).and("parentGoodsCode").is(parentGoodsCode);
		Query query = new Query(criteria);
		this.getTemplate().onSetContext(session);
		Field fields = query.fields();
		fields.include("sgid,goodsType,erpCode,goodsName,goodsCode,parentGoodsCode");
		List<GoodsModel>  skuGoodsList = this.getTemplate().select(query, GoodsModel.class, "goods");
   		
   		goodsData.put("skuList", skuGoodsList); // SKU商品
   		
        List tmpSgidList = new ArrayList();
        tmpSgidList.add(paramsObject.get("sgid"));
		for (int i = 0; i < skuGoodsList.size(); i++) {
			GoodsModel skuGoods = skuGoodsList.get(i);
			tmpSgidList.add(skuGoods.getSgid());
		}
		 
		JSONObject tmpWMQuery = new JSONObject();
		tmpWMQuery.put("sgid", tmpSgidList);
		if (!paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_PAGENO)) {
			tmpWMQuery.put(BeanConstant.QueryField.PARAMKEY_PAGENO,1);
		}
		if (!paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_PAGESIZE)) {
			tmpWMQuery.put(BeanConstant.QueryField.PARAMKEY_PAGESIZE,1000);
		}
		
	    ServiceResponse goodsDescQuery =  goodsDescService.onQuery(session,tmpWMQuery);
	    JSONObject goodsDescData = (JSONObject) goodsDescQuery.getData();
   		JSONArray goodsDescList = goodsDescData.getJSONArray(goodsDescService.getCollectionName());
   		
   		tmpSgidList = new ArrayList();
   		tmpWMQuery.remove("sgid");
		
   		for (int j = 0; j < goodsDescList.size(); j++) {
   			JSONObject json = (JSONObject) goodsDescList.get(j);
   			tmpSgidList.add(json.get("sgid"));
   		}
   		
		if (goodsDescList.size() == 0) {
			tmpWMQuery.put("sgid", "-1");
		} else {
			tmpWMQuery.put("sgid", tmpSgidList);
		}
//   		tmpWMQuery.put("order_field", "sgid");
//   		tmpWMQuery.put("order_direction", "asc");
   		tmpWMQuery.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "sgid,goodsType,erpCode,goodsName,goodsCode,parentGoodsCode");
   		ServiceResponse wmQuery = goodsService.onQuery(session, tmpWMQuery);
   		
   		return wmQuery;
   	}

    // 渠道商品资料详情查询
    public ServiceResponse channelGoodsGet(ServiceSession session, JSONObject paramsObject) throws Exception {
   		
    	if (session == null) {
   			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
   		}
   		if (!paramsObject.containsKey("sgid")) {
   			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "sgid"));
   		}
   		if (!paramsObject.containsKey("parentGoodsCode")) {
   			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "parentGoodsCode"));
   		}
   		
   		// 1 先查询出普通商品的信息
   		JSONObject queryParamsObject = new JSONObject();
   		queryParamsObject.put("sgid", paramsObject.get("sgid"));
   		ServiceResponse goodsQuery = goodsService.onQuery(session, queryParamsObject);
   		JSONObject goodsData = (JSONObject) goodsQuery.getData();
   		JSONArray goodsList = goodsData.getJSONArray(goodsService.getCollectionName());
   		DefaultParametersUtils.numberFormat(goodsList, "#0.00", "salePrice");
   		DefaultParametersUtils.numberFormat(goodsList, "#0.00", "primeCost");
		DefaultParametersUtils.numberFormat(goodsList, "#0.00", "refPrice");
		DefaultParametersUtils.numberFormat(goodsList, "#0.00", "memberPrice");
		DefaultParametersUtils.numberFormat(goodsList, "#0.00", "minSalePrice");
   		JSONObject goodsModel = (JSONObject) goodsList.get(0);
   	
   		// 2 查询母码对应的所有子码商品
//   	JSONObject skuParams = new JSONObject();
//   	skuParams.put("parentGoodsCode", goodsModel.getString("goodsCode"));
//   	skuParams.put("erpCode", goodsModel.getString("erpCode"));
//   	ServiceResponse skuQuery = goodsService.onQuery(session, skuParams);
//   	JSONObject skuData = (JSONObject) skuQuery.getData();
//   	JSONArray skuGoodsList = skuData.getJSONArray(goodsService.getCollectionName());
//   	DefaultParametersUtils.numberFormat(skuGoodsList, "#0.00", "salePrice");
//   		
//   	long total_results1 = skuData.getLong("total_results"); // 总数
//   	long total_results = skuGoodsList.size();
//   	JSONObject skuList = new JSONObject();
//		skuList.put(goodsService.getCollectionName(), skuGoodsList);
//		skuList.put("skuList_total_results", total_results1);
   		
   		// 3 查母品的图片资料和文描信息
   		JSONObject tmpImageQuery = new JSONObject();
   		tmpImageQuery.put("sgid", paramsObject.get("sgid"));
   		ServiceResponse imageAndGoodsDesc = goodsService.getImageAndGoodsDesc(session, tmpImageQuery);
   		JSONObject imageAndGDJson = (JSONObject) imageAndGoodsDesc.getData();
   		
   		JSONArray imageList = imageAndGDJson.getJSONArray(saleGoodsImageRefService.getCollectionName());
   		JSONArray goodsDescList = imageAndGDJson.getJSONArray(goodsDescService.getCollectionName());
   	
   		goodsData.put(goodsService.getCollectionName(), goodsList);// 商品信息
//   	goodsData.put("skuList", skuList); // SKU商品
   		goodsData.put(saleGoodsImageRefService.getCollectionName(), imageList); // 母品图片
   		goodsData.put(goodsDescService.getCollectionName(), goodsDescList); // 母品文描
   		
   		return goodsQuery;
   	}

    /*
    * @Description:  需要更新goods saleGoods
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    public ServiceResponse channelGoodsUpdate1(ServiceSession session, JSONObject paramsObject) {
        List <JSONObject> paramsList = (List<JSONObject>) paramsObject.get("saleGoods");
        List<JSONObject> descList = new ArrayList();
        JSONObject tmpImageObject = new JSONObject();
        Date date=new Date();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for (JSONObject paramObject: paramsList) {
            paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
            if (paramObject.containsKey("image")){
                tmpImageObject.put(String.valueOf(paramObject.get("sgid")), paramObject.get("image"));
                paramObject.remove("image");
            }
            if(paramObject.containsKey("goodsDesc") || paramObject.containsKey("goodsDescPc")||paramObject.containsKey("goodsDisplayName")){
                paramObject.put("updateDate", df.format(date));
                paramObject = DefaultParametersUtils.transformParam(session, paramObject, true, false);
                descList.add(paramObject);
            }
        }
        JSONObject tmpDescObject = new JSONObject();
        tmpDescObject.put("goods", descList);
        saleGoodsImageRefService.update(session, tmpImageObject);
        goodsService.onUpdate(session, tmpDescObject);
        FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.channelGoodsUpdate",descList);
        return ServiceResponse.buildSuccess("success");
    }

    @Autowired
   	private SaleGoodsServiceImpl saleGoodsServiceImpl;
    @Autowired
    private GoodsDescServiceImpl goodsDescService;
    
    // 编辑渠道商品--更新goods saleGoods和子品   以及文描图片信息
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse channelGoodsUpdate(ServiceSession session, JSONObject paramsObject) throws Exception {
    	
    	// 参数校验
		ParamValidateUtil.paramCheck(session, paramsObject, "sgid");
		if (!paramsObject.containsKey(goodsDescService.getCollectionName())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "goodsdesc"));
		}
		if (!paramsObject.containsKey("saleGoodsImageRef")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "saleGoodsImageRef"));
		}
		
		//统一插入时间：保持多个表的插入时间是一致性
		String updateDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Map<String,Object> returnMap = new HashMap<>();// 记录返回信息
		
		JSONObject updateGoods = new JSONObject();
		updateGoods.put("sgid", paramsObject.getString("sgid"));
		updateGoods.put("goodsDisplayName", paramsObject.getString("goodsDisplayName"));
		updateGoods.put("updateDate", updateDateString);
		updateGoods.put("modifier", session.getUser_name());
		goodsService.setUpsert(false);
		ServiceResponse goodsResult = goodsService.onUpdate(session, updateGoods);
		if (!ResponseCode.SUCCESS.equals(goodsResult.getReturncode())) {
			throw new Exception(goodsResult.getData().toString());
		}
		returnMap.put(goodsService.getCollectionName(), goodsResult.getData());
		
		// 更新saleGoods
		Long entId = session.getEnt_id();
		Criteria criteria1 = Criteria.where("entId").is(entId).and("sgid").is(paramsObject.getString("sgid"));
		Query query1 = new Query(criteria1);
		Field fields = query1.fields();
		fields.include("ssgid");
		List<SaleGoodsModel> saleGoodsList = this.getTemplate().select(query1, SaleGoodsModel.class, "saleGoods");
		// 收集查到的ssgid
		List<Long> ssgidList = new ArrayList<>();
		if (saleGoodsList != null && !saleGoodsList.isEmpty()) {
			ssgidList = saleGoodsList.stream().map(SaleGoodsModel::getSsgid).collect(Collectors.toList());
		}
		if (ssgidList.size() > 0) {
			updateGoods = new JSONObject();
			updateGoods.put("goodsDisplayName", paramsObject.getString("goodsDisplayName"));
			updateGoods.put("updateDate", updateDateString);
			updateGoods.put("ssgid", ssgidList);
			this.getTemplate().getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.updateChannelGoods",updateGoods);
		}

//		List<SaleGoodsModel> descList = new ArrayList();
//		for (int i = 0; i < saleGoodsList.size(); i++) {
//			SaleGoodsModel saleGoodsparam = saleGoodsList.get(i);
//			saleGoodsparam.setEntId(entId);
//			saleGoodsparam.setSgid(paramsObject.getLong("sgid"));
//			saleGoodsparam.setGoodsDisplayName(paramsObject.getString("goodsDisplayName"));
//			saleGoodsparam.setUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(updateDateString));
//			descList.add(saleGoodsparam);
//		}

//		if (descList.size() > 0) {
//			FMybatisTemplate template = this.getTemplate();
//			template.onSetContext(session);
//			template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.channelGoodsUpdate", descList);
//		}
        
        // 修改文描
        JSONArray gdList = paramsObject.getJSONArray(goodsDescService.getCollectionName());
        if (gdList.size() > 0) {
        	int temp1 = gdList.size();
        	for (int i = 0; i < temp1; i++) {
        		JSONObject goodsDesc = (JSONObject) gdList.get(i);
        		ServiceResponse goodsDescResult = goodsDescService.saveBySgid(session, goodsDesc);
        		if(!ResponseCode.SUCCESS.equals(goodsDescResult.getReturncode())) {
//					  return goodsDescResult;
					  throw new Exception(goodsDescResult.getData().toString());
				}
        	}
        }
        
        Object sgid1 = paramsObject.get("sgid");
		// 更新图片
		List <JSONObject> imageList = (List<JSONObject>) paramsObject.get("saleGoodsImageRef");
		// 2.1 删除以前图片
		paramsObject.clear();
		paramsObject.put("sgid", sgid1);
		saleGoodsImageRefService.onDelete(session, paramsObject);
		if (imageList.size() > 0) {
			
			JSONObject saleGoodsImageRef = (JSONObject) imageList.get(0);
			Object sgid = saleGoodsImageRef.get("sgid");
			
//			paramsObject.clear();
//			paramsObject.put("sgid", sgid);
//			saleGoodsImageRefService.onDelete(session, paramsObject);
			//2.2增加图片
			paramsObject.clear();
			for (JSONObject jsonObject : imageList) {
				jsonObject.put("sgid", sgid);
			}
			paramsObject.put(saleGoodsImageRefService.getCollectionName(), imageList);
			ServiceResponse imageResult = saleGoodsImageRefService.onInsert(session, paramsObject);
			if (!ResponseCode.SUCCESS.equals(imageResult.getReturncode())) {
				throw new Exception(imageResult.getData().toString());
			}
		}
        return ServiceResponse.buildSuccess("success");
    }

    /*
    * @Description:  根据商品类型删除商品
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    @Override
    public ServiceResponse goodsDetailsDelete(ServiceSession session, JSONObject paramsObject) {
        if(!paramsObject.containsKey("sgid") || !paramsObject.containsKey("goodsType")){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
        }
        List tmpSgidList = (List) paramsObject.get("sgid");
        if(tmpSgidList.size() < 1){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "没有需要删除的商品");
        }
        List<String> sgidList = new ArrayList();
        for (Object tmpSgid:tmpSgidList
                ) {
            String sgid = String.valueOf(tmpSgid);
            sgidList.add(sgid);
        }
        paramsObject.put("sgid", sgidList);


        return onDelete(session, paramsObject);
    }

    @Autowired
    GoodsServiceImpl goodsService;
    /*
    * @Description: 箱码经营配置-详细查询
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    @Override
    public ServiceResponse goodsSettingSearch(ServiceSession session, JSONObject paramsObject) {

        String saleGoodsFields = "ssgid,goodsCode,goodsName,partsNum,salePrice,barNo,goodsStatus,vid,venderCode,goodsStatus,shopId,shopCode,sgid,createDate";
        String goodsFields = "sgid,goodsCode,goodsName,partsNum,salePrice,barNo,goodsStatus,vid,venderCode,createDate";
        if(!paramsObject.containsKey("status")){
            paramsObject.put("status", 1);
        }
        paramsObject.put(BeanConstant.QueryField.PARAMKEY_FIELDS, saleGoodsFields);
        ServiceResponse ssResponse =  onQuery(session, paramsObject);
        if(ssResponse.getReturncode() != "0"){
            return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询销售商品表失败");
        }
        JSONObject ssData = (JSONObject) ssResponse.getData();
        List<RowMap> ssList = (List<RowMap>) ssData.get(this.getCollectionName());
        if(ssList.size() >0){
            ssData.put("goodsFlage", true);
            return ssResponse;
        }
        paramsObject.remove("shopId");
        paramsObject.put(BeanConstant.QueryField.PARAMKEY_FIELDS, goodsFields);
        ServiceResponse rtResponse =  goodsService.onQuery(session, paramsObject);
        if(rtResponse.getReturncode() != "0"){
            return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询商品表失败");
        }
        JSONObject rtData = (JSONObject) rtResponse.getData();
        List<RowMap> rtList = (List<RowMap>) rtData.get(goodsService.getCollectionName());
//        if(rtList.size() == 0 ){
//            return
//            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "请先进行箱码设置");
//        }
        for (RowMap tmpMap: rtList
                ) {
            tmpMap.put("goodsStatus", 1); //这里暂时设置为1 不可售卖
            ssList.add(tmpMap);
        }
        ssData.put("goodsFlage", false);
        return ssResponse;
    }

    //    合代码的时候这里需要删掉
    @Autowired
    SaleGoodsServiceImpl saleGoodsService;
    /*
    * @Description: 子品查询
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    @Override
    public ServiceResponse subGoodsSearch(ServiceSession session, JSONObject paramsObject) {
        if(!paramsObject.containsKey("shopId")){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误，必须包含shopId");
        }
        if(!paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_FIELDS)){
            paramsObject.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "goodsCode,goodsName");
        }
        List tmpRmKey = new ArrayList();
        for (String formatKey : paramsObject.keySet()
                ) {
            if (paramsObject.get(formatKey) == null || String.valueOf(paramsObject.get(formatKey)).length() < 1) {
                tmpRmKey.add(formatKey);
            }
        }
        for (Object rmKey : tmpRmKey
                ) {
            paramsObject.remove(rmKey);
        }
        paramsObject = DefaultParametersUtils.transformParam(session, paramsObject, false, false);
        FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        List result =  template.getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.subGoodsSearch", paramsObject);
        long count = 0;
        if (result != null && result.size() > 0) {
            count = template.getSqlSessionTemplate()
                    .selectOne("beanmapper.SaleGoodsModelMapper.subGoodsSearchCount", paramsObject);
        }
        JSONObject rtResult = new JSONObject();
        rtResult.put("saleGoods", result);
        rtResult.put("total_results", count);
        //        template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.channelGoodsUpdate",descList);


        ServiceResponse ssResponse =  ServiceResponse.buildSuccess(rtResult);
        saleGoodsService.formatShopValue(ssResponse, session);
        return ssResponse;

    }

    /*
    * @Description: 子品经营配置
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    @Override
    public ServiceResponse subGoodsSetting(ServiceSession session, JSONObject paramsObject) {
        return null;
    }


    @Override
    public ServiceResponse searchAllFgsp(ServiceSession session, JSONObject paramsObject) {
  	  FMybatisTemplate template = this.getTemplate();
  	  ServiceResponse response = super.onQuery(session, paramsObject);
  	  List<SaleGoodsModel> fgspList = null;
  	  if(response.getReturncode() != null && response.getReturncode().equals("0")){
  		  JSONObject data =  JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
  		  List<SaleGoodsModel> saleGoodsList = JSONArray.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), SaleGoodsModel.class);
  		  List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
  		  for(SaleGoodsModel saleGoods : saleGoodsList){
  			  Map<String, Object> saleGoodsMap = new HashMap<String,Object>();//套餐表头
  			  saleGoodsMap.put("sgid", saleGoods.getSgid());
  			  saleGoodsMap.put("barNo", saleGoods.getBarNo());
  			  saleGoodsMap.put("goodsCode", saleGoods.getGoodsCode());
  			  saleGoodsMap.put("goodsName", saleGoods.getGoodsName());
  			  saleGoodsMap.put("categoryId", saleGoods.getCategoryId());
  			  saleGoodsMap.put("salePrice", saleGoods.getSalePrice());
  			  saleGoodsMap.put("shopId", saleGoods.getShopId());
  			  saleGoodsMap.put("shopCode",saleGoods.getShopCode());
  			  resultList.add(saleGoodsMap);
  			  
  			  Criteria criteria = Criteria.where("goodsType").is(7).
  					  and("shopId").is(saleGoods.getShopId()).
  					  and("parentGoodsCode").is(saleGoods.getGoodsCode());
  			  Query query = new Query(criteria);
  			  query.with(new Sort(new Order(Direction.DESC,"createDate")));  
  			  fgspList = template.select(query, SaleGoodsModel.class, "salegoods");
  			  
  			  for(SaleGoodsModel fgsp: fgspList) {
  	    			saleGoodsMap = new HashMap<String,Object>();//分割商品
  	    			saleGoodsMap.put("fgspBarNo", fgsp.getBarNo());//分割商品条码
  	    			saleGoodsMap.put("fgspGoodsCode", fgsp.getGoodsCode());//分割商品编码
  	    			saleGoodsMap.put("fgspGoodsName", fgsp.getGoodsName());//分割名称
  	    			saleGoodsMap.put("fgspPartsNum", fgsp.getPartsNum());//分割包装含量
  	    			saleGoodsMap.put("fgspSalePrice", fgsp.getSalePrice());//分割参考价
  	    			saleGoodsMap.put("fgspSaleUnit", fgsp.getSaleUnit());//单位
  	    			saleGoodsMap.put("fgspSsgid", fgsp.getSsgid());//分割商品ID
  	    			saleGoodsMap.put("fgspShopId", fgsp.getShopId());
  	    			saleGoodsMap.put("fgspShopCode", fgsp.getShopCode());
  		    		resultList.add(saleGoodsMap);
  			  }
  		  }
  		  JSONObject result = new JSONObject();
  		  result.put(this.getCollectionName(), resultList);
  		  result.put("total_results", data.get("total_results"));
  		  return ServiceResponse.buildSuccess(result);
  		  
  	  }
  	  return response;
    }


    @Override
    public ServiceResponse searchAllXzsp(ServiceSession session, JSONObject paramsObject) throws Exception {
    	  FMybatisTemplate template = this.getTemplate();
    	  //boolean hasShopNameField = false;
//    	  if(paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_FIELDS)){
//			  String fields = paramsObject.getString(BeanConstant.QueryField.PARAMKEY_FIELDS);
////			  if (fields.indexOf("shopName") != -1){
////				  hasShopNameField = true;
////				  String newFields = removeFieldsByName(fields, "shopName");
////				  paramsObject.put(BeanConstant.QueryField.PARAMKEY_FIELDS, newFields);
////			  }
//    	  }
    	  ServiceResponse response = saleGoodsServiceImpl.search(session, paramsObject);
    	  List<Map<String,Object>> xzspList = null;
    	  if(response.getReturncode() != null && response.getReturncode().equals("0")){
    		  JSONObject data =  JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
    		  List<SaleGoodsModel> saleGoodsList = JSONArray.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), SaleGoodsModel.class);
    		  List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
    		  for(SaleGoodsModel saleGoods : saleGoodsList){
    			  Map<String, Object> saleGoodsMap = new HashMap<String,Object>();
    			  saleGoodsMap.put("sgid", saleGoods.getSgid());
    			  saleGoodsMap.put("barNo", saleGoods.getBarNo());
    			  saleGoodsMap.put("goodsCode", saleGoods.getGoodsCode());
    			  saleGoodsMap.put("goodsName", saleGoods.getGoodsName());
    			  saleGoodsMap.put("categoryId", saleGoods.getCategoryId());
    			  saleGoodsMap.put("salePrice", saleGoods.getSalePrice());
    			  saleGoodsMap.put("shopId", saleGoods.getShopId());
	    		  saleGoodsMap.put("shopCode", saleGoods.getShopCode());
	    		  saleGoodsMap.put("shopName", saleGoods.getShopName());
	    		  saleGoodsMap.put("erpCode", saleGoods.getErpCode());
	    		  saleGoodsMap.put("saleUnit", saleGoods.getSaleUnit());
//	    		  if(hasShopNameField){
//					Criteria criteria = Criteria.where("shopId").is(saleGoods.getShopId());
//					Query query = new Query(criteria);
//					ShopModel shop = template.selectOne(query, ShopModel.class, "shop");
//					saleGoodsMap.put("shopName", shop.getShopName());
//	    		  }
    			  resultList.add(saleGoodsMap);
    			  
//    			  Criteria criteria = Criteria.where("goodsType").is(8).
//    					  and("parentGoodsCode").is(saleGoods.getGoodsCode());
//    			  Query query = new Query(criteria);
//    			  query.with(new Sort(new Order(Direction.DESC,"createDate")));
//    			  query.limit(Integer.MAX_VALUE);
    			// xzspList = template.select(query, SaleGoodsModel.class, "salegoods");
    			  JSONObject paramObj = new JSONObject();
    			  paramObj.put("erpCode", saleGoods.getErpCode());
    			  paramObj.put("goodsCode", saleGoods.getGoodsCode());
    			  paramObj.put("goodsStatus", 1);
    			  paramObj.put("goodsType", 8);
    			  paramObj.put("mainBarcodeFlag", 0);
    			  paramObj.put("shopId", saleGoods.getShopId());
    			  paramObj.put("order_direction", "desc");
    			  paramObj.put("order_field", "updateDate");
    			  ServiceResponse moreCodeGoodsDetailResponse = moreCodeGoodsDetailSearch(session, paramObj);
    			  
    			  if(moreCodeGoodsDetailResponse.getReturncode() != null && moreCodeGoodsDetailResponse.getReturncode().equals("0")){
    				  JSONObject jsonData =(JSONObject)moreCodeGoodsDetailResponse.getData();
    				  xzspList = (List<Map<String,Object>>)jsonData.get(this.getCollectionName());
	    			  for(Map<String,Object> xzsp: xzspList) {
						saleGoodsMap = new HashMap<String,Object>();//箱装商品
						saleGoodsMap.put("xzspBarNo", xzsp.get("barNo"));//箱装商品条码
						saleGoodsMap.put("xzspGoodsCode", xzsp.get("goodsCode"));//箱装商品编码
						saleGoodsMap.put("xzspGoodsName", xzsp.get("goodsName"));//箱装名称
						saleGoodsMap.put("xzspPartsNum", xzsp.get("partsNum"));//箱装包装含量
						saleGoodsMap.put("xzspSalePrice", xzsp.get("salePrice"));//箱装参考价
						saleGoodsMap.put("xzspSsgid", xzsp.get("ssgid"));//箱装商品ID
						saleGoodsMap.put("xzspShopId", xzsp.get("shopId"));
						saleGoodsMap.put("xzspShopCode", xzsp.get("shopCode"));
						resultList.add(saleGoodsMap);
	    			  }
    			  }
    		  }
    		  JSONObject result = new JSONObject();
    		  result.put(this.getCollectionName(), resultList);
    		  result.put("total_results", data.get("total_results"));
    		  return ServiceResponse.buildSuccess(result);
    		  
    	  }
    	  return response;
      }

    @Autowired GoodsMoreBarCodeServiceImpl goodsMoreBarCodeService;
    @Autowired GoodsSpecPriceServiceImpl goodsSpecPriceService;
    /*
    * @Description: plu 商品保存
    * 1  首先保存商品多条码表 goodsMoreBarCode
    * 2  保存商品多条码价格表 goodsSpecPrice
    * 3  保存saleGoods 记录
    * @param session
    * @param paramsObject {"psgid":"1111", "saleGoods":[{"barNo":"", "goodsType":"" "salePrice":""}]}
    * @return: com.product.model.ServiceResponse
    */
    @Transactional(propagation = Propagation.REQUIRED)
    public ServiceResponse moreCodeGoodsSave(ServiceSession session, JSONObject paramsObject) {
        List<String> mustKey = Arrays.asList("psgid", "saleGoods");
        List<String> mustFields = Arrays.asList("barNo", "goodsType", "salePrice");
        for (String tmpKey: mustKey
             ) {
            if(!paramsObject.containsKey(tmpKey)){
                logger.info(String.format( "参数必须包括: %s", tmpKey));
                return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, String.format( "参数必须包括: %s", tmpKey));
            }
        }
        List<JSONObject> paramsObjectList = (List<JSONObject>) paramsObject.get(this.getCollectionName());
        List<JSONObject> goodsMoreBarCodeList = new ArrayList<>();
        List<JSONObject> goodsSpecPriceList = new ArrayList<>();
        List<JSONObject> saleGoodsList = new ArrayList<>();
        JSONObject queryObject = new JSONObject();
        String psgid = String.valueOf(paramsObject.get("psgid"));
        queryObject.put("ssgid", psgid);
        ServiceResponse presponse = onQuery(session, queryObject);
        if(!presponse.getReturncode().equals("0")){
            logger.info(String.format( "查询商品错误错误: %s", presponse.getData()));
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, String.format( "查询商品错误: %s", presponse.getData()));
        }
        JSONObject rmpData = (JSONObject) presponse.getData();
        List<RowMap> tmpList = (List<RowMap>) rmpData.get(this.getCollectionName());
        if(tmpList.size() < 1){
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "要设置的商品不存在");

        }
        for (JSONObject tmpObject:paramsObjectList
             ) {
            for (String tmpField: mustFields
                    ) {
                if(!tmpObject.containsKey(tmpField)){
                    logger.info(String.format( "参数必须包括: %s", tmpField));
                    return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, String.format( "参数必须包括: %s", tmpField));
                }
            }
            JSONObject tmpGoodsMoreBarCodeObject = new JSONObject();
            JSONObject tmpgoodsSpecPriceObject = new JSONObject();
            JSONObject tmpsaleGoodsObject = new JSONObject();

            long gsid = UniqueID.getUniqueID(true);
            RowMap tmpMap = tmpList.get(0);
            tmpMap.put("status", 1);
            tmpMap.put("creator", session.getUser_name());
            tmpMap.put("createDate", new Date());
            tmpMap.put("updateDate", new Date());
            tmpMap.put("goodsDisplayName", tmpObject.get("goodsName"));
            //不需要档口信息
            tmpMap.remove("siid");
            tmpMap.remove("stallCode");
            for (String key:tmpObject.keySet()
                    ) {
                tmpMap.put(key, tmpObject.get(key));

            }

//          先对多条码商品赋值
            tmpGoodsMoreBarCodeObject = (JSONObject) new JSONObject(tmpMap).clone();
            tmpGoodsMoreBarCodeObject.put("gsid", gsid);
//          对多条码价格表赋值
            tmpgoodsSpecPriceObject = (JSONObject) new JSONObject(tmpMap).clone();
            tmpgoodsSpecPriceObject.put("gspid", gsid);
            //          会员价 批发价 零售价保持一致
            tmpgoodsSpecPriceObject.put("customPrice", tmpgoodsSpecPriceObject.get("salePrice"));
            tmpgoodsSpecPriceObject.put("bulkPrice", tmpgoodsSpecPriceObject.get("salePrice"));
//          codeType 1 条码 (也就是 箱码商品) 2 plu码 (也就是plu称重商品)
            int codeType = 1;
//          前端 可能传值 "6" 或者 6
            if(tmpObject.get("goodsType").equals(6) || tmpObject.get("goodsType").equals("6")){
                codeType = 2;
            }
            tmpGoodsMoreBarCodeObject.put("codeType", codeType);
            tmpgoodsSpecPriceObject.put("codeType", codeType);
//            修改sgid barNo salePrice mainBarcodeFlag goodsType
            tmpsaleGoodsObject = (JSONObject) new JSONObject(tmpMap).clone();;
            tmpsaleGoodsObject.put("ssgid", gsid);
            tmpsaleGoodsObject.put("mainBarcodeFlag",0);
            tmpsaleGoodsObject.put("directFromErp",0);//是否来源ERP
            tmpsaleGoodsObject.put("goodsStatus",0);
//            tmpsaleGoodsObject.put("escaleFlag",true);//更新PLU码为电子称码
//          参考售价 会员价 最低售价 批发价 和零售价保持一致
            List<String> priceKeyList = Arrays.asList("refPrice", "memberPrice", "minSalePrice", "bulkPrice", "primeCost");
            for (String priceKey:priceKeyList
                    ) {
                tmpsaleGoodsObject.put(priceKey, tmpsaleGoodsObject.get(priceKey));
            }
            goodsMoreBarCodeList.add(tmpGoodsMoreBarCodeObject);
            goodsSpecPriceList.add(tmpgoodsSpecPriceObject);

            //移除字段sortLevel(分拣等级)，SaleGoods没改字段
            tmpsaleGoodsObject.remove("sortLevel");
            saleGoodsList.add(tmpsaleGoodsObject);
        }

        JSONObject goodsMoreBarCodeObject = new JSONObject();
        goodsMoreBarCodeObject.put(goodsMoreBarCodeService.getCollectionName(), goodsMoreBarCodeList);
        ServiceResponse response1 =  goodsMoreBarCodeService.onInsert(session, goodsMoreBarCodeObject);
        if(!response1.getReturncode().equals(ResponseCode.SUCCESS)){
            return ServiceResponse.buildFailure(session, "数据库异常");
        }
        JSONObject goodsSpecPriceObject = new JSONObject();
        goodsSpecPriceObject.put(goodsSpecPriceService.getCollectionName(), goodsSpecPriceList);
        ServiceResponse response2 =  goodsSpecPriceService.onInsert(session, goodsSpecPriceObject);
        if(!response2.getReturncode().equals(ResponseCode.SUCCESS)){
            return ServiceResponse.buildFailure(session, "数据库异常");
        }
        JSONObject saleGoodsObject = new JSONObject();
        saleGoodsObject.put(this.getCollectionName(), saleGoodsList);
        ServiceResponse rtResponse =  onInsert(session, saleGoodsObject);
        if(!rtResponse.getReturncode().equals(ResponseCode.SUCCESS)){
            throw new RuntimeException("插入销售表失败，请重新尝试");
        }
        return  rtResponse;
    }


    /*
    * @Description: plu商品 更新
    * 1 更新 goodsMoreBarCode
    * 2 更新 goodsSpecPrice
    * 3 更新 saleGoods
    * @param session
    * @param paramsObject {"saleGoods":["ssgid":"","barNo":"", "goodsName":"", "sortLevel":"",  "salePrice", "shopId":""]}
    * @return: com.product.model.ServiceResponse
    */
    @Transactional(propagation = Propagation.REQUIRED)
    public ServiceResponse moreCodeGoodsUpdate(ServiceSession session, JSONObject paramsObject){
        if(!paramsObject.containsKey("saleGoods")){
            logger.info(String.format( "参数必须包括: %s", "saleGoods"));
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, String.format( "参数必须包括: %s", "saleGoods"));
        }
        List<JSONObject> paramsObjectList = (List<JSONObject>) paramsObject.get("saleGoods");
        List<JSONObject> goodsMoreBarCodeList = new ArrayList<>();
        List<JSONObject> goodsSpecPriceList = new ArrayList<>();
        List<JSONObject> saleGoodsList = new ArrayList<>();
        List<String> mustField = Arrays.asList("ssgid", "barNo", "goodsName", "shopId", "erpCode");
        for (JSONObject tmpObject: paramsObjectList
             ) {
            for (String key: mustField
                 ) {
                if(!tmpObject.containsKey(key)){
                    logger.info(String.format( "参数必须包括: %s", key));
                    return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, String.format( "参数必须包括: %s", key));
                }
            }
            JSONObject tmpGoodsMoreBarCode = new JSONObject();
            JSONObject tmpGoodsSpecPricee = new JSONObject();
            JSONObject tmpsaleGood = new JSONObject();

//            1 首先，通过，ssgid查询出原来的 barNo
            JSONObject tmpQueryParams = new JSONObject();
            tmpQueryParams.put("ssgid", tmpObject.get("ssgid"));
            tmpQueryParams.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "barNo");
            ServiceResponse tmpQueryResponse = onQuery(session, tmpQueryParams);
            if (!tmpQueryResponse.getReturncode().equals(ResponseCode.SUCCESS)){
                logger.error(String.format("查询错误------%s",tmpQueryResponse.getData()));
                return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询错误");
            }
            JSONObject tmpQueryObject = (JSONObject) tmpQueryResponse.getData();
            List<RowMap> tmpQueryModelList = (List<RowMap>) tmpQueryObject.get(this.getCollectionName());
            if(tmpQueryModelList.size() < 0){
                logger.error("需要更新的数据已经删除了");
                return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "需要更新的数据已经删除了");
            }
            SaleGoodsModel saleGoodsModel =  castToJavaBean(tmpQueryModelList.get(0), SaleGoodsModel.class);
            if (saleGoodsModel.getBarNo() == null){
                logger.error("需要更新的数据的barNo不存在");
                return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "需要更新的数据的barNo不存在");
            }
            String oldBarNo = saleGoodsModel.getBarNo();
            tmpObject.put("updateDate", new Date());
            tmpObject.put("entId", session.getEnt_id());
            tmpObject.put("oldBarNo", oldBarNo);

//          2 多条码商品 tmpGoodsMoreBarCode 赋值
            tmpGoodsMoreBarCode = (JSONObject) new JSONObject(tmpObject).clone();
//          3 多条码价格商品表赋值
            tmpGoodsSpecPricee = (JSONObject) new JSONObject(tmpObject).clone();
//          4 商品变赋值
            tmpsaleGood = (JSONObject) new JSONObject(tmpObject).clone();
            goodsMoreBarCodeList.add(tmpGoodsMoreBarCode);
            goodsSpecPriceList.add(tmpGoodsSpecPricee);
            
            //移除字段sortLevel(分拣等级)，SaleGoods没改字段
            tmpsaleGood.remove("sortLevel");
            saleGoodsList.add(tmpsaleGood);
        }
        FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        int result1 = template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.UpdategoodsMoreBarCode", goodsMoreBarCodeList);
        if (result1 < 1){
            logger.error("goodsspecprice 没有需要修改的商品");

        }
        int result2 = template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.UpdateGoodsSpecPrice", goodsSpecPriceList);
        if (result2 < 1){
            logger.error("goodsMoreBarCode 没有需要修改的商品");
        }
        JSONObject saleGoodsParams = new JSONObject();
        saleGoodsParams.put(this.getCollectionName(), saleGoodsList);
        return onUpdate(session, saleGoodsParams);
    }

    /*
    * @Description: 通过goodsCode 查找 plu商品
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    public ServiceResponse moreCodeGoodsDetailSearch(ServiceSession session, JSONObject paramsObject){

        List<String> mustField = Arrays.asList("goodsCode", "erpCode", "goodsType");
        for (String key: mustField
             ) {
            if(!paramsObject.containsKey(key)){
                logger.info(String.format( "参数必须包括: %s", key));
                return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, String.format( "参数必须包括: %s", key));
            }
        }
        String field = "goodsCode,goodsName,sortLevel,barNo,salePrice,ssgid,shopId,shopCode,updateDate,erpCode,partsNum";
        paramsObject.put(BeanConstant.QueryField.PARAMKEY_FIELDS, field);
        paramsObject.put("status", 1);
        int codeType = paramsObject.get("goodsType").equals("8")? 1:2;
        paramsObject.put("codeType", codeType); //1 -> barNo 2->pluNo
//        paramsObject.put("mainBarcodeFlag", 0);
        ServiceResponse rtResponse =  goodsSpecPriceService.onQuery(session, paramsObject);
        if(!rtResponse.getReturncode().equals(ResponseCode.SUCCESS)){
            return  rtResponse;
        }
        JSONObject rtObject = (JSONObject) rtResponse.getData();
        rtObject = (JSONObject) toNameJSONByKey(rtObject, config);
        return ServiceResponse.buildSuccess(rtObject);
    }

    public ServiceResponse unquieMoreDetailSearch(ServiceSession session, JSONObject paramsObject){
//        查询多条码商品表
        List<String> mustFiled = Arrays.asList("goodsCode", "erpCode");
        for (String field: mustFiled
             ) {
            if(!paramsObject.containsKey(field)){
                logger.error(String.format("参数必须包括字段 %s", field));
                return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, String.format("参数必须包括字段 %s", field));
            }
        }
        paramsObject.put("codeType", 1); //1 条码 2 plu码
        paramsObject.put("status", 1);
        paramsObject.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "goodsCode,goodsName,partsNum,salePrice,barNo,updateDate,erpCode");
        ServiceResponse goodsMoreResponse = goodsMoreBarCodeService.onQuery(session, paramsObject);
        if(!goodsMoreResponse.getReturncode().equals(ResponseCode.SUCCESS)){
            logger.error(String.format("查询箱码设置详情失败----%s", goodsMoreResponse.getData()));
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, String.format("查询箱码设置详情失败----%s", goodsMoreResponse.getData()));

        }
        JSONObject goodsMoreObject = (JSONObject) goodsMoreResponse.getData();
        goodsMoreObject = (JSONObject) toNameJSONByKey(goodsMoreObject, config);
        List<JSONObject> goodsMoreList = (List<JSONObject>) goodsMoreObject.get("saleGoods");
//        为了避免前端修改
        for (JSONObject tmpMap: goodsMoreList
             ) {
            tmpMap.put("ssgid", tmpMap.get("barNo"));
            tmpMap.put("ssgidList", new ArrayList<String>());
            getSsgidByBarNo(session, tmpMap);
        }
        return ServiceResponse.buildSuccess(goodsMoreObject);

    }


     /*
      * @Description:  根据barNo查询出所有的ssgid，如果后期箱码设置查询速度慢，可以优化此处
      *
      * @return:
      */
    public void getSsgidByBarNo(ServiceSession session, JSONObject tmpMap){
        JSONObject queryParams = new JSONObject();
        if (tmpMap.containsKey("erpCode")){
            queryParams.put("erpCode", tmpMap.get("erpCode"));
        }
        if(!tmpMap.containsKey("barNo")){
            logger.error("查询的商品中不包括barNo");
            return ;
        }
        queryParams.put("barNo", tmpMap.get("barNo"));
        queryParams.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "ssgid");
        ServiceResponse ssResponse = onQuery(session, queryParams);
        if(ssResponse.getReturncode().equals(ResponseCode.SUCCESS)){
            JSONObject ttObject = (JSONObject) ssResponse.getData();
            List<RowMap> ttList = (List<RowMap>) ttObject.get(this.getCollectionName());
            for (RowMap ttMap: ttList
                 ) {
                if(ttMap.containsKey("ssgid")){
                    String ssgid = (String) ttMap.get("ssgid");
                    List<String> ssgidList = (List<String>) tmpMap.get("ssgidList");
                    if(!ssgidList.contains(ssgid)){
                        ssgidList.add(ssgid);
                    }
                }
            }
        }
    }



     /*
      * @Description:  多条码或者多plu码商品删除， update
      * 1 更新 goodsMoreBarCode
      * 2 更新 goodsSpecPrice
      * 3 删除 saleGoods
      * @return:
      */
    public ServiceResponse moreGoodsDelete(ServiceSession session, JSONObject paramsObject){
        List<JSONObject> deleteParamsList = new ArrayList<>();
        List<JSONObject> updateGoodsMoreBarCodeList = new ArrayList<>();

        JSONObject tmpGoodsMoreObject = new JSONObject();
        if(!paramsObject.containsKey("ssgid") || !paramsObject.containsKey("barNo") ||
                !paramsObject.containsKey("status")){
            logger.error("参数没有唯一标示");
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");

        }
        paramsObject.put("oldBarNo", paramsObject.get("barNo"));
        if(List.class.isInstance(paramsObject.get("ssgid"))){
            List<String> tmpSgidList = (List<String>) paramsObject.get("ssgid");
            for (String ssgid:tmpSgidList
                 ) {
                JSONObject tmpObject = new JSONObject();
                tmpObject.put("ssgid", ssgid);
                deleteParamsList.add(tmpObject);
            }
        }else {
            JSONObject tmpObject = new JSONObject();
            tmpObject.put("ssgid", paramsObject.get("ssgid"));
            deleteParamsList.add(tmpObject);
        }
        if(deleteParamsList.size() < 1){
            logger.error("没有需要删除的商品");
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "没有需要删除的商品");
        }
        tmpGoodsMoreObject = (JSONObject) paramsObject.clone();
        tmpGoodsMoreObject.put("entId", session.getEnt_id());
        updateGoodsMoreBarCodeList.add(tmpGoodsMoreObject);
        FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        int result1 = template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.UpdategoodsMoreBarCode", updateGoodsMoreBarCodeList);
        logger.error(String.format("goodsMoreCode 修改的商品%d", result1));

        int result2 = template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.UpdateGoodsSpecPrice", updateGoodsMoreBarCodeList);
        logger.error(String.format("goodsspecprice 修改的商品%d", result2));
//        }
        JSONObject deleteParam = new JSONObject();
        deleteParam.put("saleGoods", deleteParamsList);
        return onDelete(session, deleteParam);
    }

//    public ServiceResponse moreGoodsDeleteByBarNo(ServiceSession session, JSONObject paramsObject){
//
//        List<String> mustField = Arrays.asList("barNo")
//    }



}
