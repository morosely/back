package com.efuture.omdmain.component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.SaleGoodsModel;
import com.efuture.omdmain.model.ShopGoodsInfoUpdateModel;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ResponseCode.Failure;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.ParamValidateUtil;

/**
 * 门店商品资料修改表Service
 * 
 */
public class ShopGoodsInfoUpdateServiceImpl extends CommonServiceImpl<ShopGoodsInfoUpdateModel, ShopGoodsInfoUpdateServiceImpl>{

	public ShopGoodsInfoUpdateServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
		super(mybatisTemplate, collectionName, keyfieldName);
	}
	
	@Autowired
	private SaleGoodsServiceImpl saleGoodsService;
	
	//门店商品修改价格
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {
		Long startTime = System.currentTimeMillis();
		this.logger.info("   门店商品修改价格 start ============================================= >>>  "+ startTime);
		//0.校验
		 ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "ssgid");
		 if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		 
		 //1.查询之前的记录
		 JSONObject param = new JSONObject();
		 param.put("ssgid",paramsObject.getLong("ssgid"));
		 SaleGoodsModel saleGoods = saleGoodsService.wrapQueryBean(session,param);
		 if(saleGoods==null)
			 return ServiceResponse.buildFailure(session,Failure.NOT_EXIST,"查询商品不存在");
		 
		 //2.更新SaleGoods数据
		 String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		 paramsObject.put("updateDate",nowDate);
		 this.getTemplate().getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.updateShopGoodsInfo",paramsObject);
		 
		 //3.插入新数据到门店商品资料修改表
		 paramsObject.put("oldGoodsName",saleGoods.getGoodsName());
		 paramsObject.put("oldGoodsDisplayName",saleGoods.getGoodsDisplayName());
		 paramsObject.put("oldSalePrice",saleGoods.getSalePrice());
		 paramsObject.put("oldGoodsFromCode",saleGoods.getGoodsFromCode());
		 paramsObject.put("oldCategoryCode",saleGoods.getCategoryCode());
		 paramsObject.put("oldCategoryName",saleGoods.getCategoryName());
		 
		 paramsObject.put("createDate",nowDate);
//		 paramsObject.put("creator",session.getUser_name());
		 paramsObject.put("creator",session.getUser_code());
		 paramsObject.put("entId",session.getEnt_id());
		 paramsObject.put("newGoodsName",paramsObject.get("goodsName"));
		 paramsObject.put("newGoodsDisplayName",paramsObject.get("goodsDisplayName"));
		 paramsObject.put("newSalePrice",paramsObject.get("salePrice"));
		 paramsObject.put("newGoodsFromCode",paramsObject.get("goodsFromCode"));
		 paramsObject.put("newCategoryCode",paramsObject.get("categoryCode"));
		 paramsObject.put("newCategoryName",paramsObject.get("categoryName"));
		 result = this.onInsert(session, paramsObject);
		 Long endTime = System.currentTimeMillis();
		 this.logger.info("   门店商品修改价格 cost ============================================= >>> "+ + (endTime - startTime));
		 return result;
	 }
	
	// 门店商品修改查询
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		FMybatisTemplate template = this.getTemplate();
	    template.onSetContext(session);
	    
        // 设置默认分页参数
        if (!paramsObject.containsKey("page_no")) {
            paramsObject.put("page_no", 1);
        } else {
            paramsObject.put("page_no",(paramsObject.getInteger("page_no") - 1) * paramsObject.getInteger("page_size"));
        }
        if (!paramsObject.containsKey("page_size")) {
            paramsObject.put("page_size", 10);
        }
     	
        if(!StringUtils.isEmpty(paramsObject.get("createDate"))){
        	JSONObject object = paramsObject.getJSONObject("createDate");
        	if(!StringUtils.isEmpty(object.get(">="))){
        		paramsObject.put("beginTime", object.getString(">="));
        	}
        	if(!StringUtils.isEmpty(object.get("<="))){
        		paramsObject.put("endTime", object.getString("<="));
        	}
        	paramsObject.remove("createDate");
        }
        
        List<Map> shopGoodsInfoUpdateList = template.getSqlSessionTemplate().selectList("beanmapper.ShopGoodsInfoUpdateModelMapper.getShopGoodsInfoUpdateList", paramsObject);
        long total_results = 0;  // 总数
        if (shopGoodsInfoUpdateList != null && shopGoodsInfoUpdateList.size() > 0) {
            total_results = template.getSqlSessionTemplate().selectOne("beanmapper.ShopGoodsInfoUpdateModelMapper.countShopGoodsInfoUpdateList", paramsObject);
        }
		
		JSONObject result = null;
		StringBuilder stringOld = null;
		StringBuilder stringNew = null;
		List<JSONObject> modelList = new ArrayList<>();
		 // 操作时间格式化
     	SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if (shopGoodsInfoUpdateList != null && shopGoodsInfoUpdateList.size() > 0) {
			int size = shopGoodsInfoUpdateList.size();
			for (int i = 0; i < size; i++) {
				Map json = shopGoodsInfoUpdateList.get(i);
				
				stringOld = new StringBuilder();  // old
				// 商品名称(旧)
				stringOld.append("商品名称: " +(json.get("oldGoodsName") == null ? "" : json.get("oldGoodsName"))+ "<br>");
				// 商品展示名称(旧)
				stringOld.append("商品展示名称: "+(json.get("oldGoodsDisplayName") == null ? "" : json.get("oldGoodsDisplayName"))+ "<br>");
				// 商品价格(旧)
				stringOld.append("商品价格: " +(json.get("oldSalePrice") == null ? 0 : json.get("oldSalePrice"))+ "<br>");
				// 管制商品标签(旧)
				stringOld.append("管制商品标签: "+ (json.get("oldGoodsFromCode") == null ? "" : json.get("oldGoodsFromCode"))+ "<br>");
				// 工业分类编码(旧)
				stringOld.append("分类编码: "+ (json.get("oldCategoryCode") == null ? "" : json.get("oldCategoryCode"))+ "<br>");
				// 工业分类名称(旧)
				stringOld.append("分类名称: "+ (json.get("oldCategoryName") == null ? "" : json.get("oldCategoryName")));
				
//				System.out.println(stringOld.toString());
				
				stringNew = new StringBuilder();  // new
				// 商品名称(新)
				stringNew.append("商品名称: "+(json.get("newGoodsName")== null ? "" : json.get("newGoodsName"))+"<br>");
				// 商品展示名称(新)
				stringNew.append("商品展示名称: "+(json.get("newGoodsDisplayName")== null ? "" : json.get("newGoodsDisplayName"))+"<br>");
				// 商品价格(新)
				stringNew.append("商品价格: "+(json.get("newSalePrice")== null ? 0 : json.get("newSalePrice"))+"<br>");
				// 管制商品标签(新)
				stringNew.append("管制商品标签: "+(json.get("newGoodsFromCode")== null ? "" : json.get("newGoodsFromCode"))+"<br>");
				// 工业分类编码(新)
				stringNew.append("分类编码: "+(json.get("newCategoryCode")== null ? "" : json.get("newCategoryCode"))+"<br>");
				// 工业分类名称(新)
				stringNew.append("分类名称: "+(json.get("newCategoryName")== null ? "" : json.get("newCategoryName")));
				
				result = new JSONObject();
				result.put("erpCode", json.get("erpCode"));           // 经营公司编码
				result.put("erpName", json.get("erpName"));           // 经营公司名称
				result.put("shopCode", json.get("shopCode"));         // 门店编码
				result.put("shopName", json.get("shopName"));         // 门店名称
				result.put("old", stringOld);                         // 原值
				result.put("new", stringNew);                         // 新值
				result.put("creator", json.get("creator"));           // 操作人
				
				if (!StringUtils.isEmpty(json.get("createDate"))) {
					Date createDate = time.parse(json.get("createDate").toString());
					String createDateString = time.format(createDate);
					result.put("createDate", createDateString);             // 操作时间
				} else {
					result.put("createDate", "");                           // 操作时间
				}
				modelList.add(result);
			}
		}
		
		// 最终结果
		result = new JSONObject();
		result.put(this.getCollectionName(), modelList);
		result.put("total_results", total_results);
		return ServiceResponse.buildSuccess(result);
	}
	
	// 导出-门店商品修改查询
	public ServiceResponse getList(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		FMybatisTemplate template = this.getTemplate();
	    template.onSetContext(session);
	    
        // 设置默认分页参数
        if (!paramsObject.containsKey("page_no")) {
            paramsObject.put("page_no", 1);
        } else {
            paramsObject.put("page_no",(paramsObject.getInteger("page_no") - 1) * paramsObject.getInteger("page_size"));
        }
        if (!paramsObject.containsKey("page_size")) {
            paramsObject.put("page_size", 10);
        }
     	
        if(!StringUtils.isEmpty(paramsObject.get("createDate"))){
        	JSONObject object = paramsObject.getJSONObject("createDate");
        	if(!StringUtils.isEmpty(object.get(">="))){
        		paramsObject.put("beginTime", object.getString(">="));
        	}
        	if(!StringUtils.isEmpty(object.get("<="))){
        		paramsObject.put("endTime", object.getString("<="));
        	}
        	paramsObject.remove("createDate");
        }
        
        List<Map> shopGoodsInfoUpdateList = template.getSqlSessionTemplate().selectList("beanmapper.ShopGoodsInfoUpdateModelMapper.getShopGoodsInfoUpdateList", paramsObject);
        long total_results = 0;  // 总数
        if (shopGoodsInfoUpdateList != null && shopGoodsInfoUpdateList.size() > 0) {
            total_results = template.getSqlSessionTemplate().selectOne("beanmapper.ShopGoodsInfoUpdateModelMapper.countShopGoodsInfoUpdateList", paramsObject);
        }
		
		JSONObject result = null;
		StringBuilder stringOld = null;
		StringBuilder stringNew = null;
		List<JSONObject> modelList = new ArrayList<>();
		 // 操作时间格式化
     	SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if (shopGoodsInfoUpdateList != null && shopGoodsInfoUpdateList.size() > 0) {
			int size = shopGoodsInfoUpdateList.size();
			for (int i = 0; i < size; i++) {
				Map json = shopGoodsInfoUpdateList.get(i);
				
				stringOld = new StringBuilder();  // old
				// 商品名称(旧)
				stringOld.append("商品名称: " +(json.get("oldGoodsName") == null ? "" : json.get("oldGoodsName"))+ "\r\n");
				// 商品展示名称(旧)
				stringOld.append("商品展示名称: "+(json.get("oldGoodsDisplayName") == null ? "" : json.get("oldGoodsDisplayName"))+ "\r\n");
				// 商品价格(旧)
				stringOld.append("商品价格: " +(json.get("oldSalePrice") == null ? 0 : json.get("oldSalePrice"))+ "\r\n");
				// 管制商品标签(旧)
				stringOld.append("管制商品标签: "+ (json.get("oldGoodsFromCode") == null ? "" : json.get("oldGoodsFromCode"))+ "\r\n");
				// 工业分类编码(旧)
				stringOld.append("分类编码: "+ (json.get("oldCategoryCode") == null ? "" : json.get("oldCategoryCode"))+ "\r\n");
				// 工业分类名称(旧)
				stringOld.append("分类名称: "+ (json.get("oldCategoryName") == null ? "" : json.get("oldCategoryName")));
				
				stringNew = new StringBuilder();  // new
				// 商品名称(新)
				stringNew.append("商品名称: "+(json.get("newGoodsName")== null ? "" : json.get("newGoodsName"))+"\r\n");
				// 商品展示名称(新)
				stringNew.append("商品展示名称: "+(json.get("newGoodsDisplayName")== null ? "" : json.get("newGoodsDisplayName"))+"\r\n");
				// 商品价格(新)
				stringNew.append("商品价格: "+(json.get("newSalePrice")== null ? 0 : json.get("newSalePrice"))+"\r\n");
				// 管制商品标签(新)
				stringNew.append("管制商品标签: "+(json.get("newGoodsFromCode")== null ? "" : json.get("newGoodsFromCode"))+"\r\n");
				// 工业分类编码(新)
				stringNew.append("分类编码: "+(json.get("newCategoryCode")== null ? "" : json.get("newCategoryCode"))+"\r\n");
				// 工业分类名称(新)
				stringNew.append("分类名称: "+(json.get("newCategoryName")== null ? "" : json.get("newCategoryName")));
				
				result = new JSONObject();
				result.put("erpCode", json.get("erpCode"));           // 经营公司编码
				result.put("erpName", json.get("erpName"));           // 经营公司名称
				result.put("shopCode", json.get("shopCode"));         // 门店编码
				result.put("shopName", json.get("shopName"));         // 门店名称
				result.put("old", stringOld);                         // 原值
				result.put("new", stringNew);                         // 新值
				result.put("creator", json.get("creator"));           // 操作人
				
				if (!StringUtils.isEmpty(json.get("createDate"))) {
					Date createDate = time.parse(json.get("createDate").toString());
					String createDateString = time.format(createDate);
					result.put("createDate", createDateString);             // 操作时间
				} else {
					result.put("createDate", "");                           // 操作时间
				}
				modelList.add(result);
			}
		}
		
		// 最终结果
		result = new JSONObject();
		result.put(this.getCollectionName(), modelList);
		result.put("total_results", total_results);
		return ServiceResponse.buildSuccess(result);
	}
	
}
