package com.efuture.component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.efuture.common.CommonExtSyncService;
import com.efuture.common.CommonSyncService;
import com.efuture.common.SpringUtil;
import com.efuture.model.BrandInfoModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by huangzhengwei on 2018/5/2.
 *
 * @Desciption:
 */
public class BrandInfoServiceImpl extends CommonSyncService<BrandInfoModel,BrandInfoServiceImpl> {

    public BrandInfoServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }
    
    @Override
    public List<BrandInfoModel> queryDataByUnique(JSONObject selectParams) {
    	return template.selectList("BrandInfoModelMapper.queryDataByUnique", selectParams);
    }
    
}
