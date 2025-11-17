package com.efuture.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.common.CommonExtSyncService;
import com.efuture.model.BrandInfoModel;
import com.efuture.model.ExtAeonMoreBarNoModel;
import com.efuture.model.ExtBrandInfoModel;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

/**
 * Created by huangzhengwei on 2018/5/29.
 *
 * @Desciption:
 */
public class ExtBrandInfoServiceImpl extends CommonExtSyncService<ExtBrandInfoModel,ExtBrandInfoServiceImpl> {

    public ExtBrandInfoServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }
    
    @Override
    public List<ExtBrandInfoModel> queryDataByUnique(JSONObject selectParams) {
    	return template.selectList("ExtBrandInfoModelMapper.queryDataByUnique", selectParams);
    }
    
}
