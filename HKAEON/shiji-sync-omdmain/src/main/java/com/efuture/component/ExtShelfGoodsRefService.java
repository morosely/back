package com.efuture.component;

import com.efuture.common.CommonExtSyncService;
import com.efuture.model.ExtAeonMoreBarNoModel;
import com.efuture.model.stock.ExtShelfGoodsRefModel;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

public class ExtShelfGoodsRefService extends CommonExtSyncService<ExtShelfGoodsRefModel, ExtShelfGoodsRefService> {
    public ExtShelfGoodsRefService(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }
    
    @Override
    public List<ExtShelfGoodsRefModel> initData(List<ExtShelfGoodsRefModel> list){
    	//处理ERP需要删除的数据
    	list.forEach(model -> {
    		//ERP增删改状态1:增加,2:删除,3:更新
    		if(model.getDMLstatus() != null) {
    			model.setStatus(Integer.valueOf(model.getDMLstatus()));
    		}
    	});
    	return list;
    }
    
}
