package com.efuture.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.common.CommonSyncService;
import com.efuture.model.ShopModel;
import com.efuture.utils.HttpUtils;
import com.product.model.ServiceSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

public class ShopServiceImpl extends CommonSyncService<ShopModel,ShopServiceImpl> {

	@Value("${shop.sync.redis}")
	String url;

	private final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

	public ShopServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}

	@Override
	public List<ShopModel> initData(List<ShopModel> data){
		//(level MQ和文件解析处理方式不一样)香港树型结构代码展示，依赖level字段。门店数据强行挂在601下面
		data.forEach(model->{
			//MQ方式
			if(!"601".equals(model.getShopCode())) {
				if(model.getDealType() != null && model.getDealType().trim().length() > 0){
					//（MQ）level + 1
					Short level = Short.valueOf(String.valueOf(model.getLevel() + 1));
					model.setLevel(level);
					//（MQ）设置父节点编码
					String comCode = model.getComCode() == null ? "601" : model.getComCode();
					model.setParentCode(comCode);
				}
			}
		});
		return data;
	}

	public void onSyncRedis() {
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0);
		JSONObject param = new JSONObject();
		param.put("entId", 0);
		param.put("erpCode", "002");
		try {
			LOGGER.info("调用主数据接口，同步redis，表:{}", getTableName());
			HttpUtils.onPost(url, session, param.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*@Override
	public List<ShopModel> initData(List<ShopModel> list) {
		//父节点code为空即顶级节点：初始化为0
		list.stream().forEach(e ->{
			if(StringUtils.isEmpty(e.getParentCode())){
				e.setParentId(0L);//设置父节点ID
				e.setParentCode("0");//设置父节点编码
			}
		});  
		return list;
	}*/
	
	//通过Code设置Id
    @Override
    public void resetIdByCode(){
    	//设置父类Id
    	Map<String,Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("targetTable", "shop");
    	paramMap.put("sourceTable", "shop");
    	paramMap.put("targetCode", "parentCode");
    	paramMap.put("sourceCode", "shopCode");
    	paramMap.put("targetId", "parentId");
    	paramMap.put("sourceId", "shopId");
    	int updateIdCount = this.getTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
    	this.logger.info("==========》》》门店设置父类Id。更新数量："+updateIdCount);
    	
    	//删除门店编码999
//    	int deletedCount =  this.getTemplate().delete("AdvancedQueryMapper.deleteShop");
//    	this.logger.info("==========》》》删除门店999数量:"+deletedCount);
    }
}
