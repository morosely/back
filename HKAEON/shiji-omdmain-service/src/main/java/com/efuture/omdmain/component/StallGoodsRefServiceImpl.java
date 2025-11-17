package com.efuture.omdmain.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.SaleGoodsModel;
import com.efuture.omdmain.service.StallGoodsRefService;
import com.mongodb.DBObject;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.RowMap;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.ParamValidateUtil;

/**
 * 档口经营商品Service
 *
 * @author chenp
 */
public class StallGoodsRefServiceImpl extends CommonServiceImpl<SaleGoodsModel, StallGoodsRefServiceImpl> implements StallGoodsRefService {

    public StallGoodsRefServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
        super(mybatisTemplate, collectionName, keyfieldName);
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
    private UpdateSaleGoodsServiceImpl saleGoodsServiceImpl;
    @Autowired
    private GoodsShopRefServiceImpl goodsShopRefServiceImpl;
    @Autowired
    private PackageAttShopGoodsRefService packageAttShopGoodsRefService;

    //批量保存门店商品和属性分类关系
    public ServiceResponse bacthSaveRef(ServiceSession session, JSONObject paramsObject) throws Exception {
        ServiceResponse response = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"pCode","saleGoods");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(response.getReturncode())) return response;

        JSONArray saleGoodsParams = paramsObject.getJSONArray("saleGoods");
        String pCode = paramsObject.getString("pCode");
        if(saleGoodsParams!=null && !saleGoodsParams.isEmpty()) {
            for(int i = 0; i < saleGoodsParams.size(); i++){
                JSONObject dataMap = saleGoodsParams.getJSONObject(i);
                response = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, dataMap,"goodsCode","barNo","erpCode","shopCode","stallCode","entId");
                if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(response.getReturncode())) return response;
            }
            for(int i = 0; i < saleGoodsParams.size(); i++){
                JSONObject dataMap = saleGoodsParams.getJSONObject(i);
                dataMap.put("pCode",pCode);
            }
            response = packageAttShopGoodsRefService.upsert(session,paramsObject);
        }
        return response;
    }

    // 单行删除
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse deleteNew(ServiceSession session, JSONObject paramsObject) throws Exception {
        ServiceResponse validate = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"ssgid","goodsCode","barNo","erpCode","shopCode","stallCode","entId");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(validate.getReturncode())) return validate;

        Criteria criteria = Criteria.where("ssgid").is(paramsObject.get("ssgid"));
        Query query = new Query(criteria);
        Field fields = query.fields();
        fields.include("ssgid,entId,erpCode,shopId,shopCode,stallCode,sgid,goodsCode,goodsName,barNo");
        List<SaleGoodsModel> modelList = this.getTemplate().select(query, SaleGoodsModel.class, "saleGoods");
        JSONArray saleGoodsList = JSONArray.parseArray(JSON.toJSONString(modelList));

        if (saleGoodsList.size() == 0) {
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "该档口商品可能已经被删除了，请刷新页面再试");
        }
        JSONObject saleGoods = (JSONObject) saleGoodsList.get(0);
        //Long sgid = saleGoods.get("sgid") == null ? 0 : saleGoods.getLong("sgid");
        FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);

        // 校验商品是否跟档口套餐有关系
        JSONObject paramMap = new JSONObject();
        paramMap.put("ssgid", saleGoods.getLong("ssgid"));            // 可售商品ID
        // 更新门店销售表-档口信息<!-- 1.通过ID无法判断主从条码 2.如果是主条码，第二SQL更新从条码档口信息 -->
        int a = template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.updateSaleGoodsList", paramMap);

        paramMap = new JSONObject();
        paramMap.put("erpCode", saleGoods.getString("erpCode"));      // 经营公司编码
        paramMap.put("shopId", saleGoods.getLong("shopId"));          // 门店id
        paramMap.put("goodsCode", saleGoods.getString("goodsCode"));  // 商品编码
        paramMap.put("order_field", "siid");// 解决部分经营配置商品有多个
        paramMap.put("order_direction", "desc");

        ServiceResponse goodsShopRefQuery = goodsShopRefServiceImpl.onQuery(session, paramMap);
        JSONObject goodsShopRefData = (JSONObject) goodsShopRefQuery.getData();
        JSONArray list = goodsShopRefData.getJSONArray(goodsShopRefServiceImpl.getCollectionName());

        int b = 0;
        if (list.size() > 0) {
            JSONObject goodsShopRef = (JSONObject) list.get(0);
            paramMap = new JSONObject();
            paramMap.put("gsrid", goodsShopRef.getLong("gsrid"));

            // 更新经营配置表-档口信息
            FMybatisTemplate template1 = this.getTemplate();
            template1.onSetContext(session);
            b = template1.getSqlSessionTemplate().update("beanmapper.GoodsShopRefModelMapper.updateGoodsShopRef", paramMap);
        }

        //add by yihaitao 2024-06-03 增加删除门店商品属性分类
        int c = 0; //表示不用删除属性分类门店商品关系表
        //Cause: java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.Long
        paramsObject.put("entId", paramsObject.getLong("entId"));
        ServiceResponse response = packageAttShopGoodsRefService.syncOneDelete(session, paramsObject);
        this.logger.info(" 【StallGoodsRefServiceImpl-deleteNew】=========> {}", response != null ? response.getData() : null);
        Object data = response.getData();
        if (data instanceof Integer) {
            c = response != null ? Integer.valueOf(response.getData().toString()) : 0;
        } else {
            c = -1;//表示返回的数据不是期望的数据类型
        }
        String result = String.format("更新salegoods{%d}条记录,更新goodsshopref表{%d}条记录,删除packageattshopgoodsref表{%d}条记录",a,b,c);
        return ServiceResponse.buildSuccess(result);
    }

    // 根据档口信息siid查询档口经营商品信息
/*	public ServiceResponse getDataBySiid(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (StringUtils.isEmpty(paramsObject)) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		}
		if (!paramsObject.containsKey("shopId")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "门店ID不能为空");
		}
		if (!paramsObject.containsKey("siid")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "档口ID不能为空");
		}
		paramsObject.put("order_field", "updateDate");   // 排序字段
		paramsObject.put("order_direction", "desc");     // 排序方法
		// 增加状态过滤 goodsStatus [1，3]
		List<Integer> list = Arrays.asList(1,3);
		paramsObject.put("goodsStatus", list);
		JSONObject goodsTypeParam = new JSONObject();//goodsType != 20
		goodsTypeParam.put("!=", 20);
		paramsObject.put("goodsType", goodsTypeParam);
		ServiceResponse response = this.onQuery(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			logger.error(Thread.currentThread().getStackTrace()[1].getMethodName() + " --- "+response.getData());
		}
		Map<String,List<RowMap>> map = (Map<String,List<RowMap>>)response.getData();
		List<RowMap> rowMapList = map.get(this.getCollectionName());
		//是否套餐显示前台CheckBox
		rowMapList.forEach(action->{
			//如果商品类型是9.说明是套餐
			if(action.get("goodsType")!=null && action.get("goodsType").equals(9)) {
				action.put("TCgoodsType",true);//TCgoodsType:是否是套餐
				action.put("TCdisable",false); //TCdisable:是否可编辑
			//如果goodsType返回的20（香港虚拟商品）
			}else if(action.get("goodsType")!=null && action.get("goodsType").equals(20)) {
				action.put("TCgoodsType",false);
				action.put("TCdisable",true);
			}else {
				action.put("TCgoodsType",false);
				action.put("TCdisable",false);
			}
		});
		
		return response;
	}*/

    // 优化档口商品查询
    public ServiceResponse getDataBySiid(ServiceSession session, JSONObject paramsObject) throws Exception {

        // 参数校验
        ParamValidateUtil.paramCheck(session, paramsObject);
        paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);

        // 设置默认分页参数
        if (!paramsObject.containsKey("page_size")) {
            paramsObject.put("page_size", 10);
        }
        if (!paramsObject.containsKey("page_no")) {
            paramsObject.put("page_no", 0);
        } else {
            paramsObject.put("page_no", (paramsObject.getInteger("page_no") - 1) * paramsObject.getInteger("page_size"));
        }

        FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        paramsObject.put("entId", session.getEnt_id());
        // 查询档口商品总数
        long total_results = template.getSqlSessionTemplate().selectOne("beanmapper.SaleGoodsModelMapper.countStallGoods", paramsObject);
        // 查询档口商品
        List<Map<String, Object>> goodsList = new ArrayList<Map<String, Object>>();
        if (total_results > 0) {
            goodsList = template.getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.getStallGoodsList", paramsObject);
            if (goodsList == null) {
                return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "档口商品查询失败！");
            }
            //是否套餐显示前台CheckBox
            goodsList.forEach(action -> {
                //如果商品类型是9.说明是套餐
                if (action.get("goodsType") != null && action.get("goodsType").equals(9)) {
                    action.put("TCgoodsType", true);//TCgoodsType:是否是套餐
                    action.put("TCdisable", false); //TCdisable:是否可编辑
                    //如果goodsType返回的20.（香港虚拟商品）
                } else if (action.get("goodsType") != null && action.get("goodsType").equals(20)) {
                    action.put("TCgoodsType", false);
                    action.put("TCdisable", true);
                } else {
                    action.put("TCgoodsType", false);
                    action.put("TCdisable", false);
                }
            });
        }
//		JSONArray array = JSONArray.parseArray(JSON.toJSONString(goodsList));
//		DefaultParametersUtils.numberFormat(array, "#0.00", "salePrice");// 价格格式处理

        JSONObject result = new JSONObject();
        result.put(this.getCollectionName(), goodsList);
        result.put("total_results", total_results);
        return ServiceResponse.buildSuccess(result);
    }

    // 单行删除
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse delete(ServiceSession session, JSONObject paramsObject) throws Exception {

        if (!paramsObject.containsKey("ssgid")) {
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "可售商品ID不能为空");
        }
        ParamValidateUtil.paramCheck(session, paramsObject, "ssgid");

        Criteria criteria = Criteria.where("ssgid").is(paramsObject.get("ssgid"));
        Query query = new Query(criteria);
        Field fields = query.fields();
        fields.include("ssgid,entId,erpCode,shopId,shopCode,stallCode,sgid,goodsCode,goodsName,barNo");
        List<SaleGoodsModel> modelList = this.getTemplate().select(query, SaleGoodsModel.class, "saleGoods");
        JSONArray saleGoodsList = JSONArray.parseArray(JSON.toJSONString(modelList));

//		ServiceResponse saleGoodsQuery = this.onQuery(session, paramsObject);
//		JSONObject saleGoodsData = (JSONObject) saleGoodsQuery.getData();
//		JSONArray saleGoodsList = saleGoodsData.getJSONArray("saleGoods");
        if (saleGoodsList.size() == 0) {
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "该档口商品可能已经被删除了，请刷新页面再试");
        }
        JSONObject saleGoods = (JSONObject) saleGoodsList.get(0);
        //Long sgid = saleGoods.get("sgid") == null ? 0 : saleGoods.getLong("sgid");
        FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);

        // 校验商品是否跟档口套餐有关系
        JSONObject paramMap = new JSONObject();
       /* paramMap.put("goodsId", paramsObject.get("ssgid"));
        paramMap.put("entId", saleGoods.get("entId"));
        paramMap.put("erpCode", saleGoods.get("erpCode"));
        paramMap.put("shopCode", saleGoods.get("shopCode"));
        paramMap.put("stallCode", saleGoods.get("stallCode"));
		long total_results = template.getSqlSessionTemplate().selectOne("beanmapper.SaleGoodsModelMapper.countCheckTCGoods", paramMap);
		if (total_results > 0) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"套餐商品已有明细，要删除，必须先删掉对应门店档口下套餐明细商品！");
		}
		
		paramMap = new JSONObject();*/
        paramMap.put("ssgid", saleGoods.getLong("ssgid"));            // 可售商品ID
        // 更新门店销售表-档口信息<!-- 1.通过ID无法判断主从条码 2.如果是主条码，第二SQL更新从条码档口信息 -->
        int a = template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.updateSaleGoodsList", paramMap);

        paramMap = new JSONObject();
        paramMap.put("erpCode", saleGoods.getString("erpCode"));      // 经营公司编码
        paramMap.put("shopId", saleGoods.getLong("shopId"));          // 门店id
        paramMap.put("goodsCode", saleGoods.getString("goodsCode"));  // 商品编码
        paramMap.put("order_field", "siid");// 解决部分经营配置商品有多个
        paramMap.put("order_direction", "desc");

        ServiceResponse goodsShopRefQuery = goodsShopRefServiceImpl.onQuery(session, paramMap);
        JSONObject goodsShopRefData = (JSONObject) goodsShopRefQuery.getData();
        JSONArray list = goodsShopRefData.getJSONArray(goodsShopRefServiceImpl.getCollectionName());

        int b = 0;
        if (list.size() > 0) {
            JSONObject goodsShopRef = (JSONObject) list.get(0);
            paramMap = new JSONObject();
            paramMap.put("gsrid", goodsShopRef.getLong("gsrid"));

            // 更新经营配置表-档口信息
            FMybatisTemplate template1 = this.getTemplate();
            template1.onSetContext(session);
            b = template1.getSqlSessionTemplate().update("beanmapper.GoodsShopRefModelMapper.updateGoodsShopRef", paramMap);
        }

        return ServiceResponse.buildSuccess("success,更新saleGoods表" + a + "条记录， 更新goodsShopRef表 " + b + "条记录");
    }

    // 批量新增
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception {

        if (session == null) {
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
        }
        if (StringUtils.isEmpty(paramsObject)) {
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
        }
        if (!paramsObject.containsKey("shopId")) {
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "门店ID不能为空");
        }
        if (!paramsObject.containsKey("siid")) {
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "档口ID不能为空");
        }
        if (!paramsObject.containsKey("stallCode")) {
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "档口编码不能为空");
        }

        JSONArray paramsList = paramsObject.getJSONArray("saleGoods");
        if (paramsList == null) paramsList = new JSONArray();
        if (paramsList.size() == 0) {
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "请挑选一个商品再提交");
        }
        this.setUpsert(false);

        List<JSONObject> goodsShopRefList = new ArrayList<>();
        List<JSONObject> saleGoodsList = new ArrayList<>();

        JSONObject saleGoods = null;
        Map<String, Object> returnMap = new HashMap<>();

        for (int i = 0; i < paramsList.size(); i++) {

            JSONObject dataMap = paramsList.getJSONObject(i);

            saleGoods = new JSONObject();
            saleGoods.put("ssgid", dataMap.getString("ssgid"));                    //  门店商品销售ID
            saleGoods.put("siid", paramsObject.getString("siid"));                 //  档口ID
            saleGoods.put("stallCode", paramsObject.getString("stallCode"));       //  档口编号
//			saleGoods.put("shopId", dataMap.get("shopId")); 
//			saleGoods.put("shopId", paramsObject.get("shopId"));
//			saleGoods.put("barNo", dataMap.getString("barNo")); 
            saleGoods.put("entId", session.getEnt_id());
            saleGoods.put("processFlag", dataMap.get("processFlag"));        // 是否加工(档口商品) 1:是 0:否
            saleGoods.put("updateDate", dataMap.getString("updateDate"));
            saleGoods.put("goodsType", dataMap.getString("goodsType"));
            saleGoodsList.add(saleGoods);
            //处理主条码的档口信息，需要更新从条码的档口信息
            if (dataMap.getBoolean("mainBarcodeFlag") != null && dataMap.getBoolean("mainBarcodeFlag")) {
                this.getTemplate().getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.updateStallInfoToSlave", saleGoods);
            }

            saleGoods = new JSONObject();
            saleGoods.put("erpCode", dataMap.getString("erpCode"));           //  经营公司编码
            saleGoods.put("shopId", paramsObject.getLong("shopId"));          //  门店ID
            saleGoods.put("goodsCode", dataMap.getString("goodsCode"));       //  商品编码
            ServiceResponse goodsShopRefQuery = goodsShopRefServiceImpl.onQuery(session, saleGoods);
            JSONObject goodsShopRefData = (JSONObject) goodsShopRefQuery.getData();
            JSONArray list = goodsShopRefData.getJSONArray(goodsShopRefServiceImpl.getCollectionName());
            if (list.size() > 0) {
                JSONObject json = list.getJSONObject(0);
                saleGoods.put("gsrid", json.getString("gsrid"));                  //  经营配置ID
                saleGoods.put("siid", paramsObject.getString("siid"));            //  档口ID
                saleGoods.put("stallCode", paramsObject.getString("stallCode"));  //  档口编号
                saleGoods.put("modifier", session.getUser_name());
                saleGoods.put("updateDate", dataMap.getString("updateDate"));
                goodsShopRefList.add(saleGoods);
            }
        }

        JSONObject paramMap = new JSONObject();
        paramMap.put(saleGoodsServiceImpl.getCollectionName(), saleGoodsList);
        // 更新salegoods表
        ServiceResponse response1 = saleGoodsServiceImpl.onUpdate(session, paramMap);
        if (!ResponseCode.SUCCESS.equals(response1.getReturncode())) {
            throw new Exception("新增档口商品失败," + response1.getData().toString());
        }

        returnMap.put(saleGoodsServiceImpl.getCollectionName(), response1.getData());

        paramMap.clear();
        paramMap.put(goodsShopRefServiceImpl.getCollectionName(), goodsShopRefList);
        // 更新goodsShopRef表
        ServiceResponse response2 = goodsShopRefServiceImpl.onUpdate(session, paramMap);
        if (!ResponseCode.SUCCESS.equals(response2.getReturncode())) {
            throw new Exception("新增档口商品失败," + response2.getData().toString());
        }
        returnMap.put(goodsShopRefServiceImpl.getCollectionName(), response2.getData());

        return ServiceResponse.buildSuccess(returnMap);
    }

    // 删除档口信息时，删除相应全部档口商品
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse deleteAll(ServiceSession session, JSONObject paramsObject) {

        if (session == null) {
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
        }
        if (StringUtils.isEmpty(paramsObject)) {
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
        }
        if (!paramsObject.containsKey("saleGoods")) {
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "可售商品不能为空");
        }

        JSONArray ssgidList = paramsObject.getJSONArray("saleGoods");
        if (ssgidList == null) ssgidList = new JSONArray();

        List<JSONObject> updateSaleGoodsParams = new ArrayList<>();
        List<JSONObject> updateGoodsShopRefParams = new ArrayList<>();

        JSONObject paramMap = null;
        int temp = ssgidList.size();
        for (int i = 0; i < temp; i++) {

            JSONObject saleGoods = ssgidList.getJSONObject(i);
            paramMap = new JSONObject();
            paramMap.put("ssgid", saleGoods.getLong("ssgid")); // 可售商品ID
            updateSaleGoodsParams.add(paramMap); // 存要修改的可售商品ID

            paramMap = new JSONObject();
            paramMap.put("erpCode", saleGoods.getString("erpCode"));      // 经营公司编码
            paramMap.put("shopId", saleGoods.getLong("shopId"));          // 门店id
            paramMap.put("goodsCode", saleGoods.getString("goodsCode"));  // 商品编码

//			ServiceResponse goodsShopRefQuery = goodsShopRefServiceImpl.onQuery(session, paramMap);
//			JSONObject goodsShopRefData = (JSONObject) goodsShopRefQuery.getData();
//			JSONArray list = goodsShopRefData.getJSONArray(goodsShopRefServiceImpl.getCollectionName());
//			JSONObject goodsShopRef = (JSONObject) list.get(0);
//			paramMap = new JSONObject();
//			paramMap.put("gsrid", goodsShopRef.getLong("gsrid")); 
            updateGoodsShopRefParams.add(paramMap);
        }

        // 更新门店销售表-档口信息
        FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.updateAllSaleGoodsList", updateSaleGoodsParams);

        // 更新经营配置表-档口信息
        FMybatisTemplate template1 = this.getTemplate();
        template1.onSetContext(session);
        template1.getSqlSessionTemplate().update("beanmapper.GoodsShopRefModelMapper.updateGoodsShopRefList", updateGoodsShopRefParams);

        return ServiceResponse.buildSuccess("success");
    }

}
