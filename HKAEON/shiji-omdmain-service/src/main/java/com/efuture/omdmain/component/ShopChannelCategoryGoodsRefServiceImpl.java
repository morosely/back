package com.efuture.omdmain.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.ChannelInfoModel;
import com.efuture.omdmain.model.GoodsModel;
import com.efuture.omdmain.model.GoodsUpAndDownModel;
import com.efuture.omdmain.model.SaleGoodsModel;
import com.efuture.omdmain.model.ShopChannelCategoryGoodsRefModel;
import com.efuture.omdmain.model.ShopModel;
import com.efuture.omdmain.model.ShowCategoryModel;
import com.efuture.omdmain.model.ShowCategoryTreeBean;
import com.efuture.omdmain.service.ShopChannelCategoryGoodsRefService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;

/**
 * 门店渠道展示分类商品关联
 * */
public class ShopChannelCategoryGoodsRefServiceImpl extends JDBCCompomentServiceImpl<ShopChannelCategoryGoodsRefModel> implements ShopChannelCategoryGoodsRefService {

  @Autowired
  private ShowCategoryServiceImpl showCategoryService;
  
  public ShopChannelCategoryGoodsRefServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
    super(mybatisTemplate,collectionName, keyfieldName);
  }

  @Override
  protected DBObject onBeforeRowInsert(Query paramQuery, Update paramUpdate) {
    return this.onDefaultRowInsert(paramQuery, paramUpdate);
  }

//  @Override
//  protected FMybatisTemplate getTemplate() {
//    return this.getBean("StorageOperation", FMybatisTemplate.class);
//  }

  //线上商品展示分类，商品与展示分类关系保存
  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public ServiceResponse save(ServiceSession session, JSONObject paramsObject) throws Exception{
    if(!paramsObject.containsKey("shopId")) {
      return ServiceResponse.buildFailure(session,ResponseCode.Exception.SPECDATA_IS_EMPTY,"门店ID不能为空");
    }
    if(!paramsObject.containsKey("channelId")) {
      return ServiceResponse.buildFailure(session,ResponseCode.Exception.SPECDATA_IS_EMPTY,"渠道ID不能为空");
    }
    if(!paramsObject.containsKey("sgid")) {
      return ServiceResponse.buildFailure(session,ResponseCode.Exception.SPECDATA_IS_EMPTY,"售卖商品ID不能为空");
    }
    if(!paramsObject.containsKey("showCategoryId")) {
      return ServiceResponse.buildFailure(session,ResponseCode.Exception.SPECDATA_IS_EMPTY,"展示分类ID不能为空");
    }
    if(!paramsObject.containsKey("isRelated")) {   // Y：表示关联， N:表示不关联
      return ServiceResponse.buildFailure(session,ResponseCode.Exception.SPECDATA_IS_EMPTY,"是否关联不能为空");
    }
    
    FMybatisTemplate template = this.getTemplate();
    template.onSetContext(session);
    
    if("N".equals(paramsObject.getString("isRelated"))) {
      //不关联则删除
      paramsObject.remove("isRelated");
      return this.onDelete(session, paramsObject);
    }
    
    List<SaleGoodsModel> sgList = template.getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.searchByState",paramsObject);
    SaleGoodsModel sg = null;
    if(CollectionUtils.isNotEmpty(sgList)) {
      sg = sgList.get(0);
    }
    List<GoodsUpAndDownModel> upAndDownList = template.getSqlSessionTemplate().selectList("beanmapper.GoodsUpAndDownModelMapper.searchByState",paramsObject);
    GoodsUpAndDownModel upAndDown = null;
    if(CollectionUtils.isNotEmpty(upAndDownList)) {
      upAndDown = upAndDownList.get(0);
    }
    
    String creator = Long.toString(session.getUser_id());
    ShopChannelCategoryGoodsRefModel ref = new ShopChannelCategoryGoodsRefModel();
    
    ref.setChannelId(Long.parseLong(paramsObject.getString("channelId")));
    ref.setEntId(session.getEnt_id());
    if(sg != null) ref.setLang(sg.getLang());
    ref.setSgid(Long.parseLong(paramsObject.getString("sgid")));
    ref.setShopId(Long.parseLong(paramsObject.getString("shopId")));
    ref.setShowCategoryId(Long.parseLong(paramsObject.getString("showCategoryId")));
    JSONObject jsonObj = JSONObject.parseObject(JSON.toJSONString(ref));
    this.onDelete(session, jsonObj);
    
    ref.setSccgrid(UniqueID.getUniqueID(true));  //唯一标示符
    ref.setCreateDate(new Date());
    ref.setUpdateDate(new Date());
    ref.setCreator(creator);
    ref.setModifier(creator);
    //Integer upDownStatus = upAndDown == null ? 0 :upAndDown.getUpdownStatus(); //0：表示下架  1：上架
    //ref.setUpdownStatus(upDownStatus);
    jsonObj = JSONObject.parseObject(JSON.toJSONString(ref));
    return this.onInsert(session, jsonObj);
  }
  
  //多个商品及其对应的明细的保存
  @Transactional
  @Override
  public ServiceResponse batchSave(ServiceSession session, JSONObject paramsObject) {
    if(!paramsObject.containsKey("shopId")) {
      return ServiceResponse.buildFailure(session,ResponseCode.Exception.SPECDATA_IS_EMPTY,"门店ID不能为空");
    }
    if(!paramsObject.containsKey("channelId")) {
      return ServiceResponse.buildFailure(session,ResponseCode.Exception.SPECDATA_IS_EMPTY,"渠道ID不能为空");
    }
    if(!paramsObject.containsKey("infoList")) {
      return ServiceResponse.buildFailure(session,ResponseCode.Exception.SPECDATA_IS_EMPTY,"售卖商品ID不能为空");
    }
    
    JSONArray infoListArray = paramsObject.getJSONArray("infoList");
    if(infoListArray.isEmpty()) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "请选择要关联的商品与展示分类！", new Object[] {});
    }
    
    FMybatisTemplate template = this.getTemplate();
    template.onSetContext(session);
    List<GoodsUpAndDownModel> upAndDownList = template.getSqlSessionTemplate().selectList("beanmapper.GoodsUpAndDownModelMapper.searchIn",paramsObject);
    
    Map<Long, GoodsUpAndDownModel> upDownMap = new HashMap<>();
    if(!CollectionUtils.isEmpty(upAndDownList)) {
      for(GoodsUpAndDownModel m: upAndDownList) {
        upDownMap.put(m.getSsgid(), m);
      }
    }
    
    String creator = Long.toString(session.getUser_id());
    
    //1.先删除旧的信息
    template.getSqlSessionTemplate().selectList("beanmapper.ShopChannelCategoryGoodsRefModelMapper.batchDelete",paramsObject);
    //2.添加新的信息
    JSONArray infoList = paramsObject.getJSONArray("infoList");
    for(int i=0; infoList != null && i<infoList.size(); i++) {
      JSONObject tmp = infoList.getJSONObject(i);
      Long tmpSgid = tmp.getLong("sgid");
      String[] categoryIds = tmp.getString("showCategoryIds").split(",");
      for(String s: categoryIds) {
        if(StringUtils.isEmpty(s)) {
          continue;
        }
        ShopChannelCategoryGoodsRefModel refModel = new ShopChannelCategoryGoodsRefModel();
        refModel.setChannelId(paramsObject.getLong("channelId"));
        refModel.setCreateDate(new Date());
        refModel.setCreator(creator);
        refModel.setEntId(session.getEnt_id());
        refModel.setModifier(creator);
        refModel.setSccgrid(UniqueID.getUniqueID(true));
        refModel.setSgid(tmp.getLong("sgid"));
        refModel.setShopId(paramsObject.getLong("shopId"));
        refModel.setShowCategoryId(Long.parseLong(s));
        refModel.setUpdateDate(new Date());
       /* if(upDownMap.get(tmpSgid) != null)
          refModel.setUpdownStatus(upDownMap.get(tmpSgid).getUpdownStatus());
        else
          refModel.setUpdownStatus(0);  //下架
       */ 
        JSONObject insertObj = JSON.parseObject(JSON.toJSONString(refModel));
        ServiceResponse rep = this.onInsert(session, insertObj);
        if(!ResponseCode.SUCCESS.equals(rep.getReturncode())) {
          throw new RuntimeException(rep.getData().toString());
        }
      }
    }
    return ServiceResponse.buildSuccess("保存成功.");
  }
  
  //线上商品展示分类-树控件加载并渲染已勾选叶节点   
  public ServiceResponse showCategoryTreeWithChecked(ServiceSession session, JSONObject paramsObject) throws Exception {
    JSONObject params = new JSONObject();
    params.put("channelIds", paramsObject.getString("channelIds"));
    params.put("checkedFlag",true);
    ServiceResponse treeData = this.showCategoryService.onShowCategoryTree(session, params);
    if(!ResponseCode.SUCCESS.equals(treeData.getReturncode())) {
      return treeData;
    }
    
    JSONObject jsonObj = (JSONObject)treeData.getData();
    JSONArray jsonArray = jsonObj.getJSONArray("showcategory");
    List<ShowCategoryTreeBean> showcategory = jsonArray.parseArray(JSON.toJSONString(jsonArray), ShowCategoryTreeBean.class);
    
    ServiceResponse refResponse = this.onQuery(session, paramsObject);
    jsonObj = (JSONObject)refResponse.getData();
    jsonArray = jsonObj.getJSONArray("shopChannelCategoryGoodsRef");
    List<ShopChannelCategoryGoodsRefModel> refData = JSONArray.parseArray(JSON.toJSONString(jsonArray), ShopChannelCategoryGoodsRefModel.class);
    Map<Long, ShopChannelCategoryGoodsRefModel> refMap = new HashMap<>();
    for(ShopChannelCategoryGoodsRefModel tmp : refData) {
      refMap.put(tmp.getShowCategoryId().longValue(), tmp);
    }
    
    this.addSelectedToChildrenNode(showcategory, refMap);
    JSONObject result = new JSONObject();
    result.put("showcategory", showcategory);
    return ServiceResponse.buildSuccess(result);
  }
  
  //将被勾选的树控件叶子节点的selected设置为true
  private void addSelectedToChildrenNode(List<ShowCategoryTreeBean> treeData, Map<Long, ShopChannelCategoryGoodsRefModel> refMap){
    if(CollectionUtils.isEmpty(treeData)) return;
    
    for(int index=0; index<treeData.size(); index++) {
      ShowCategoryTreeBean b = treeData.get(index);
      if(refMap.get(b.getShowCategoryId()) != null) {
        b.setSelected(true);
      }
      addSelectedToChildrenNode(b.getChildren(), refMap);
    }
  }
  
  //商品查询中心-渠道展示
  public ServiceResponse getRefBySGID(ServiceSession session, JSONObject paramsObject) {
    if(!paramsObject.containsKey("entId")) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "零售商ID不能为空.");
    }
    if(!paramsObject.containsKey("sgid")) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "门店销售商品ID不能为空.");
    }
    
    FMybatisTemplate template = this.getTemplate();
    template.onSetContext(session);
    List<Map<String, Object>> refList = template.getSqlSessionTemplate().selectList("beanmapper.ShopChannelCategoryGoodsRefModelMapper.selectByState", paramsObject);
    long totalResult = template.getSqlSessionTemplate().selectOne("beanmapper.ShopChannelCategoryGoodsRefModelMapper.selectByStateCount", paramsObject);

    List<Long> channelIds = new ArrayList<>();
    List<Long> shopIds = new ArrayList<>();
    List<Long> showCategoryIds = new ArrayList<>();
    List<Long> sgids = new ArrayList<>();
    
    for(Map<String, Object> r: refList) {
      if(r.get("channelId") != null)
        channelIds.add(Long.parseLong(r.get("channelId").toString()));
      if(r.get("shopId") != null)
        shopIds.add(Long.parseLong(r.get("shopId").toString()));
      if(r.get("showCategoryId") != null)
        showCategoryIds.add(Long.parseLong(r.get("showCategoryId").toString()));
      if(r.get("sgid") != null)
        sgids.add(Long.parseLong(r.get("sgid").toString()));
      
      //保证在查询不到数据的情况下，前端不报undefined
      r.put("shopName", "-");
      r.put("shopCode", "-");
      r.put("channelName", "-");
      r.put("categoryName", "-");
      r.put("originArea", "-");
    }
    
    List<ChannelInfoModel> channelList = new ArrayList<>();
    if(CollectionUtils.isNotEmpty(channelIds)) {
      paramsObject.clear();
      DefaultParametersUtils.removeRepeateParams4Long(channelIds);
      paramsObject.put("channelIds", channelIds);
      channelList = template.getSqlSessionTemplate().selectList("beanmapper.ChannelInfoModelMapper.selectIn", paramsObject);
    }
    
    List<ShopModel> shopList = new ArrayList<>();
    if(CollectionUtils.isNotEmpty(shopIds)) {
      DefaultParametersUtils.removeRepeateParams4Long(shopIds);
      shopList = template.getSqlSessionTemplate().selectList("beanmapper.ShopModelMapper.selectIn", shopIds);
    }
    
    List<ShowCategoryModel> showCategory = new ArrayList<>();
    if(CollectionUtils.isNotEmpty(showCategoryIds)) {
      paramsObject.clear();
      DefaultParametersUtils.removeRepeateParams4Long(showCategoryIds);
      paramsObject.put("showCategoryIds", showCategoryIds);
      showCategory = template.getSqlSessionTemplate().selectList("beanmapper.ShowCategoryModelMapper.selectIn",paramsObject);
    }
    
    List<GoodsModel> goodsList = new ArrayList<>();
    if(CollectionUtils.isNotEmpty(sgids)) {
      paramsObject.clear();
      DefaultParametersUtils.removeRepeateParams4Long(sgids);
      paramsObject.put("sgids", sgids);
      goodsList = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.selectInSGID", paramsObject);
    }

    if(CollectionUtils.isNotEmpty(refList) && CollectionUtils.isNotEmpty(channelList))
    {
      for(Map<String, Object> r: refList) {
        for(ChannelInfoModel c: channelList) {
          if(r.get("channelId") != null && Long.parseLong(r.get("channelId").toString()) == c.getChannelId()) {
            r.put("channelName", c.getChannelName());
            break;
          }
        }
        
        for(ShopModel s: shopList) {
          if(r.get("shopId") != null && Long.parseLong(r.get("shopId").toString()) == s.getShopId()) {
            r.put("shopName", s.getShopName());
            r.put("shopCode", s.getShopCode());
            break;
          }
        }
        
        for(ShowCategoryModel s: showCategory) {
          if(r.get("showCategoryId") != null && Long.parseLong(r.get("showCategoryId").toString()) == s.getShowCategoryId()) {
            r.put("showCategoryName", s.getShowCategoryName());
            break;
          }
        }
        
        //添加产地
        for(GoodsModel g: goodsList) {
          if(r.get("sgid") != null && Long.parseLong(r.get("sgid").toString()) == g.getSgid()) {
            r.put("goodsCode", g.getGoodsCode());
            r.put("goodsName", g.getGoodsName());
            r.put("originArea", g.getOriginArea());
            r.put("salePrice", g.getSalePrice());
            break;
          }
        }
      }
    }
    
    JSONObject result = new JSONObject();
    result.put("total_result", totalResult);
    result.put("shopChannelCategoryGoodsRef", refList);
    return ServiceResponse.buildSuccess(result);
  }
  
  public static void main(String[] args) {
    String s = "12,34,56";
    String[] a = s.split(",");
    for(String ss: a)
      System.out.println(Long.parseLong(ss));
  }
}