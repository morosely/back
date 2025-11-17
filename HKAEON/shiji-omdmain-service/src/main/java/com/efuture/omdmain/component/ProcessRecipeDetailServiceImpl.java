package com.efuture.omdmain.component;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.GoodsModel;
import com.efuture.omdmain.model.ProcessRecipeDetailModel;
import com.efuture.omdmain.model.SaleGoodsModel;
import com.efuture.omdmain.service.ProcessRecipeDetailService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.FileProcessorUtils;

public class ProcessRecipeDetailServiceImpl extends JDBCCompomentServiceImpl<ProcessRecipeDetailModel> implements ProcessRecipeDetailService {
	
  @Autowired
  private SaleGoodsServiceImpl saleGoodsServiceImpl;

  public ProcessRecipeDetailServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
    super(mybatisTemplate,collectionName, keyfieldName);
  }

  @Override
  protected DBObject onBeforeRowInsert(Query query, Update update) {
      return this.onDefaultRowInsert(query, update);
  }

//  @Override
//  protected FMybatisTemplate getTemplate() {
//      return this.getBean("StorageOperation", FMybatisTemplate.class);
//  }
  
  /**
   * @Title: 			batchDelete
   * @Description: 	批量删除明细
   * @param: 			@param session
   * @param: 			@param paramsObject
   * @param: 			@return   
   * @return: 		ServiceResponse   
   * @throws
   */
  @Transactional
  public ServiceResponse batchDelete(ServiceSession session, JSONObject paramsObject) {
    if(!paramsObject.containsKey("prdids")) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "prdids不能为空.");
    }
    
    FMybatisTemplate template = this.getTemplate();
    template.onSetContext(session);
    template.getSqlSessionTemplate().delete("beanmapper.ProcessRecipeDetailModelMapper.batchDeleteByPrid", paramsObject);
    return ServiceResponse.buildSuccess("删除成功.");
  }
  
  
}
