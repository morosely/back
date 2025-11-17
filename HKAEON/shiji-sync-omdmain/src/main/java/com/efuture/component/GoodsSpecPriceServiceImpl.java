package com.efuture.component;

import com.efuture.common.CommonSyncService;
import com.efuture.model.GoodsSpecPriceModel;
import org.mybatis.spring.SqlSessionTemplate;

public class GoodsSpecPriceServiceImpl extends CommonSyncService<GoodsSpecPriceModel, GoodsSpecPriceServiceImpl>{
	public GoodsSpecPriceServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public List<GoodsSpecPriceModel> initData(List<GoodsSpecPriceModel> list) {
		//1.门店Id
		JSONObject selectParams = new JSONObject();
        selectParams.put("key", "shopCode,erpCode,entId");
        selectParams.put("values", list.stream().map(GoodsSpecPriceModel::getShopCodeKeyValue).collect(Collectors.toSet()));
        selectParams.put("table","shop");
        selectParams.put("field","concat('\\'',shopCode,'\\',\\'',erpCode,'\\',',entId) as mapkey, shopId as mapvalue");
        MapResultHandler shopMap = new MapResultHandler();
	    this.getTemplate().select("AdvancedQueryMapper.selectMap", selectParams,shopMap);
		Map<String,Long> shopMapper = shopMap.getMappedResults();
		//2.商品Id
		selectParams.clear();
		selectParams.put("key", "goodsCode,erpCode,entId");
        selectParams.put("values", list.stream().map(GoodsSpecPriceModel::getGoodsCodeKeyValue).collect(Collectors.toSet()));
        selectParams.put("table","goods");
        selectParams.put("field","concat('\\'',goodsCode,'\\',\\'',erpCode,'\\',',entId) as mapkey, sgid as mapvalue");
        MapResultHandler goodsCodeMap = new MapResultHandler();
	    this.getTemplate().select("AdvancedQueryMapper.selectMap", selectParams,goodsCodeMap);
		Map<String,Long> goodsCodeMapper = goodsCodeMap.getMappedResults();
		//统一设置Id
		list.stream().forEach(e ->{
			e.setShopId(shopMapper.get(e.getShopCodeKeyValue()));//设置门店Id
			e.setSgid(goodsCodeMapper.get(e.getGoodsCodeKeyValue()));//设置商品Id
		});  
		return list;
	}*/

}
