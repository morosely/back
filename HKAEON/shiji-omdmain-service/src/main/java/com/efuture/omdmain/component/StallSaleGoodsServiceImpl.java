package com.efuture.omdmain.component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.efuture.omdmain.model.*;
import com.efuture.omdmain.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.RowMap;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

import static java.util.stream.Collectors.*;

public class StallSaleGoodsServiceImpl extends CommonServiceImpl<StallSaleGoodsModel,StallSaleGoodsServiceImpl>{

	public StallSaleGoodsServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
		super(mybatisTemplate, collectionName, keyfieldName);
	}

	@Autowired
	private PackageAttShopGoodsRefService packageAttShopGoodsRefService;
	@Autowired
	private PackageAttCateService packageAttCateService;
	@Autowired
	private PackageAttDictService packageAttDictService;
	@Autowired
	private SaleGoodsPropertyService saleGoodsPropertyService;
	@Autowired
	private SaleGoodsImageRefServiceImpl saleGoodsImageRefServiceImpl;
	@Autowired
	private CateringTimeService cateringTimeService;

	//档口查询单品和套餐对应的明细商品
	public ServiceResponse searchAllDetails(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse validateResult = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "shopCode","stallCode");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(validateResult.getReturncode())) return validateResult;
		//JSONObject goodsType = new JSONObject(){{put("!=","20");}};
		//paramsObject.putIfAbsent("goodsType",goodsType);
		paramsObject.putIfAbsent("mainBarcodeFlag",1);
		paramsObject.putIfAbsent("goodsStatus",Arrays.asList(1,2,3));
		JSONArray hotGoodsCode = paramsObject.getJSONArray("hotGoodsCode");
		JSONArray hotBarNo = paramsObject.getJSONArray("hotBarNo");

		List<Map> rowMapList = new ArrayList<>();
		Map<String,Object> map = new HashMap<>();
		if(hotGoodsCode != null && hotBarNo != null && !hotGoodsCode.isEmpty() && !hotBarNo.isEmpty()){
			//"$or":[{"goodsCode":["010002079","000037531"]},{"parentGoodsCode":["010002079","000037531"]}]
			paramsObject.put("goodsCode",hotGoodsCode);
			paramsObject.put("barNo",hotBarNo);
			paramsObject.put("page_size",Integer.MAX_VALUE);
			ServiceResponse response1 = this.onQuery(session, paramsObject);
			Map<String,Object> goodsCodeMap = (Map<String,Object>)response1.getData();
			List<Map> response1RowMapList = (List<Map>)goodsCodeMap.get(this.getCollectionName());
			List mealIds = response1RowMapList.stream().filter(f -> f.get("goodsType")!=null && "9".equals(f.get("goodsType").toString())).map(action->action.get("ssgid")).collect(toList());

			List<Map> response2RowMapList = null;
			if(mealIds!=null && !mealIds.isEmpty()){
				paramsObject.clear();
				paramsObject.put("list",mealIds);
				response2RowMapList = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.StallInfoModelMapper.searchDetailSaleGoods", paramsObject);
			}

			if(response1RowMapList!=null && !response1RowMapList.isEmpty()){
				rowMapList.addAll(response1RowMapList);
			}
			if(response2RowMapList!=null && !response2RowMapList.isEmpty()){
				rowMapList.addAll(response2RowMapList);
			}
			map.put("total_results",rowMapList.size());
		}else{
			ServiceResponse response = this.onQuery(session, paramsObject);
			map = (Map<String,Object>)response.getData();
			rowMapList = (List<Map>)map.get(this.getCollectionName());
		}

		if(rowMapList!=null && !rowMapList.isEmpty()){
			Set<String> goodsCode = rowMapList.stream().map(action -> (String)action.get("goodsCode")).collect(toSet());
			String shopCode = paramsObject.getString("shopCode");
			String stallCode = paramsObject.getString("stallCode");
			paramsObject.clear();
			paramsObject.put("shopCode",shopCode);
			paramsObject.put("stallCode",stallCode);
			paramsObject.put("goodsCode",goodsCode);
			paramsObject.put("page_size",Integer.MAX_VALUE);
			JSONObject dateParam = new JSONObject(){{put(">=", DateUtils.getCurrentDay());}};
			paramsObject.put("updateDate",dateParam);
			List<SaleGoodsProperty> properties = saleGoodsPropertyService.wrapQueryBeanList(session,paramsObject);
			Map<String,String> goodsCodeSelloutMap = properties.stream().collect(Collectors.toMap(SaleGoodsProperty::getGoodsCode,SaleGoodsProperty::getSellout));

			//查询商品的图片
			paramsObject.clear();
			paramsObject.put("goodsCode",goodsCode);
			paramsObject.putIfAbsent("imageType",1);//默认查询主图
			paramsObject.put("page_size",Integer.MAX_VALUE);
			List<SaleGoodsImageRefModel> imageList = saleGoodsImageRefServiceImpl.wrapQueryBeanList(session,paramsObject);
			Map<String,String> codeImageTypeMap = imageList.stream().collect(Collectors.toMap(SaleGoodsImageRefModel::getGoodsCode,SaleGoodsImageRefModel::getImageUrl,(key1 , key2)-> key2));

			for (int i = 0; i < rowMapList.size(); i++) {
				Map data = rowMapList.get(i);
				data.put("sellout",goodsCodeSelloutMap.get(data.get("goodsCode")) == null || goodsCodeSelloutMap.isEmpty() ? 0 : goodsCodeSelloutMap.get(data.get("goodsCode")));
				//增加图片URL
				data.put("imageType",codeImageTypeMap.get(data.get("goodsCode")));
			}
		}

		paramsObject.clear();
		paramsObject.put("total_results",map.get("total_results"));
		paramsObject.put(this.getCollectionName(),rowMapList);
		return ServiceResponse.buildSuccess(paramsObject);
	}

	//档口查询所属商品(新)
	public ServiceResponse searchNew(ServiceSession session, JSONObject paramsObject) throws Exception {
		long start = System.currentTimeMillis();
		this.logger.info(" start --------->>>>> 开始进入方法 omdmain.stallsalegoods.searchNew");
		String erpCode = paramsObject.getString("erpCode") == null ? "002" : paramsObject.getString("erpCode");

		//返回的数据:筛选出套餐时段 add by yihaitao 2024-09-12
		Boolean cateringTimeFlag = paramsObject.getBoolean("cateringTimeFlag") != null ? paramsObject.getBoolean("cateringTimeFlag") : false;
		if(cateringTimeFlag){//套餐时段查询：不分页
			paramsObject.put("page_size",Integer.MAX_VALUE);
		}
		//自助不显示 0:否(显示),1:是(不显示) add by yihaitao 2024-12-30
		String selfNotShowParam = paramsObject.getString("selfNotShow");
		if(selfNotShowParam != null && selfNotShowParam.trim().length()>0){
			paramsObject.put("page_size",Integer.MAX_VALUE);
		}

		ServiceResponse response = this.onQuery(session, paramsObject);
		Map<String,List<RowMap>> map = (Map<String,List<RowMap>>)response.getData();
		List<RowMap> salegoodsRowList = map.get(this.getCollectionName());

		if(salegoodsRowList!=null && !salegoodsRowList.isEmpty()){
			List goodsCodes = salegoodsRowList.stream().map(action -> action.get("goodsCode")).collect(Collectors.toList());
			Object shopCode = salegoodsRowList.get(0).get("shopCode");
			Object stallCode = salegoodsRowList.get(0).get("stallCode");
			JSONArray attData = new JSONArray();
			JSONArray cateringTimeData = new JSONArray();
			JSONObject queryParam = new JSONObject();
			List<PackageAttCate> attCateList;
			Map<String,List<PackageAttCate>> attCateMap;
			List<PackageAttDict> dictList;
			Map<String,List<PackageAttDict>> dictMap;

			//1.查询商品属性分类
			Map<String, String> arrMap = new HashMap<>();
			paramsObject.clear();
			paramsObject.put("erpCode",erpCode);
			paramsObject.put("shopCode",shopCode);
			paramsObject.put("stallCode",stallCode);
			paramsObject.put("goodsCode",goodsCodes);
			paramsObject.put("page_size",Integer.MAX_VALUE);
			List<PackageAttShopGoodsRef> arrGoodsList = packageAttShopGoodsRefService.wrapQueryBeanList(session,paramsObject);
			Map<String, List<PackageAttShopGoodsRef>> arrGoodsMap = arrGoodsList.stream().collect(Collectors.groupingBy(PackageAttShopGoodsRef::getGoodsCode));
			//增加兼容性代码。如果属性分类删除了，属性分类名称显示属性分类的编码 add by yihaitao 2024-06-24
			//Map<String,String> pCodeNameMap = arrGoodsList.stream().collect(Collectors.toMap(PackageAttShopGoodsRef::getPCode,action -> action.getPName() == null ? action.getPCode() : action.getPName(),(key1 , key2)-> key2));//重复键对应的后值覆盖前值
			//防止空指针，代码优化
			Map<String,String> pCodeNameMap = new HashMap<>();
			if(!arrGoodsList.isEmpty()){
				for(PackageAttShopGoodsRef ref : arrGoodsList){
					String pCode = ref.getPCode();
					String pName = ref.getPName();
					if(pCode != null){
						pCodeNameMap.put(pCode,pName == null ? pCode : pName);
					}
				}
			}
			//查询商品的图片
			paramsObject.clear();
			paramsObject.put("goodsCode",goodsCodes);
			paramsObject.putIfAbsent("imageType",1);//默认查询主图
			paramsObject.put("page_size",Integer.MAX_VALUE);
			List<SaleGoodsImageRefModel> imageList = saleGoodsImageRefServiceImpl.wrapQueryBeanList(session,paramsObject);
			Map<String,String> codeImageTypeMap = imageList.stream().collect(Collectors.toMap(SaleGoodsImageRefModel::getGoodsCode,SaleGoodsImageRefModel::getImageUrl,(key1 , key2)-> key2));

			//2.查询packageattcate获取下级数据
			if(!pCodeNameMap.isEmpty()){
				pCodeNameMap.forEach((k,v) -> {
					JSONObject data = new JSONObject();
					data.put("pCode",k);
					data.put("pName",v);
					attData.add(data);
				});
				queryParam.clear();
				queryParam.put("level",3);
				queryParam.put("parentCode",arrGoodsList.stream().map(action -> action.getPCode()).collect(Collectors.toSet()));
				queryParam.put("erpCode",erpCode);
				queryParam.put("entId",session.getEnt_id());
				queryParam.put("page_size",Integer.MAX_VALUE);
				attCateList = packageAttCateService.wrapQueryBeanList(session,queryParam);

				//3.查询packageattdict数据
				if(attCateList!=null && !attCateList.isEmpty()){
					queryParam.clear();
					queryParam.put("pCode",attCateList.stream().map(action -> action.getPCode()).collect(toList()));
					queryParam.put("erpCode",erpCode);
					queryParam.put("entId",session.getEnt_id());
					queryParam.put("page_size",Integer.MAX_VALUE);
					dictList = packageAttDictService.wrapQueryBeanList(session,queryParam);
					if(dictList!=null && !dictList.isEmpty()){
						dictMap = dictList.stream().collect(Collectors.groupingBy(PackageAttDict::getPCode));
					} else {
						dictMap = null;
					}

					//4.数据挂载
					attCateMap = attCateList.stream().collect(Collectors.groupingBy(PackageAttCate::getParentCode));
					attData.forEach(obj -> {
						JSONObject jsonObj = (JSONObject) obj;
						String attCatePCode = jsonObj.getString("pCode");
						if(attCateMap.containsKey(attCatePCode)){
							JSONArray attCateArray = JSON.parseArray((JSONObject.toJSONString(attCateMap.get(attCatePCode), SerializerFeature.WriteDateUseDateFormat)));
							for (int i = 0; i < attCateArray.size(); i++) {
								JSONObject attCateJSON = attCateArray.getJSONObject(i);
								String pCode = attCateJSON.getString("pCode");
								if (dictMap != null && dictMap.containsKey(pCode)) {
									attCateJSON.put("attdic", JSON.parseArray((JSONObject.toJSONString(dictMap.get(pCode), SerializerFeature.WriteDateUseDateFormat))));
								}
							}
							jsonObj.put("attdetail", attCateArray);
						}
					});
				}
			}

			Map<String,List<Object>> attMap = attData.stream().collect(Collectors.groupingBy(item -> JSON.parseObject(item.toString()).getString("pCode")));
			paramsObject.clear();
			paramsObject.put("shopCode",shopCode);
			paramsObject.put("stallCode",stallCode);
			paramsObject.put("goodsCode",goodsCodes);
			JSONObject dateParam = new JSONObject(){{put(">=", DateUtils.getCurrentDay());}};
			paramsObject.put("updateDate",dateParam);
			paramsObject.put("page_size",Integer.MAX_VALUE);
			List<SaleGoodsProperty> properties = saleGoodsPropertyService.wrapQueryBeanList(session,paramsObject);
			Map<String,String> goodsCodeSelloutMap = properties.stream().collect(Collectors.toMap(SaleGoodsProperty::getGoodsCode,SaleGoodsProperty::getSellout));
			//是否套餐显示前台CheckBox
			for (int i=0;i < salegoodsRowList.size();i++){
				RowMap salegoodsRow = salegoodsRowList.get(i);
				Object goodsCode = salegoodsRow.get("goodsCode");
				//赋值默认值0.自助不显示（0:否,1:是.默认勾选1）add by yihaitao 2024-12-13
				salegoodsRow.put("selfNotShow","0");
				//属性分类
				List<PackageAttShopGoodsRef> attRefData = arrGoodsMap.get(goodsCode);//判断商品是否有属性分类
				if(attRefData!=null && !attRefData.isEmpty()){//增加属性分类和套餐时段 add by yihaitao 2024-06-13
					PackageAttShopGoodsRef ref = attRefData.get(0);
					//增加自助不显示 add by yihaitao 2024-12-13
					salegoodsRow.put("selfNotShow",ref.getSelfNotShow());
					//增加属性分类
					String pCode = ref.getPCode();
					List data = attMap.get(pCode);
					if(data !=null && !data.isEmpty()){
						JSONObject attJSON = (JSONObject)data.get(0);
						salegoodsRow.putAll(attJSON);
					}
					//增加套餐时段的返回 add by yihaitao 2024-09-11
					String cateringTimeCode = ref.getCateringTimeCode();
					String cateringTimeName = ref.getCateringTimeName();
					if(cateringTimeCode!=null){
						//增加套餐时段 add by yihaitao 2024-09-26 存在多个时段编码：00#01
						if(cateringTimeCode.contains("#")){
							String [] cateringTimeArr = cateringTimeCode.split("#");
							List<CateringTime> cateringTimeList = cateringTimeService.wrapQueryBeanList(session, new JSONObject(){{put("cateringTimeCode",Arrays.asList(cateringTimeArr));}});
							Map<String,String> cateringTimeMap = cateringTimeList.stream().collect(Collectors.toMap(CateringTime::getCateringTimeCode,CateringTime::getCateringTimeName));
							StringBuffer sb = new StringBuffer();
							for(int j = 0 ; j < cateringTimeArr.length; j ++){
								if(j == cateringTimeArr.length - 1){
									sb.append(cateringTimeMap.get(cateringTimeArr[j]));
								}else{
									sb.append(cateringTimeMap.get(cateringTimeArr[j])).append("#");
								}
							}
							cateringTimeName = sb.toString();
						}
						salegoodsRow.put("cateringTimeCode",cateringTimeCode);
						salegoodsRow.put("cateringTimeName",cateringTimeName == null ? "" : cateringTimeName);
					}
				}
				//如果商品类型是9.说明是套餐
				if(salegoodsRow.get("goodsType")!=null && salegoodsRow.get("goodsType").equals(9)) {
					salegoodsRow.put("TCgoodsType",true);//TCgoodsType:是否是套餐
					salegoodsRow.put("TCdisable",false); //TCdisable:是否可编辑
					//如果goodsType返回的20（香港虚拟商品）
				}else if(salegoodsRow.get("goodsType")!=null && salegoodsRow.get("goodsType").equals(20)) {
					salegoodsRow.put("TCgoodsType",false);
					salegoodsRow.put("TCdisable",true);
				}else {
					salegoodsRow.put("TCgoodsType",false);
					salegoodsRow.put("TCdisable",false);
				}
				//增加售罄标识 add by yihaitao 2024-06-13
				salegoodsRow.put("sellout",goodsCodeSelloutMap.get(salegoodsRow.get("goodsCode")) == null || goodsCodeSelloutMap.isEmpty() ? 0 : goodsCodeSelloutMap.get(salegoodsRow.get("goodsCode")));
				//增加图片URL
				salegoodsRow.put("imageType",codeImageTypeMap.get(salegoodsRow.get("goodsCode")));
			}
		}

		//返回的数据:筛选出套餐时段 add by yihaitao 2024-09-12
		if(cateringTimeFlag) {//套餐时段查询：不分页
			//{"startTime":{"<=":"09:00:00"},"endTime":{">=":"09:00:00"}}
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String currentTime = sdf.format(new Date());
			paramsObject.clear();
			JSONObject startTime = new JSONObject() {{
				put("<=", currentTime);
			}};
			JSONObject endTime = new JSONObject() {{
				put(">=", currentTime);
			}};
			paramsObject.put("startTime", startTime);
			paramsObject.put("endTime", endTime);
			paramsObject.put("erpCode", erpCode);
			paramsObject.putIfAbsent("page_size",Integer.MAX_VALUE);
			List<CateringTime> cateringTimeList = cateringTimeService.wrapQueryBeanList(session, paramsObject);
			List<String> validCodeList = cateringTimeList.stream().map(CateringTime::getCateringTimeCode).collect(toList());

			if (response.getData() instanceof JSONObject) {
				JSONObject data = (JSONObject) response.getData();
				List list = (List)data.get(this.getCollectionName());
				if(list == null || list.isEmpty())
					return response;
				Integer total_results = data.getInteger("total_results");
				//list.removeIf(e -> cateringCodeList.contains(((Map)e).get("catertingTimeCode")));
				for (int i = list.size()-1; i >=0; i--){
					Map mapData = (Map)list.get(i);
					Object cateringTimeCode = mapData.get("cateringTimeCode");
					if(cateringTimeCode!=null && cateringTimeCode.toString().trim().length() > 0){
						String [] codeArr = cateringTimeCode.toString().split("#");
						if(codeArr.length == 1){//处理单个时段编码逻辑 add by yihaitao 2024-09-26
							if(!validCodeList.contains(cateringTimeCode.toString())){
								list.remove(i);
								total_results --;
							}
						}else{//处理多个时段编码逻辑  00#100#1010 add by yihaitao 2024-09-26
							boolean containFlag = false;
							for(int k = 0; k < codeArr.length; k++){
								String code = codeArr[k];
								if(validCodeList.contains(code)){
									containFlag = true;
									break;
								}
							}
							if(!containFlag){
								list.remove(i);
								total_results --;
							}
						}
					}
				}
				data.put(this.getCollectionName(),list);
				data.put("total_results",total_results);
				response.setData(data);
			}else{
				this.logger.error("--------->>>>> response.getData() 不是 JSONObject :{}",response.getData());
			}
		}


		//返回的数据筛选：自助不显示 0:否(显示),1:是(不显示) add by yihaitao 2024-12-30
		if(selfNotShowParam != null && selfNotShowParam.trim().length()>0){
			if (response.getData() instanceof JSONObject) {
				JSONObject data = (JSONObject) response.getData();
				List list = (List)data.get(this.getCollectionName());
				if(list == null || list.isEmpty())
					return response;
				Integer total_results = data.getInteger("total_results");
				for (int i = list.size()-1; i >=0; i--){
					Map mapData = (Map)list.get(i);
					Object selfNotShow = mapData.get("selfNotShow");
					if(!selfNotShowParam.equals(selfNotShow)){
						list.remove(i);
						total_results --;
					}
				}
				data.put(this.getCollectionName(),list);
				data.put("total_results",total_results);
				response.setData(data);
			}
		}
		this.logger.info(" end --------->>>>> 结束进入方法 omdmain.stallsalegoods.searchNew : time is " +(System.currentTimeMillis() - start));
		return response;
	}

	//某档口下查询归属档口商品
	public ServiceResponse searchStallGoods(ServiceSession session, JSONObject paramsObject) throws Exception {
		// 1.校验
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"goodsCode","erpCode","shopId");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode()))
			return result;
		//2.查询
//		FMybatisTemplate template = this.getTemplate();
//		template.onSetContext(session);
		paramsObject.put("siid","0");
		Map salegoodsMap = this.getTemplate().getSqlSessionTemplate().selectOne("beanmapper.StallInfoModelMapper.searchStallGoods", paramsObject);
		if(salegoodsMap!=null){
			String stallCode = (String)salegoodsMap.get("stallCode");
			if(stallCode == null || stallCode.trim().equals("")){//查询到没有其他档口占用商品
				paramsObject.clear();
				//如果商品类型是9.说明是套餐
				if(salegoodsMap.get("goodsType")!=null && salegoodsMap.get("goodsType").equals(9)) {
					salegoodsMap.put("TCgoodsType",true);//TCgoodsType:是否是套餐
					salegoodsMap.put("TCdisable",false); //TCdisable:是否可编辑
				//如果goodsType返回的20（香港虚拟商品）
				}else if(salegoodsMap.get("goodsType")!=null && salegoodsMap.get("goodsType").equals(20)) {
					salegoodsMap.put("TCgoodsType",false);
					salegoodsMap.put("TCdisable",true);
				}else {
					salegoodsMap.put("TCgoodsType",false);
					salegoodsMap.put("TCdisable",false);
				}
				paramsObject.put(this.getCollectionName(),Arrays.asList(salegoodsMap));
				return ServiceResponse.buildSuccess(paramsObject);
			}else{//该商品被其他档口占用商品
				paramsObject.clear();
				paramsObject.put("isExist",1);
				paramsObject.put("message","该商品编码"+salegoodsMap.get("goodsCode") + "已被档口占用："+salegoodsMap.get("stallCode"));
				paramsObject.put(this.getCollectionName(),Arrays.asList());
				return ServiceResponse.buildSuccess(paramsObject);
			}
		}else{//没有查询到记录
			String goodsCode = (String)paramsObject.get("goodsCode");
			paramsObject.clear();
			paramsObject.put("isExist",0);
			paramsObject.put("message","该商品编码:"+goodsCode+"没有查询到结果");
			paramsObject.put(this.getCollectionName(),Arrays.asList());
			return ServiceResponse.buildSuccess(paramsObject);
		}
	}
	
	//档口查询所属商品
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		long start = System.currentTimeMillis();
		this.logger.info(" start --------->>>>> 开始进入方法 omdmain.stallsalegoods.search");
		ServiceResponse response = this.onQuery(session, paramsObject);
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
		this.logger.info(" end --------->>>>> 结束进入方法 omdmain.stallsalegoods.search : time is " +(System.currentTimeMillis() - start));
		return response;
	}

}
