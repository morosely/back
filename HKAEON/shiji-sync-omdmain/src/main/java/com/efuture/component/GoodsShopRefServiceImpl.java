package com.efuture.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.common.CommonSyncService;
import com.efuture.common.MapResultHandler;
import com.efuture.model.GoodsShopRefModel;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author yihaitao
* @time 2018年5月18日 上午11:16:14 
*
*/
public class GoodsShopRefServiceImpl extends CommonSyncService<GoodsShopRefModel,GoodsShopRefServiceImpl> {
	public GoodsShopRefServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}


	List validStatus = Arrays.asList(1,2,3,4,5,10);
	List invalidStatus = Arrays.asList(0,6,7,8,9);
	@Override
	public List<GoodsShopRefModel> initData(List<GoodsShopRefModel> list) {
		//1.门店Id
		JSONObject selectParams = new JSONObject();
		selectParams.put("key", "shopCode,erpCode,entId");
		selectParams.put("values", list.stream().map(GoodsShopRefModel::getShopCodeKeyValue).collect(Collectors.toSet()));
		selectParams.put("table","shop");
		selectParams.put("field","concat('\\'',shopCode,'\\',\\'',erpCode,'\\',',entId) as mapkey, shopId as mapvalue");
		MapResultHandler shopMap = new MapResultHandler();
		this.getSqlSessionTemplate().select("AdvancedQueryMapper.selectMap", selectParams,shopMap);
		Map<String,Long> shopMapper = shopMap.getMappedResults();

		//2.档口Id
		/*selectParams.clear();
		selectParams.put("key", "stallCode,erpCode,entId");
		selectParams.put("values", list.stream().map(GoodsShopRefModel::getStallCodeKeyValue).collect(Collectors.toSet()));
		selectParams.put("table","stallinfo");
		selectParams.put("field","concat('\\'',stallCode,'\\',\\'',erpCode,'\\',',entId) as mapkey, siid as mapvalue");
		MapResultHandler stallinfoMap = new MapResultHandler();
		this.getSqlSessionTemplate().select("AdvancedQueryMapper.selectMap", selectParams,stallinfoMap);
		Map<String,Long> stallinfoMapper = stallinfoMap.getMappedResults();*/

		//3.柜组Id
		/*selectParams.clear();
		selectParams.put("key", "orgCode,erpCode,entId");
        selectParams.put("values", list.stream().map(GoodsShopRefModel::getOrgCodeKeyValue).collect(Collectors.toSet()));
        selectParams.put("table","saleorg");
        selectParams.put("field","concat('\\'',orgCode,'\\',\\'',erpCode,'\\',',entId) as mapkey, saleOrgId as mapvalue");
        MapResultHandler saleorgMap = new MapResultHandler();
	    this.getSqlSessionTemplate().select("AdvancedQueryMapper.selectMap", selectParams,saleorgMap);
		Map<String,Long> saleorgMapper = saleorgMap.getMappedResults();*/

		//4.商品Id
		selectParams.clear();
		selectParams.put("key", "goodsCode,erpCode,entId");
		selectParams.put("values", list.stream().map(GoodsShopRefModel::getGoodsCodeKeyValue).collect(Collectors.toSet()));
		selectParams.put("table","goods");
		selectParams.put("field","concat('\\'',goodsCode,'\\',\\'',erpCode,'\\',',entId) as mapkey, sgid as mapvalue");
		MapResultHandler goodsCodeMap = new MapResultHandler();
		this.getSqlSessionTemplate().select("AdvancedQueryMapper.selectMap", selectParams,goodsCodeMap);
		Map<String,Long> goodsCodeMapper = goodsCodeMap.getMappedResults();

		//5.供应商Id
		selectParams.clear();
		selectParams.put("key", "venderCode,erpCode,entId");
		selectParams.put("values", list.stream().map(GoodsShopRefModel::getVenderCodeKeyValue).collect(Collectors.toSet()));
		selectParams.put("table","vender");
		selectParams.put("field","concat('\\'',venderCode,'\\',\\'',erpCode,'\\',',entId) as mapkey, vid as mapvalue");
		MapResultHandler venderCodeMap = new MapResultHandler();
		this.getSqlSessionTemplate().select("AdvancedQueryMapper.selectMap", selectParams,venderCodeMap);
		Map<String,Long> venderCodeMapper = venderCodeMap.getMappedResults();

		//统一设置Id
		list.stream().forEach(e ->{
			//e.setGoodStatus(1);
			Integer status = e.getGoodStatus();
			if(validStatus.contains(status)){
				e.setGoodStatus(1);
			}else if(invalidStatus.contains(status)){
				e.setGoodStatus(-1);
			}
			e.setShopId(shopMapper.get(e.getShopCodeKeyValue()));//设置门店Id
			//e.setSiid(stallinfoMapper.get(e.getStallCodeKeyValue()));//设置档口Id
			//e.setSaleOrgId(saleorgMapper.get(e.getOrgCodeKeyValue()));//设置柜组Id
			e.setSgid(goodsCodeMapper.get(e.getGoodsCodeKeyValue()));//设置商品Id
			e.setVid(venderCodeMapper.get(e.getVenderCodeKeyValue()));//设置供应商Id
		});
		return list;
	}


	//优化方案:先查询后，有结果再更新
	@Override
	public void resetIdByCode(){
		//1.商品Id
		Map<String,Object> paramMap = new HashMap<String,Object>();
		this.logger.info("【经营配置同步】1.==========》》》设置商品Id Start...");
		long start = System.currentTimeMillis();
		paramMap.put("targetTable", "goodsshopref");
		paramMap.put("sourceTable", "goods");
		paramMap.put("targetCode", "goodsCode");
		paramMap.put("sourceCode", "goodsCode");
		paramMap.put("targetId", "sgid");
		paramMap.put("sourceId", "sgid");
		//select sgid,goodsCode from goods where goodsCode in (select goodsCode from goodsshopref where sgid is null);
		//int updateSgidCount = this.getTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
		//上述SQL容易锁表，优化成查询后有数据再更新
		MapResultHandler goodsCodeMap = new MapResultHandler();
		this.getSqlSessionTemplate().select("AdvancedQueryMapper.selectIdByCode",paramMap,goodsCodeMap);
		Map<String,Long> goodsCodeMapper = goodsCodeMap.getMappedResults();
		int totalUpdateCount = 0;
		if(goodsCodeMapper!=null && goodsCodeMapper.size()>0){
			for(String key:goodsCodeMapper.keySet()){
				String value = goodsCodeMapper.get(key).toString();
				if(key!=null && key.trim().length()>0 && value!=null && value.trim().length()>0){
					paramMap.put("key",key);
					paramMap.put("value",value);
					int oneUpdateCount = this.getSqlSessionTemplate().update("AdvancedQueryMapper.singleUpdateIdByCode",paramMap);
					totalUpdateCount = totalUpdateCount + oneUpdateCount;
					this.logger.info("【经营配置同步】1.==========》》》补偿刷新设置商品Id：sgid:{},goodsCode:{},更新数量:{}",value,key,oneUpdateCount);
				}
			}
		}
		long end = System.currentTimeMillis();
		this.logger.info("【经营配置同步】1.==========》》》设置商品Id End... 更新总数量:{},时间:{}",totalUpdateCount,(end-start));

		//2.设置门店Id
		this.logger.info("【经营配置同步】2.==========》》》设置门店Id Start...");
		totalUpdateCount = 0;
		start = System.currentTimeMillis();
		paramMap.put("targetTable", "goodsshopref");
		paramMap.put("sourceTable", "shop");
		paramMap.put("targetCode", "shopCode");
		paramMap.put("sourceCode", "shopCode");
		paramMap.put("targetId", "shopId");
		paramMap.put("sourceId", "shopId");
		//int updateShopIdCount = this.getTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
		MapResultHandler shopMap = new MapResultHandler();
		this.getSqlSessionTemplate().select("AdvancedQueryMapper.selectIdByCode",paramMap,shopMap);
		Map<String,Long> shopMapper = shopMap.getMappedResults();
		totalUpdateCount = 0;
		if(shopMapper!=null && shopMapper.size()>0){
			for(String key:shopMapper.keySet()){
				String value = shopMapper.get(key).toString();
				if(key!=null && key.trim().length()>0 && value!=null && value.trim().length()>0){
					paramMap.put("key",key);
					paramMap.put("value",value);
					int oneUpdateCount = this.getSqlSessionTemplate().update("AdvancedQueryMapper.singleUpdateIdByCode",paramMap);
					totalUpdateCount = totalUpdateCount + oneUpdateCount;
					this.logger.info("【经营配置同步】2.==========》》》补偿刷新设置设置门店Id：shopId:{},shopCode:{},更新数量:{}",value,key,oneUpdateCount);
				}
			}
		}
		end = System.currentTimeMillis();
		this.logger.info("【经营配置同步】2.==========》》》设置门店Id End... 更新总数量:{},时间:{}",totalUpdateCount,(end-start));

		//3.供应商Id
		this.logger.info("【经营配置同步】3.==========》》》设置供应商Id Start...");
		start = System.currentTimeMillis();
		paramMap.put("targetTable", "goodsshopref");
		paramMap.put("sourceTable", "vender");
		paramMap.put("targetCode", "venderCode");
		paramMap.put("sourceCode", "venderCode");
		paramMap.put("targetId", "vid");
		paramMap.put("sourceId", "vid");
		//int updateVidCount = this.getTemplate().update("AdvancedQueryMapper.updateIdByCode",paramMap);
		MapResultHandler venderMap = new MapResultHandler();
		this.getSqlSessionTemplate().select("AdvancedQueryMapper.selectIdByCode",paramMap,venderMap);
		Map<String,Long> venderCodeMapper = venderMap.getMappedResults();
		totalUpdateCount = 0;
		if(venderCodeMapper!=null && venderCodeMapper.size()>0){
			for(String key:venderCodeMapper.keySet()){
				String value = venderCodeMapper.get(key).toString();
				if(key!=null && key.trim().length()>0 && value!=null && value.trim().length()>0){
					paramMap.put("key",key);
					paramMap.put("value",value);
					int oneUpdateCount = this.getSqlSessionTemplate().update("AdvancedQueryMapper.singleUpdateIdByCode",paramMap);
					totalUpdateCount = totalUpdateCount + oneUpdateCount;
					this.logger.info("【经营配置同步】3.==========》》》补偿刷新设置供应商Id：vid:{},venderCode:{},更新数量:{}",value,key,oneUpdateCount);
				}
			}
		}
		end = System.currentTimeMillis();
		this.logger.info("【经营配置同步】3.==========》》》设置供应商Id End。更新总数量:{},时间:{}",totalUpdateCount,(end-start));

		//4定制化需求：初始化goods的最大价格saleprice字段
//		this.logger.info("【经营配置同步】4.==========》》》初始化goods的saleprice字段 Start...");
//		start = System.currentTimeMillis();
//		int initGoodsSalePriceCount = this.getTemplate().update("GoodsModelMapper.initGoodsSalePrice");
//		end = System.currentTimeMillis();
//		this.logger.info("【经营配置同步】4.==========》》》初始化goods的saleprice字段End... 更新数量:{},时间:{}",initGoodsSalePriceCount,(end-start));
	}

	//通过Code设置Id
    /*@Override
    public void resetIdByCode(){
		//1.商品Id
		Map<String,String> paramMap = new HashMap<String,String>();
    	this.logger.info("==========》》》设置商品Id Start...");
    	long start = System.currentTimeMillis();
    	paramMap.put("targetTable", "goodsshopref");
    	paramMap.put("sourceTable", "goods");
    	paramMap.put("targetCode", "goodsCode");
    	paramMap.put("sourceCode", "goodsCode");
    	paramMap.put("targetId", "sgid");
    	paramMap.put("sourceId", "sgid");
    	int updateSgidCount = this.getTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
    	long end = System.currentTimeMillis();
    	this.logger.info("==========》》》设置商品Id End... 更新数量:{},时间:{}",updateSgidCount,(end-start));
		
    	//2.设置门店Id
    	this.logger.info("==========》》》设置门店Id Start...");
    	start = System.currentTimeMillis();
    	paramMap.put("targetTable", "goodsshopref");
    	paramMap.put("sourceTable", "shop");
    	paramMap.put("targetCode", "shopCode");
    	paramMap.put("sourceCode", "shopCode");
    	paramMap.put("targetId", "shopId");
    	paramMap.put("sourceId", "shopId");
    	int updateShopIdCount = this.getTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
    	end = System.currentTimeMillis();
    	this.logger.info("==========》》》设置门店Id End... 更新数量:{},时间:{}",updateShopIdCount,(end-start));
    	
    	//3.设置档口Id
    	*//*this.logger.info("==========》》》设置档口Id Start...");
    	start = System.currentTimeMillis();
    	paramMap.put("targetTable", "goodsshopref");
    	paramMap.put("sourceTable", "stallinfo");
    	paramMap.put("targetCode", "stallCode");
    	paramMap.put("sourceCode", "stallCode");
    	paramMap.put("targetId", "siid");
    	paramMap.put("sourceId", "siid");
    	int updateSiidCount = this.getTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
    	end = System.currentTimeMillis();
    	this.logger.info("==========》》》设置档口Id End... 更新数量:{},时间:{}",updateSiidCount,(end-start));*//*

    	//4.柜组Id
    	*//*this.logger.info("==========》》》设置柜组Id Start...");
    	start = System.currentTimeMillis();
    	paramMap.put("targetTable", "goodsshopref");
    	paramMap.put("sourceTable", "saleorg");
    	paramMap.put("targetCode", "orgCode");
    	paramMap.put("sourceCode", "orgCode");
    	paramMap.put("targetId", "saleOrgId");
    	paramMap.put("sourceId", "saleOrgId");
    	int updateSaleOrgIdCount = this.getTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
    	end = System.currentTimeMillis();
    	this.logger.info("==========》》》设置柜组Id End... 更新数量:{},时间:{}",updateSaleOrgIdCount,(end-start));*//*
    	
    	//5.供应商Id
    	this.logger.info("==========》》》设置供应商Id Start...");
    	start = System.currentTimeMillis();
    	paramMap.put("targetTable", "goodsshopref");
    	paramMap.put("sourceTable", "vender");
    	paramMap.put("targetCode", "venderCode");
    	paramMap.put("sourceCode", "venderCode");
    	paramMap.put("targetId", "vid");
    	paramMap.put("sourceId", "vid");
    	int updateVidCount = this.getTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
    	end = System.currentTimeMillis();
    	this.logger.info("==========》》》设置供应商Id End。更新数量:{},时间:{}",updateVidCount,(end-start));
    	
    	//6.定制化需求：初始化goods的最大价格saleprice字段
		*//*
    	this.logger.info("==========》》》初始化goods的saleprice字段 Start...");
    	start = System.currentTimeMillis();
    	int initGoodsSalePriceCount = this.getTemplate().update("GoodsModelMapper.initGoodsSalePrice");
    	end = System.currentTimeMillis();
    	this.logger.info("==========》》》初始化goods的saleprice字段End... 更新数量:{},时间:{}",initGoodsSalePriceCount,(end-start));
    	*//*
	}*/

}
