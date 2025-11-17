package com.efuture.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.common.CommonSyncService;
import com.efuture.common.MapResultHandler;
import com.efuture.model.CategoryModel;
import com.efuture.utils.HttpUtils;
import com.product.model.ServiceSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by huangzhengwei on 2018/5/2.
 *
 * @Desciption:
 */
public class CategoryServiceImpl extends CommonSyncService<CategoryModel,CategoryServiceImpl> {

    private final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Value("${category.sync.redis}")
    String redisURL;
    
    @Value("${stock.oms.safestockCategory.categoryInit}")
    private String safeStockURL;
    
    private volatile boolean isClear = false;

    public CategoryServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }

    @Override
    public void onSyncRedis() {
        ServiceSession session = new ServiceSession();
        session.setEnt_id(0);
        JSONObject param = new JSONObject();
        param.put("entId", 0);
        param.put("erpCode", "002");

        try {
            LOGGER.info("调用主数据接口，同步redis，表:{}", getTableName());
            HttpUtils.onPost(redisURL, session, param.toJSONString());
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

	@Override
	public List<CategoryModel> initData(List<CategoryModel> list) {
        //修复更改品类的parentCode，parentID无法自动更新Bug
        JSONObject selectParams = new JSONObject();
        selectParams.clear();
        selectParams.put("key", "categoryCode,erpCode,entId,level");
        selectParams.put("values", list.stream().map(CategoryModel::getParentCodeKeyValue).collect(Collectors.toSet()));
        selectParams.put("table", "category");
        selectParams.put("field",
                "concat('\\'',categoryCode,'\\',\\'',erpCode,'\\',',entId,',',level) as mapkey, categoryId as mapvalue");
        MapResultHandler categoryCodeMap = new MapResultHandler();
        this.template.select("AdvancedQueryMapper.selectMap", selectParams, categoryCodeMap);
        Map<String, Long> categoryCodeMapper = categoryCodeMap.getMappedResults();

        for (CategoryModel model : list) {
            if (!categoryCodeMapper.isEmpty()) {
                model.setParentId(categoryCodeMapper.get(model.getParentCodeKeyValue()));
            }
        }
        return list;
	}
	
	//通过Code设置Id
    @Override
    public void resetIdByCode(){
    	//设置父类Id
    	/*Map<String,String> paramMap = new HashMap<String,String>();
    	paramMap.put("targetTable", "category");
    	paramMap.put("sourceTable", "category");
    	paramMap.put("targetCode", "parentCode");
    	paramMap.put("sourceCode", "categoryCode");
    	paramMap.put("targetId", "parentId");
    	paramMap.put("sourceId", "categoryId");
    	int updateIdCount = this.getTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);*/
    	int updateIdCount = this.getTemplate().update("CategoryModelMapper.updateIdByCode", null);
    	this.logger.info("==========》》》设置父类Id。更新数量："+updateIdCount);
    	
    	//同步营销系统品类全路径
    	//this.getTemplate().update("CategoryModelMapper.initAllCategory", null);

    	/*add by yihaitao 2022-12-06 代码貌似从未使用过
    	if(abroadBuyFlag) {//海外购标识
	    	this.getTemplate().delete("CategoryModelMapper.deleteSeven", null);
	    	updateIdCount = this.getTemplate().update("CategoryModelMapper.updateCateStockPid", null);
	    	this.logger.info("==========》》》新增品类库存设置父类Id。更新数量："+updateIdCount);
    	}*/
    }

    @Override
    public void changeDataAfter(){
        initAllCategory();
        this.logger.info("==========》》》同步营销系统品类全路径已完成。。。");
    };

    @Transactional
    public void initAllCategory(){
        this.getTemplate().update("CategoryModelMapper.initAllCategory", null);
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public final static String ClassCodeKey = "omdmain:sync:minDiscount:classCode";
    public final static String ArtiCodeKey ="omdmain:sync:minDiscount:artiCode";
    @Override
    public void specialData(List<CategoryModel> insertData,List<CategoryModel> updateData,List<CategoryModel> DBdata) {
        try {
            if(insertData != null && insertData.size() > 0) {
                this.logger.info(" ==========》》》新增品类，清空缓存 ");
                stringRedisTemplate.delete(ClassCodeKey);
                stringRedisTemplate.delete(ArtiCodeKey);
            }
        }catch (Exception e){
            this.logger.error(" ==========》》》新增品类，清空缓存发生异常 ",e);
            e.printStackTrace();
        }
    }
    
    //调用安全库存品类初始化接口（只处理增量数据）
    /*//add by yihaitao 2022-12-06 代码貌似从未使用过
    @Override
    public void specialData(List<CategoryModel> insertData,List<CategoryModel> updateData,List<CategoryModel> DBdata) {
//    	this.logger.info("start ==========》》》调用新增品类进行品类安全库存的初始化接口");
//    	try {
//    		if(insertData != null && insertData.size() > 0) {
//    			ServiceSession session = new ServiceSession();
//    	        session.setEnt_id(0);
//    	        session.setErpCode("002");
//    	        JSONObject param = new JSONObject();
//    	        param.put("entId", 0);
//    	        param.put("erpCode", "002");
//    	        param.put("flag", "I");
//    	        param.put("detail",insertData);
//    			String result = HttpUtils.onPost(safeStockURL, session, param.toJSONString());
//    			this.logger.info("--------------------》》》【result】调用库存初始化接口返回结果"+result);
//    		}
//		} catch (Exception e) {
//			this.logger.error("--------------------》》》调用库存初始化接口异常。。。\n"+e.getLocalizedMessage());
//			e.printStackTrace();
//		}
//    	this.logger.info("end  《《《 ========== 调用新增品类进行品类安全库存的初始化接口");
    	this.logger.info("start ==========》》》调用新增品类进行品类插入临时表 ");
    	try {
    		if(insertData != null && insertData.size() > 0) {
    			template.insert("CategoryModelMapper.categoryStock",insertData);
    		}
    	}catch (Exception e) {
			this.logger.error("--------------------》》》插入新增品类临时表异常。。。\n"+e.getLocalizedMessage());
			e.printStackTrace();
		}
    	this.logger.info("end 《《《========== 调用新增品类进行品类插入临时表 ");
    }*/
}
