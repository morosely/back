package com.efuture.component;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.efuture.common.CommonExtSyncService;
import com.efuture.model.ExtGoodsMoreBarCodeModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.StringUtils;

public class ExtGoodsMoreBarcodeServiceImpl extends CommonExtSyncService<ExtGoodsMoreBarCodeModel,ExtGoodsMoreBarcodeServiceImpl> {

    public ExtGoodsMoreBarcodeServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }

    @Override
    public List<JSONObject> search(JSONObject param) {
        return getOmdTemplate().selectList(this.getMapperNamespace() + ".select", param);
    }

    @Override
    public long dataCount(JSONObject param) {
        return getOmdTemplate().selectOne(this.getMapperNamespace() + ".dataCount", param);
    }
    
    //初始化数据
    public List<ExtGoodsMoreBarCodeModel> initData(List<ExtGoodsMoreBarCodeModel> list){
    	list.stream().forEach(e ->{
			e.setErpCode(StringUtils.isEmpty(e.getErpCode())? "002" : e.getErpCode());//为空默认值是002
			e.setEntId(StringUtils.isEmpty(e.getEntId())? 0l : e.getEntId());//为空默认值是0
		});  
    	return list;
    };

}
