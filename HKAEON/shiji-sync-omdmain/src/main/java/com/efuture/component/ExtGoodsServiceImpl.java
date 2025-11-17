package com.efuture.component;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.efuture.common.CommonExtSyncService;
import com.efuture.model.ExtGoodsModel;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by huangzhengwei on 2018/5/29.
 *
 * @Desciption:
 */
public class ExtGoodsServiceImpl extends CommonExtSyncService<ExtGoodsModel,ExtGoodsServiceImpl> {

    @Qualifier("sqlSessionTemplate")
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    public ExtGoodsServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }

    @Transactional
    public ServiceResponse updateGItemsGoodsType(ServiceSession session, JSONObject param) throws Exception {
        int updated = sqlSessionTemplate.update("ExtGoodsModelMapper.updateGItemsGoodsType");
        logger.info("===============更新组包码商品类型[{}]条================", updated);
        return ServiceResponse.buildSuccess("===============更新组包码商品类型[" + updated + "]条================");
    }
    
    @Override
    public List<ExtGoodsModel> initData(List<ExtGoodsModel> list){
    	//处理ERP需要删除的数据
    	list.forEach(model -> {
    		//ERP增删改状态1:增加,2:删除,3:更新
    		if("2".equals(model.getDMLstatus())) {
    			model.setGoodsStatus((short)-1);
    		}else {
    			model.setGoodsStatus((short)1);
    		}
    		//处理goodsName的/ 
    		if(!StringUtils.isEmpty(model.getGoodsName())) {
	    		String goodsName = model.getGoodsName().replaceAll("\\\\", "");
	    		if(goodsName.contains("𠱸")) {
	    			goodsName = goodsName.replaceAll("𠱸", "臣");
	    		}
	    		if(goodsName.contains("𧐢")) {
	    			goodsName = goodsName.replaceAll("𧐢", "毫");
	    		}
	    		model.setGoodsName(goodsName);
    		}
    		if(!StringUtils.isEmpty(model.getGoodsDisplayName())) {
    			String goodsDisplayName = model.getGoodsDisplayName().replaceAll("\\\\", "");
    			if(goodsDisplayName.contains("𠱸")) {
    				goodsDisplayName = goodsDisplayName.replaceAll("𠱸", "臣");
	    		}
    			if(goodsDisplayName.contains("𧐢")) {
    				goodsDisplayName = goodsDisplayName.replaceAll("𧐢", "毫");
    			}
    			model.setGoodsDisplayName(goodsDisplayName);
    		}
    		if(!StringUtils.isEmpty(model.getEnSname())) {
    			String enSname = model.getEnSname().replaceAll("\\\\", "");
    			if(enSname.contains("𠱸")) {
    				enSname = enSname.replaceAll("𠱸", "臣");
	    		}
    			if(enSname.contains("𧐢")) {
    				enSname = enSname.replaceAll("𧐢", "毫");
    			}
    			model.setEnSname(enSname);
    		}
    		if(!StringUtils.isEmpty(model.getEnFname())) {
    			String enFname = model.getEnFname().replaceAll("\\\\", "");
    			if(enFname.contains("𠱸")) {
    				enFname = enFname.replaceAll("𠱸", "臣");
	    		}
    			if(enFname.contains("𧐢")) {
    				enFname = enFname.replaceAll("𧐢", "毫");
    			}
    			model.setEnFname(enFname);
    		}
    		if(!StringUtils.isEmpty(model.getFullName())) {
    			String fullName = model.getFullName().replaceAll("\\\\", "");
    			if(fullName.contains("𠱸")) {
    				fullName = fullName.replaceAll("𠱸", "臣");
	    		}
    			if(fullName.contains("𧐢")) {
    				fullName = fullName.replaceAll("𧐢", "毫");
    			}
    			model.setFullName(fullName);
    		}
    		model.setDirectFromErp(true);
    		//在生成goods时，不管profit给过来的escalflag值是什么，我们都处理成0
    		model.setEscaleFlag(false);
    	});
    	return list;
    }
}
