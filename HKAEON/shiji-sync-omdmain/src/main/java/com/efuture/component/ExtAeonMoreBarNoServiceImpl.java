package com.efuture.component;

import com.efuture.common.CommonExtSyncService;
import com.efuture.model.ExtAeonMoreBarNoModel;
import com.efuture.model.GoodsModel;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

public class ExtAeonMoreBarNoServiceImpl extends CommonExtSyncService<ExtAeonMoreBarNoModel, ExtAeonMoreBarNoServiceImpl> {
    public ExtAeonMoreBarNoServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }
    
    @Override
    public List<ExtAeonMoreBarNoModel> initData(List<ExtAeonMoreBarNoModel> list){
    	//处理ERP需要删除的数据
    	list.forEach(model -> {
    		//ERP增删改状态1:增加,2:删除,3:更新
    		if("2".equals(model.getDMLstatus())) {
    			model.setBarNoStatus((short)-1);
    		}else{
    			model.setBarNoStatus((short)1);
    		}
    	});
    	return list;
    }
}
