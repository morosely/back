package com.efuture.omdmain.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.GoodsModel;
import com.efuture.omdmain.model.GoodsMoreBarCodeModel;
import com.efuture.omdmain.service.GoodsMoreBarCodeService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class GoodsMoreBarCodeServiceImpl extends JDBCCompomentServiceImpl<GoodsMoreBarCodeModel> implements GoodsMoreBarCodeService{

  public GoodsMoreBarCodeServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
    super(mybatisTemplate,collectionName, keyfieldName);
  }

//  @Override
//  protected FMybatisTemplate getTemplate() {
//      return this.getBean("StorageOperation", FMybatisTemplate.class);
//  }

  @Override
  protected DBObject onBeforeRowInsert(Query query, Update update) {
    return this.onDefaultRowInsert(query, update);
  }

  @Override
  public ServiceResponse search(ServiceSession session, JSONObject paramsObject) {
      return onQuery(session, paramsObject);
  }
  
  /**
   * @Title: 			searchInfoBySGID
   * @Description: 	获取商品的多条码信息
   * @param: 			@param session
   * @param: 			@param paramsObject
   * @param: 			@return   
   * @return: 		ServiceResponse   
   * @throws
   */
  public ServiceResponse searchInfoBySGID(ServiceSession session, JSONObject paramsObject) {
      if(!paramsObject.containsKey("entId")) {
        return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "entId不能为空");
      }
      if(!paramsObject.containsKey("erpCode")) {
        return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "erpCode不能为空");
      }
      if(!paramsObject.containsKey("codeType")) {
        return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "codeType不能为空");
      }
      if(!paramsObject.containsKey("sgid")) {
        return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "sgid不能为空");
      }
      
      FMybatisTemplate template = this.getTemplate();
      template.onSetContext(session);
      DefaultParametersUtils.addSplitPageParams(paramsObject);
      DefaultParametersUtils.removeEmptyParams(paramsObject);
      List<Map<String, Object>> goodsMoreBarcode = template.getSqlSessionTemplate().selectList("beanmapper.GoodsMoreBarCodeModelMapper.selectByState", paramsObject);
      long total_results = template.getSqlSessionTemplate().selectOne("beanmapper.GoodsMoreBarCodeModelMapper.selectByStateCount", paramsObject);
      
      List<Long> sgids = new ArrayList<>();
      for(Map<String, Object> g: goodsMoreBarcode) {
        if(g.get("sgid") != null) {
          sgids.add(Long.parseLong(g.get("sgid").toString()));
        }
      }
      
      List<GoodsModel> goodsModel = new ArrayList<>();
      if(CollectionUtils.isNotEmpty(sgids)){
        paramsObject.clear();
        paramsObject.put("sgids", sgids);
        goodsModel = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.selectInSGID", paramsObject);
      }
      
      for(Map<String, Object> m: goodsMoreBarcode) {
        for(GoodsModel g: goodsModel) {
          if(m.get("sgid") != null && Long.parseLong(m.get("sgid").toString()) == g.getSgid().longValue()) {
            m.put("goodsName", g.getGoodsName());
            m.put("goodsCode", g.getGoodsCode());
            m.put("originArea", g.getOriginArea());
            m.put("longScale", g.getLongScale());
            m.put("wideScale", g.getWideScale());
            m.put("highScale", g.getHighScale());
            m.put("rweight", g.getRweight());
            m.put("goodsStatus", g.getGoodsStatus());
            m.put("originArea", g.getOriginArea());
            break;
          }
        }
      }
      
      JSONObject result = new JSONObject();
      result.put("goodsMoreBarCode", goodsMoreBarcode);
      result.put("total_results", total_results);
      return ServiceResponse.buildSuccess(result);
  }
  
}
