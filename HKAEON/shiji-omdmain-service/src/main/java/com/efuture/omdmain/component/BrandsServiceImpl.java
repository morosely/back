package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.BrandInfoModel;
import com.efuture.omdmain.service.BrandsService;
import com.mongodb.DBObject;
import com.product.component.CommonServiceImpl;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Created by huangzhengwei on 2018/5/2.
 *
 * @Desciption:
 */
public class BrandsServiceImpl extends CommonServiceImpl<BrandInfoModel,BrandsServiceImpl> implements BrandsService {
    public BrandsServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

    /* 
    * @Description: 品牌查询
     * @param session
 * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    @Override
    public ServiceResponse search(ServiceSession session, JSONObject paramsObject) {
        /*List<String> paramList = new ArrayList();
        paramList.add("brandName");
        String fields = "erpCode,erpName,status,brandName,brandSCode,brandTypeCode,brandLevelCode,updateDate";
        paramsObject = ParamsFormat.formatLike(paramList, paramsObject);
        if(!paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_FIELDS)){
        	paramsObject.put(BeanConstant.QueryField.PARAMKEY_FIELDS, fields);
        }*/
        return onQuery(session, paramsObject);
    }

    @Override
    protected DBObject onBeforeRowInsert(Query query, Update update) {
        return this.onDefaultRowInsert(query, update);
    }

/*    @Override
    protected FMybatisTemplate getTemplate() {
        return this.getBean("StorageOperation", FMybatisTemplate.class);
    }*/
}
