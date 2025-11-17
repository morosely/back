package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.SaleGoodsImageRefModel;
import com.efuture.omdmain.service.SaleGoodsImageRefService;
import com.mongodb.DBObject;
import com.product.component.CommonServiceImpl;
import com.product.model.BeanConstant;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhengwei on 2018/5/28.
 *
 * @Desciption:
 */
public class SaleGoodsImageRefServiceImpl extends CommonServiceImpl<SaleGoodsImageRefModel,SaleGoodsImageRefServiceImpl> implements SaleGoodsImageRefService {
    
	public SaleGoodsImageRefServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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
    
    public ServiceResponse update(ServiceSession session, JSONObject paramsObject){
        JSONObject deleteObject = new JSONObject();
        JSONObject insertObject = new JSONObject();
        List<JSONObject> sgidList = new ArrayList();
        List<JSONObject> insertList = new ArrayList();
//        String tmpSgid = "";
        for (String tmpKey: paramsObject.keySet()
             ) {
            long sgid = Long.parseLong(tmpKey);
            JSONObject tmpSgidObject = new JSONObject();
            tmpSgidObject.put("sgid", sgid);
            sgidList.add(tmpSgidObject);
            List<JSONObject> imageObject = (List<JSONObject>) paramsObject.get(tmpKey);
            for (JSONObject tmpImageObject: imageObject
                 ) {
                JSONObject tmpObject = (JSONObject) tmpImageObject.clone();


                tmpObject.put("sgid", sgid);
                insertList.add(tmpObject);
            }
        }

        deleteObject.put(getCollectionName(), sgidList);
        System.out.println(deleteObject);
        insertObject.put(getCollectionName(), insertList);
        if(onDelete(session, deleteObject).getReturncode() != "0"){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "删除图片失败");
        }
        return  onInsert(session, insertObject);

    }
    
    // 查询当前商品的图片信息
    public ServiceResponse searchBySgid(ServiceSession session, JSONObject paramsObject) {
    	
    	if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (StringUtils.isEmpty(paramsObject)) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		}
		if (!paramsObject.containsKey("sgid")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "sgid"));
		}
		
		JSONObject tmpImageQuery = new JSONObject();
		tmpImageQuery.put("sgid", paramsObject.get("sgid"));
		tmpImageQuery.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "sgid,imageUrl,showTerm,imageType");
		
		return this.onQuery(session, tmpImageQuery);
    }
}
