package com.efuture.component;

import com.efuture.common.CommonSyncService;
import com.efuture.model.GoodsMoreBarCodeModel;
import org.mybatis.spring.SqlSessionTemplate;

public class GoodsMoreBarcodeServiceImpl extends CommonSyncService<GoodsMoreBarCodeModel,GoodsMoreBarcodeServiceImpl> {
	public GoodsMoreBarcodeServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}


/*	@SuppressWarnings("unchecked")
	@Override
	public List<GoodsMoreBarCodeModel> initData(List<GoodsMoreBarCodeModel> list) {
		JSONObject selectParams = new JSONObject();
        selectParams.put("key", "goodsCode,erpCode,entId");
        selectParams.put("values", list.stream().map(GoodsMoreBarCodeModel::getGoodsCodeKeyValue).collect(Collectors.toSet()));
        selectParams.put("table","goods");
        selectParams.put("field","concat('\\'',goodsCode,'\\',\\'',erpCode,'\\',',entId) as mapkey, sgid as mapvalue");
        MapResultHandler mapResultHandler = new MapResultHandler();
	    this.getTemplate().select("AdvancedQueryMapper.selectMap", selectParams,mapResultHandler);
		Map<String,Long> map = mapResultHandler.getMappedResults();
		list.stream().forEach(e ->{
			e.setSgid(map.get(e.getGoodsCodeKeyValue()));
		});  
		return list;
	}*/
    
	/*@Override
    public void resetIdByCode(){
    	Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("targetTable", "goodsshopref");
		paramMap.put("sourceTable", "goods");
		paramMap.put("targetCode", "goodsCode");
		paramMap.put("sourceCode", "goodsCode");
		paramMap.put("targetId", "sgid");
		paramMap.put("sourceId", "sgid");
		int updateSgidCount = this.getSqlSessionTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
		this.logger.info("==========》》》设置商品Id。更新数量："+updateSgidCount);
    }*/
}
