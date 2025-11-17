package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.ExtCategoryModel;
import com.efuture.omdmain.service.ExtCategoryService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.*;
import com.product.storage.template.FMybatisTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhengwei on 2018/5/29.
 *
 * @Desciption:
 */
public class ExtCategoryServiceImpl extends JDBCCompomentServiceImpl<ExtCategoryModel> implements ExtCategoryService {
    public ExtCategoryServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

    @Override
    public ServiceResponse search(ServiceSession session, JSONObject paramsObject) {
        ServiceResponse ssResponse = onQuery(session, paramsObject);
        if(ssResponse.getReturncode() != "0"){
            return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询失败");
        }
        JSONObject ssObject = (JSONObject) ssResponse.getData();
        List<RowMap> ssList = (List<RowMap>) ssObject.get(this.getCollectionName());
        List<String> parentCodeList = new ArrayList();
        if(ssList.size() == 0){
            return ssResponse;
        }
        for (RowMap ssMap: ssList
             ) {
            if(ssMap.containsKey("parentCode")){
                parentCodeList.add((String) ssMap.get("parentCode"));
            }
        }
        JSONObject tmpQueryParams = new JSONObject();
        tmpQueryParams.put("categoryCode", parentCodeList);
        tmpQueryParams.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "categoryName,categoryCode");
        ServiceResponse tmpResponse = onQuery(session, tmpQueryParams);
        if(tmpResponse.getReturncode() != "0"){
            return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询失败");
        }
        JSONObject tmpObject = (JSONObject) tmpResponse.getData();
        List<RowMap> tmpList = (List<RowMap>) tmpObject.get(this.getCollectionName());
        for (RowMap ssMap: ssList
                ) {
            for (RowMap tmpMap:tmpList
                 ) {
                if (ssMap.get("parentCode").equals(tmpMap.get("categoryCode"))){
                    ssMap.put("parentName", tmpMap.get("categoryName"));
                    break;
                }
            }
            }
        System.out.println(ssList);
        return ssResponse;
    }

    @Override
    protected DBObject onBeforeRowInsert(Query query, Update update) {
        return this.onDefaultRowInsert(query, update);
    }

}
