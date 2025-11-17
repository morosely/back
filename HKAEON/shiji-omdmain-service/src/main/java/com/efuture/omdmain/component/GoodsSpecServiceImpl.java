package com.efuture.omdmain.component;

import java.util.List;
import java.util.Map;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.GoodsSpecModel;
import com.efuture.omdmain.service.GoodsSpecService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class GoodsSpecServiceImpl extends JDBCCompomentServiceImpl<GoodsSpecModel> implements GoodsSpecService{

  public GoodsSpecServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
    super(mybatisTemplate,collectionName, keyfieldName);
  }

  @Override
  protected DBObject onBeforeRowInsert(Query query, Update update) {
    return this.onDefaultRowInsert(query, update);
  }

//  @Override
//  protected FMybatisTemplate getTemplate() {
//    return this.getBean("StorageOperation", FMybatisTemplate.class);
//  }
  
  //商品查询中心-订货配送规格
  public ServiceResponse searchByState(ServiceSession session, JSONObject paramsObject) {
    if(!paramsObject.containsKey("sgid")) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "参数母品ID不能为空");
    }
    if(!paramsObject.containsKey("erpCode")) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "经营公司编码不能为空");
    }
    if(!paramsObject.containsKey("entId")) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "零售商ID不能为空");
    }
    DefaultParametersUtils.removeEmptyParams(paramsObject);
    
    FMybatisTemplate template = this.getTemplate();
    template.onSetContext(session);
    List<Map<String, Object>> goodsSpec = template.getSqlSessionTemplate().selectList("beanmapper.GoodsSpecModelMapper.searchByState", paramsObject);
    long totalResult = template.getSqlSessionTemplate().selectOne("beanmapper.GoodsSpecModelMapper.searchByStateCount", paramsObject);
    
    JSONObject result = new JSONObject();
    result.put("total_result", totalResult);
    result.put("goodsSpec", goodsSpec);
    return ServiceResponse.buildSuccess(result);
  }
}
