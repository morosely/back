package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.CateringTime;
import com.efuture.omdmain.model.PackageAttShopGoodsRef;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

public class CateringTimeService extends CommonServiceImpl<CateringTime,CateringTimeService> {
    public CateringTimeService(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
        super(mybatisTemplate, collectionName, keyfieldName);
    }
    @Autowired
    private PackageAttShopGoodsRefService packageAttShopGoodsRefService;
    @Autowired
    private  SyncFrontService syncFrontService;

    public ServiceResponse upsert(ServiceSession session, JSONObject paramsObject) throws Exception {
        ServiceResponse validateResult = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "cateringTimeCode","cateringTimeName","startTime","endTime");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(validateResult.getReturncode())) return validateResult;

        String cateringTimeCode = paramsObject.getString("cateringTimeCode");
        if(cateringTimeCode.contains("#")){
            return ServiceResponse.buildFailure(session, ResponseCode.EXCEPTION,"套餐時段編碼不允許包含#");
        }

        Date date = new Date();
        paramsObject.putIfAbsent("erpCode", paramsObject.getString("erpCode") == null ? session.getErpCode() == null ? "002" : session.getErpCode() : paramsObject.getString("erpCode"));
        paramsObject.putIfAbsent("status","1");
        paramsObject.putIfAbsent("id", UniqueID.getUniqueID(true));
        paramsObject.put("creator",session.getUser_code());
        paramsObject.put("createDate",date);
        paramsObject.put("modifier",session.getUser_code());
        paramsObject.put("updateDate",date);
        paramsObject.put("entId",session.getEnt_id());
        paramsObject.put("startTime", Time.valueOf(paramsObject.getString("startTime")));
        paramsObject.put("endTime", Time.valueOf(paramsObject.getString("endTime")));
        int count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.CateringTimeMapper.insertOrUpdate",paramsObject);
        paramsObject.clear();
        paramsObject.put(this.getCollectionName(),count);
        paramsObject.put("tableName",this.getCollectionName());
        syncFrontService.asyncSendFrontData(session,paramsObject);
        return ServiceResponse.buildSuccess(paramsObject);
    }

    public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {
        ServiceResponse validateResult = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "cateringTime");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(validateResult.getReturncode())) return validateResult;
        List list = paramsObject.getJSONArray("cateringTime");
        for(Object object : list){
            JSONObject jsonData = (JSONObject)object;
            Date date = new Date();
            jsonData.putIfAbsent("erpCode", paramsObject.getString("erpCode") == null ? session.getErpCode() == null ? "002" : session.getErpCode() : paramsObject.getString("erpCode"));
            jsonData.putIfAbsent("status","1");
            jsonData.putIfAbsent("id", UniqueID.getUniqueID(true));
            //jsonData.put("creator",session.getUser_code());
            //jsonData.put("createDate",date);
            jsonData.put("modifier",session.getUser_code());
            jsonData.put("updateDate",date);
            jsonData.put("entId",session.getEnt_id());
            jsonData.put("startTime", Time.valueOf(jsonData.getString("startTime")));
            jsonData.put("endTime", Time.valueOf(jsonData.getString("endTime")));
        }
        paramsObject.clear();
        paramsObject.put("list",list);
        int count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.CateringTimeMapper.batchInsert",paramsObject);
        paramsObject.clear();
        paramsObject.put(this.getCollectionName(),count);
        paramsObject.put("tableName",this.getCollectionName());
        syncFrontService.asyncSendFrontData(session,paramsObject);
        return ServiceResponse.buildSuccess(paramsObject);
    }

    public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
        return this.onQuery(session,paramsObject);
    }

    public ServiceResponse delete(ServiceSession session, JSONObject paramsObject) throws Exception {
        ServiceResponse validateResult = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "id","cateringTimeCode","erpCode");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(validateResult.getReturncode())) return validateResult;
        String cateringTimeCodeParam = paramsObject.getString("cateringTimeCode");
        Long id = paramsObject.getLong("id");
        String erpCode = paramsObject.getString("erpCode");
        //删除选中的时段，并将相关联的档口商品默认设置为全时段展示
        JSONObject param = new JSONObject(){{
            put("cateringTimeCode",cateringTimeCodeParam);
            put("erpCode",paramsObject.getString("erpCode"));
        }};
        //处理单个时段编码逻辑 add by yihaitao 2024-09-26
        List<PackageAttShopGoodsRef> list = packageAttShopGoodsRefService.wrapQueryBeanList(session,param);
        if(list != null && !list.isEmpty()){
            for(PackageAttShopGoodsRef ref : list){
                String pCode = ref.getPCode();
                String cateringTimeCode = ref.getCateringTimeCode();
                if(pCode == null || pCode.trim().length() == 0){ //删除 packageattshopgoodsref 表的记录
                    paramsObject.clear();
                    paramsObject.put("id",ref.getId());
                    int count = this.getTemplate().getSqlSessionTemplate().delete("beanmapper.PackageAttShopGoodsRefMapper.deleteByPrimaryKey",paramsObject);
                }else{ //更新 cateringTimeCode 为 Null
                    paramsObject.clear();
                    paramsObject.put("id",ref.getId());
                    int count = this.getTemplate().getSqlSessionTemplate().delete("beanmapper.PackageAttShopGoodsRefMapper.updateNullCateringTimeCode",paramsObject);
                }
            }
        }
        //处理多个时段编码逻辑 10或者100 search from 00#100#1010 add by yihaitao 2024-09-26
        JSONObject likeParam = new JSONObject(){{
            put("like","%"+cateringTimeCodeParam+"%");
        }};
        param.put("cateringTimeCode",likeParam);
        list = packageAttShopGoodsRefService.wrapQueryBeanList(session,param);
        if(list != null && !list.isEmpty()){
            for(PackageAttShopGoodsRef ref : list){
                String cateringTimeCode = ref.getCateringTimeCode();
                String [] codeArr = cateringTimeCode.split("#");//00#100#1010 例如删除10编码。所以完全匹配才行
                if(codeArr.length > 1){
                    List updateCode = new ArrayList<>();
                    if(Arrays.asList(codeArr).contains(cateringTimeCodeParam)){
                        for(int j = 0 ; j < codeArr.length; j ++){
                            if(!cateringTimeCodeParam.equals(codeArr[j])){
                                updateCode.add(codeArr[j]);
                            }
                        }
                    }
                    //说明匹配成功:此时需要更新
                    if(codeArr.length > updateCode.size()){
                        StringBuffer code = new StringBuffer();
                        for (Iterator iter = updateCode.iterator(); iter.hasNext();) {
                            code.append(iter.next());
                            if(iter.hasNext()){
                                code.append("#");
                            }
                        }
                        param.clear();
                        param.put("id",ref.getId());
                        param.put("cateringTimeCode",code.toString());
                        int count = this.getTemplate().getSqlSessionTemplate().delete("beanmapper.PackageAttShopGoodsRefMapper.updateByPrimaryKeySelective",param);
                    }
                }
            }
        }
        paramsObject.clear();
        paramsObject.put("id",id);
        int count = this.getTemplate().getSqlSessionTemplate().delete("beanmapper.CateringTimeMapper.deleteByPrimaryKey",paramsObject);
        paramsObject.clear();
        paramsObject.put("tableName","packageattshopgoodsref");
        syncFrontService.asyncSendFrontData(session,paramsObject);
        //Fix 同步方法有延时，多次调用入参paramsObject的值被覆盖 add by yihaitao 2024-09-27
        //paramsObject.put("tableName",this.getCollectionName());
        String tableName = this.getCollectionName();
        JSONObject syncParam = new JSONObject(){{
            put("tableName",tableName);
        }};
        syncFrontService.asyncSendFrontData(session,syncParam);
        return ServiceResponse.buildSuccess(count);
    }
}
