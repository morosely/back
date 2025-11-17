package com.efuture.component;


import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.efuture.common.CommonSyncService;
import com.efuture.common.MapResultHandler;
import com.efuture.common.SpringUtil;
import com.efuture.model.GoodsModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class GoodsServiceImpl extends CommonSyncService<GoodsModel,GoodsServiceImpl> {

	public GoodsServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}

	//初始化数据
	@Override
	public List<GoodsModel> initData(List<GoodsModel> list) {
		JSONObject selectParams = new JSONObject();
		// 1.设置品类Id
		selectParams.clear();
		selectParams.put("key", "categoryCode,erpCode,entId,level");
		selectParams.put("values", list.stream().map(GoodsModel::getCategoryKeyValue).collect(Collectors.toSet()));
		selectParams.put("table", "category");
		selectParams.put("field", "concat('\\'',categoryCode,'\\',\\'',erpCode,'\\',',entId,',',level) as mapkey, categoryId as mapvalue");
		MapResultHandler categoryCodeMap = new MapResultHandler();
		this.template.select("AdvancedQueryMapper.selectMap", selectParams, categoryCodeMap);
		Map<String,Long> categoryCodeMapper = categoryCodeMap.getMappedResults();

		// 2.设置品牌Id
		selectParams.clear();
		selectParams.put("key", "brandCode,erpCode,entId");
		selectParams.put("values", list.stream().map(GoodsModel::getBrandCodeKeyValue).collect(Collectors.toSet()));
		selectParams.put("table", "brandinfo");
		selectParams.put("field", "concat('\\'',brandCode,'\\',\\'',erpCode,'\\',',entId) as mapkey, brandId as mapvalue");
		MapResultHandler brandCodeCodeMap = new MapResultHandler();
		this.template.select("AdvancedQueryMapper.selectMap", selectParams, brandCodeCodeMap);
		Map<String,Long> brandCodeCodeMapper = brandCodeCodeMap.getMappedResults();

//		// 3.特殊商品
//		selectParams.clear();
//		selectParams.put("key", "goodsCode");
//		selectParams.put("values", list.stream().map(GoodsModel::getGoodsCode).collect(Collectors.toSet()));
//		selectParams.put("table", "goodsspecial");
//		selectParams.put("field", "goodsCode as mapkey, goodsType as mapvalue");
//		MapResultHandler goodsTypeMap = new MapResultHandler();
//		this.template.select("AdvancedQueryMapper.selectMap", selectParams, goodsTypeMap);
//		Map<String,Integer> goodsTypeMapper = goodsTypeMap.getMappedResults();

		for (GoodsModel model : list) {
			model.setDirectFromErp(true);//是否直接来源ERP 0-否/1-是
			model.setSingleItemFlag(true);//默认设置单品状态
			if (StringUtils.isEmpty(model.getBarNo())) {//如果条码为空，编码赋值给条码
				model.setBarNo(model.getGoodsCode());
			}
			model.setDeliveryFlag((short) 0);//不读取profit数据，初始化为0，无需更新此字段
			// 处理goodsName的/
			if (!StringUtils.isEmpty(model.getGoodsName())) {
				String goodsName = replaceBlank(model.getGoodsName());
				model.setGoodsName(goodsName);
			}
			if (!StringUtils.isEmpty(model.getGoodsDisplayName())) {
				String goodsDisplayName = replaceBlank(model.getGoodsDisplayName());
				model.setGoodsDisplayName(goodsDisplayName);
			}
			if (!StringUtils.isEmpty(model.getEnSname())) {
				String enSname = replaceBlank(model.getEnSname());
				model.setEnSname(enSname);
			}
			if (!StringUtils.isEmpty(model.getEnFname())) {
				String enFname = replaceBlank(model.getEnFname());
				model.setEnFname(enFname);
			}
			if (!StringUtils.isEmpty(model.getFullName())) {
				String fullName = replaceBlank(model.getFullName());
				model.setFullName(fullName);
			}
			//同步时 saleSpec去除非法字符
			if (!StringUtils.isEmpty(model.getSaleSpec())) {
				String SaleSpec = replaceBlank(model.getSaleSpec());
				model.setSaleSpec(SaleSpec);
			}
			// 1.设置品类Id
			if (!categoryCodeMapper.isEmpty()) {
				model.setCategoryId(categoryCodeMapper.get(model.getCategoryKeyValue()));
			}
			// 2.设置品牌Id
			if (!brandCodeCodeMapper.isEmpty()) {
				Long brandId = brandCodeCodeMapper.get(model.getBrandCodeKeyValue());
				if(brandId != null){
					model.setBrandId(brandId);
				}else if("0".equals(model.getBrandCode())){
					//特殊处理:品牌编码如果品牌表没有brandCode为0，将brandId默认为0
					model.setBrandId(0l);
				}
			}

//			// 3.设置特殊商品类型
//			if (!goodsTypeMapper.isEmpty()) {
//				Integer goodsType = goodsTypeMapper.get(model.getGoodsCode());
//				if(goodsType != null){
//					//同步代码不更新商品的goodsType字段,因此在此更新
//					JSONObject param = new JSONObject();
//					param.put("goodsCode",model.getGoodsCode());
//					param.put("goodsType",goodsType);
//					int count = this.getSqlSessionTemplate().update("GoodsModelMapper.updateGoodsCodeSpecial",param);
//					this.logger.info("【商品同步】==========》》》更新特殊商品的类型:goodsCode:{},goodsType:{}",model.getGoodsCode(),goodsType);
//				}
//			}
		}
		return list;
	}

	//通过Code设置Id
    @Override
    public void resetIdByCode(){
    	//设置品类Id
		this.logger.info("【商品同步】1.==========》》》设置品类Id Start...");
		long start = System.currentTimeMillis();
    	Map<String,String> paramMap = new HashMap<String,String>();
    	paramMap.put("targetTable", "goods");
    	paramMap.put("sourceTable", "category");
    	paramMap.put("targetCode", "categoryCode");
    	paramMap.put("sourceCode", "categoryCode");
    	paramMap.put("targetId", "categoryId");
    	paramMap.put("sourceId", "categoryId");
    	//int updateIdCount = this.getSqlSessionTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
    	//this.logger.info("==========》》》设置品类Id。更新数量："+updateIdCount);
		MapResultHandler categoryCodeMap = new MapResultHandler();
		this.getSqlSessionTemplate().select("AdvancedQueryMapper.selectIdByCode",paramMap,categoryCodeMap);
		Map<String,Long> categoryCodeMapper = categoryCodeMap.getMappedResults();
		int totalUpdateCount = 0;
		if(categoryCodeMapper!=null && categoryCodeMapper.size()>0){
			for(String key:categoryCodeMapper.keySet()){
				String value = categoryCodeMapper.get(key).toString();
				if(key!=null && key.trim().length()>0 && value!=null && value.trim().length()>0){
					paramMap.put("key",key);
					paramMap.put("value",value);
					int oneUpdateCount = this.getSqlSessionTemplate().update("AdvancedQueryMapper.singleUpdateIdByCode",paramMap);
					totalUpdateCount = totalUpdateCount + oneUpdateCount;
					this.logger.info("【商品同步】1.==========》》》同步补偿刷新设置品类Id：categoryId:{},categoryCode:{},更新数量:{}",value,key,oneUpdateCount);
				}
			}
		}
		long end = System.currentTimeMillis();
		this.logger.info("【商品同步】1.==========》》》设置品类Id End... 更新总数量:{},时间:{}",totalUpdateCount,(end-start));

    	//2.设置品牌Id
		this.logger.info("【商品同步】2.==========》》》设置品牌Id Start...");
		start = System.currentTimeMillis();
    	paramMap.put("targetTable", "goods");
    	paramMap.put("sourceTable", "brandinfo");
    	paramMap.put("targetCode", "brandCode");
    	paramMap.put("sourceCode", "brandCode");
    	paramMap.put("targetId", "brandId");
    	paramMap.put("sourceId", "brandId");
    	//int updateBrandIdCount = this.getTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
    	//this.logger.info("==========》》》设置品牌Id。更新数量："+updateBrandIdCount);
		MapResultHandler brandInfoMap = new MapResultHandler();
		this.getSqlSessionTemplate().select("AdvancedQueryMapper.selectIdByCode",paramMap,brandInfoMap);
		Map<String,Long> brandInfoMapper = brandInfoMap.getMappedResults();
		totalUpdateCount = 0;
		if(brandInfoMapper!=null && brandInfoMapper.size()>0){
			for(String key:brandInfoMapper.keySet()){
				String value = brandInfoMapper.get(key).toString();
				if(key!=null && key.trim().length()>0 && value!=null && value.trim().length()>0){
					paramMap.put("key",key);
					paramMap.put("value",value);
					int oneUpdateCount = this.getSqlSessionTemplate().update("AdvancedQueryMapper.singleUpdateIdByCode",paramMap);
					totalUpdateCount = totalUpdateCount + oneUpdateCount;
					this.logger.info("【商品同步】2.==========》》》同步补偿刷新设置设置品牌Id：brandId:{},brandCode:{},更新数量:{}",value,key,oneUpdateCount);
				}
			}
		}
		end = System.currentTimeMillis();
		this.logger.info("【商品同步】2.==========》》》设置品牌Id End... 更新总数量:{},时间:{}",totalUpdateCount,(end-start));

    	//3.设置商品门店库存属性shopSheetType--(为-1时默认取类别那边属性)
		//int updateShopSheetTypeCount = this.getSqlSessionTemplate().update("GoodsModelMapper.updateShopSheetType");
		//this.logger.info("【商品同步】==========》》》设置商品门店库存属性shopSheetType。更新数量：" + updateShopSheetTypeCount);
		this.logger.info("【商品同步】3.==========》》》设置商品门店库存属性shopSheetType Start...");
		start = System.currentTimeMillis();
		Integer count = this.getSqlSessionTemplate().selectOne("GoodsModelMapper.countShopSheetType");
		if(count > 0){
			int updateShopSheetTypeCount = this.getSqlSessionTemplate().update("GoodsModelMapper.updateShopSheetType");
			this.logger.info("【商品同步】3.==========》》》设置商品门店库存属性shopSheetType。更新数量：" + updateShopSheetTypeCount);
		}
		end = System.currentTimeMillis();
		this.logger.info("【商品同步】3.==========》》》设置商品门店库存属性shopSheetType End... 更新总数量:{},时间:{}",totalUpdateCount,(end-start));

		//4.更新特殊商品的类型
		//add by yihaitao 2024-01-10 优化更新特殊类型商品类型（先查询结果后比对，比对有差异再去更新）
		List<JSONObject> goodsTypes = this.getSqlSessionTemplate().selectList("GoodsModelMapper.selectGoodsSpecial");
		Integer updateGoodsSpecialCount = -1;
		if(goodsTypes!=null && !goodsTypes.isEmpty()){
			for (int i = 0; i < goodsTypes.size(); i++) {
				JSONObject jsonObject = goodsTypes.get(i);
				String specialType = jsonObject.getString("specialType");
				String goodsType = jsonObject.getString("goodsType");
				if(specialType!=null && !specialType.equals(goodsType)){
					updateGoodsSpecialCount = this.getSqlSessionTemplate().update("GoodsModelMapper.updateGoodsSpecial");
					break;
				}
			}
		}
		this.logger.info("【商品同步】4.==========》》》更新特殊商品的类型。(-1表示无需执行更新SQL语句) 更新数量：" + updateGoodsSpecialCount);

		//增加是否受限标志开关：默认false：不自动更新受限品类下新增的商品，商品受限商品标识从DMC获取
		this.logger.info("【商品同步】5.==========》》》是否受限标志开关：【seasonFlag】 --- " + seasonFlag);
		if(seasonFlag){
			//5.新增商品更新受限标识
			int updateRestrictedCount =  this.getSqlSessionTemplate().update("GoodsModelMapper.updateRestricted");
			this.logger.info("【商品同步】5.==========》》》新增商品更新受限标识。更新数量：" + updateRestrictedCount);
		}
		//6.当商品所属品类发生改变时，重新调用库存推送接口为海外购中台提供可售库存
//	   if(abroadBuyFlag) {//海外购标识
//			this.getTemplate().delete("GoodsModelMapper.deleteSevenGoodsStock", null);
//			int updateIdCount = this.getTemplate().update("GoodsModelMapper.updateGoodsStockCateId", null);
//	    	this.logger.info("【商品同步】6.==========》》》当商品所属品类发生改变时，重新调用库存推送接口为海外购中台提供可售库存。刷新GoodsStock品类ID。更新数量："+updateIdCount);
//	   }
	}

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	public final static String ClassCodeKey = "omdmain:sync:minDiscount:classCode";
	public final static String ArtiCodeKey ="omdmain:sync:minDiscount:artiCode";
	@Override
	//select categoryCode,level from category c where c.`level` = 5 and c.parentCode in (select m.categoryCode from mindiscount m where m.level = 4);
	public void specialData(List<GoodsModel> insertData,List<GoodsModel> updateData,List<GoodsModel> DBdata) {
		try {
			if(insertData != null && insertData.size() > 0) {
				this.logger.info("【商品同步】商品折扣率 新增 ---------->>>>> start ");
				for(GoodsModel goods:insertData){
					Object minDiscount = this.minDiscountFromRedis(goods.getCategoryCode());
					if(minDiscount!=null){
						goods.setMinDiscount(Float.valueOf(minDiscount.toString()));
						int flag = template.update("GoodsModelMapper.updateMinDiscount",JSONObject.toJSON(goods));
						this.logger.info("【商品同步】商品折扣率 新增商品:{},折扣率:{},品类:{} ---------->>>>> 是否成功:{}",goods.getGoodsCode(),goods.getMinDiscount(),goods.getCategoryCode(),flag);
					}
				}
				this.logger.info("【商品同步】商品折扣率 新增 <<<<<---------- end");
			}
			//屏蔽商品更换品类，自动更新后品类折扣率
			/*if(updateData != null && updateData.size() > 0 && DBdata != null && DBdata.size() > 0) {
				List<String> dbGoodsCategoryKeyList = DBdata.stream().map(GoodsModel::getGoodsCategoryKeyValue).collect(Collectors.toList());
				List<GoodsModel> sameCategoryList = updateData.stream().filter(model -> dbGoodsCategoryKeyList.contains(model.getGoodsCategoryKeyValue())).collect(Collectors.toList());
				updateData.removeAll(sameCategoryList);//品类不同集合=更新全集合-品类相同集合
				if(updateData.size() > 0) {
					this.logger.info("【商品同步】商品折扣率 (商品品类变更) ---------->>>>> start ");
					for(GoodsModel goods:updateData){
						Object minDiscount = this.minDiscountFromRedis(goods.getCategoryCode());
						if(minDiscount!=null){
							goods.setMinDiscount(Float.valueOf(minDiscount.toString()));
							int flag = template.update("GoodsModelMapper.updateMinDiscount",JSONObject.toJSON(goods));
							this.logger.info("【商品同步】商品折扣率 (商品品类变更)商品:{},折扣率:{},品类:{} ---------->>>>> 是否成功:{}",goods.getGoodsCode(),goods.getMinDiscount(),goods.getCategoryCode(),flag);
						}
					}
					this.logger.info("【商品同步】商品折扣率 更新(品类) <<<<<---------- end ");
				}
			}*/
		}catch (Exception e) {
			this.logger.error(" 【商品同步】==========》》》specialData方法发生异常 ",e);
			e.printStackTrace();
		}
	}

	@Override
	public void specialSingleInsertData(GoodsModel goods){
		if(goods!=null){
			Object minDiscount = this.minDiscountFromRedis(goods.getCategoryCode());
			if(minDiscount!=null){
				goods.setMinDiscount(Float.valueOf(minDiscount.toString()));
				int flag = template.update("GoodsModelMapper.updateMinDiscount",JSONObject.toJSON(goods));
				this.logger.info("【商品同步】商品折扣率 新增商品:{},折扣率:{},品类:{} ---------->>>>> 是否成功:{}",goods.getGoodsCode(),goods.getMinDiscount(),goods.getCategoryCode(),flag);
			}
		}
	}

	//获取折扣率
    private Object minDiscountFromRedis(String categoryCode){
		Map classCodeRedis = stringRedisTemplate.opsForHash().entries(ClassCodeKey);
		if(classCodeRedis!=null && classCodeRedis.size()==0){
			initRedisClassCodeMincount();
		}
		Map artiCodeRedis = stringRedisTemplate.opsForHash().entries(ArtiCodeKey);
		if(artiCodeRedis!=null && artiCodeRedis.size()==0){
			initRedisArtiCodeMincount();
		}
		classCodeRedis = stringRedisTemplate.opsForHash().entries(ClassCodeKey);
		artiCodeRedis = stringRedisTemplate.opsForHash().entries(ArtiCodeKey);
		if(classCodeRedis!=null){
			//先从ClassCode缓存查找，查找不到再从ArtiCode缓存查找
			Object minDiscount = classCodeRedis.get(categoryCode);
			if(minDiscount == null && artiCodeRedis!=null){
				minDiscount = artiCodeRedis.get(categoryCode);
			}
			return minDiscount;
		}
		return null;
	}

	/**
	 * ClassCode级别的折扣率初始化redis
	 * select categoryCode,minDiscount from mindiscount m where m.`level` = 5;
	 */
	private void initRedisClassCodeMincount(){
		StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) SpringUtil.getBean(StringRedisTemplate .class);
		MapResultHandler classCodeMindis = new MapResultHandler();
		template.select("AdvancedQueryMapper.classCodeMinDiscount",classCodeMindis);
		Map<String,Object> classCodeMap = classCodeMindis.getMappedResults();
		classCodeMap.forEach((k, v) -> classCodeMap.put(k,String.valueOf(v)));
		stringRedisTemplate.opsForHash().putAll(ClassCodeKey,classCodeMap);
		stringRedisTemplate.expire(ClassCodeKey,1, TimeUnit.HOURS);
	}

	/**
	 * 将ArtiCode对应的ClassCode折扣率初始化redis
	 * select c.categoryCode,m.minDiscount from mindiscount m join category c on m.categoryCode = c.parentCode and m.level = 4 and c.`level` = 5
	 */
	private void initRedisArtiCodeMincount(){
		StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) SpringUtil.getBean(StringRedisTemplate .class);
		MapResultHandler artiCodeMindis = new MapResultHandler();
		template.select("AdvancedQueryMapper.artiCodeMinDiscount",artiCodeMindis);
		Map<String,Object> artiCodeMap = artiCodeMindis.getMappedResults();
		artiCodeMap.forEach((k, v) -> artiCodeMap.put(k,String.valueOf(v)));
		stringRedisTemplate.opsForHash().putAll(ArtiCodeKey,artiCodeMap);
		stringRedisTemplate.expire(ArtiCodeKey,1, TimeUnit.HOURS);
	}

    //当商品所属品类发生改变时，重新调用库存推送接口为海外购中台提供可售库存。
    /*//add by yihaitao 2022-12-06 代码貌似从未使用过
    @Override
    public void specialData(List<GoodsModel> insertData,List<GoodsModel> updateData,List<GoodsModel> DBdata) {
    	this.logger.info("start ==========》》》当商品所属品类发生改变时，重新调用库存推送接口为海外购中台提供可售库存 ");
    	try {
    		if(insertData != null && insertData.size() > 0) {
    			this.logger.info("start ---------->>>>> 新增商品 ");
    			template.insert("GoodsModelMapper.goodsStock",insertData);
    			this.logger.info("end <<<<<---------- 新增商品 :  "+insertData.size());
    		}
    		
    		if(updateData != null && updateData.size() > 0 && DBdata != null && DBdata.size() > 0) {
    			List<String> dbGoodsCategoryKeyList = DBdata.stream().map(GoodsModel::getGoodsCategoryKeyValue).collect(Collectors.toList());
    			List<GoodsModel> sameCategoryList = updateData.stream().filter(model -> dbGoodsCategoryKeyList.contains(model.getGoodsCategoryKeyValue())).collect(Collectors.toList());
    			updateData.removeAll(sameCategoryList);//品类不同集合=更新全集合-品类相同集合
    			if(updateData.size() > 0) {
    				template.update("CategoryModelMapper.updateGoodsCategory",updateData);
    				template.insert("GoodsModelMapper.goodsStock",updateData);
    				this.logger.info("---------->>>>> 商品品类修改商品 :  "+updateData.size());
    			}
    		}
    	}catch (Exception e) {
			this.logger.error("--------------------》》》当商品所属品类发生改变时，重新调用库存推送接口为海外购中台提供可售库存产生异常。。。\n"+e.getLocalizedMessage());
			e.printStackTrace();
		}
    	this.logger.info("end 《《《========== 当商品所属品类发生改变时，重新调用库存推送接口为海外购中台提供可售库存 ");
    }*/
}
