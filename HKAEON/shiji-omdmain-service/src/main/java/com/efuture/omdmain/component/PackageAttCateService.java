package com.efuture.omdmain.component;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.PackageAttCate;
import com.efuture.omdmain.model.PackageAttCateTree;
import com.efuture.omdmain.model.PackageAttDict;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

public class PackageAttCateService extends CommonServiceImpl<PackageAttCate,PackageAttCateService> {
    public PackageAttCateService(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
        super(mybatisTemplate, collectionName, keyfieldName);
    }

    @Autowired
    private  SyncFrontService syncFrontService;

    //属性复制
    @Transactional(rollbackFor=Exception.class)
    public ServiceResponse copyData(ServiceSession session, JSONObject paramsObject) throws Exception {
        ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"id","pCode","pName","erpCode","level");
        logger.info("==========>>> 【PackageAttCateService:copyData】 方法入参:{}",paramsObject);
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
        //1.复制属性分类:系统自动生成无重复的pCode，pName( 第二级名称不允许重复，第三级名称可以重复)
        //重命名的规则：pCode-1、pCode-2、pCode-3
        String pCode = paramsObject.getString("pCode");
        String pName = paramsObject.getString("pName");
        String erpCode = paramsObject.getString("erpCode");
        String level = paramsObject.getString("level");
        if(!"2".equals(level)){
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY,"第二級才能複製");
        }
        //获取复制后的pCode
        List<PackageAttCate> likeList = this.wrapQueryBeanList(session,new JSONObject(){{put("pCode",new JSONObject(){{put("like",pCode+"_%");}});}});
        int maxNumPCode = 0;
        if(!likeList.isEmpty()){
            for(PackageAttCate model : likeList){
                String dbPCode = model.getPCode();
                maxNumPCode = getMaxNum(pCode, maxNumPCode, dbPCode);
            }
        }
        String newPCode = pCode+"_"+(maxNumPCode+1);
        paramsObject.put("newPCode",newPCode);
        //获取复制后的pName
        likeList = this.wrapQueryBeanList(session,new JSONObject(){{put("pName",new JSONObject(){{put("like",pName+"_%");}});}});
        //likeList = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.PackageAttCateMapper.selectPName",paramsObject);
        int maxNumPName = 0;
        if(!likeList.isEmpty()){
            for(PackageAttCate model : likeList){
                String dbPName = model.getPName();
                maxNumPName = getMaxNum(pName, maxNumPName, dbPName);
            }
        }
        String newPName = pName+"_"+(maxNumPName+1);
        paramsObject.put("newPName",newPName);
        paramsObject.put("creator",session.getUser_code());
        int count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.PackageAttCateMapper.copyData",paramsObject);
        logger.info("1. ==========>>>>> 属性分类复制 2级数据:{}",count);
        //2.复制属性分类的子类
        //3级只需要重命名pCode
        List<PackageAttCate> sonList = this.wrapQueryBeanList(session,new JSONObject(){{put("parentCode",pCode);put("erpCode",erpCode);}});
        if(!sonList.isEmpty()){
            Map<String,String> newOldCodeMap = new HashMap();
            //重命名
            for (PackageAttCate copySonModel : sonList){
                String sonPCode = copySonModel.getPCode();
                likeList = this.wrapQueryBeanList(session,new JSONObject(){{put("pCode",new JSONObject(){{put("like",sonPCode+"_%");}});}});
                int maxNumSonPCode = 0;
                if(!likeList.isEmpty()){
                    for(PackageAttCate model : likeList){
                        String dbPCode = model.getPCode();
                        maxNumSonPCode = getMaxNum(sonPCode, maxNumSonPCode, dbPCode);
                    }
                }
                String newSonPCode = sonPCode+"_"+(maxNumSonPCode+1);
                copySonModel.setPCode(newSonPCode);
                copySonModel.setParentCode(newPCode);
                copySonModel.setId(UniqueID.getUniqueID(true));
                copySonModel.setCreator(session.getUser_code());
                copySonModel.setModifier(session.getUser_code());
                copySonModel.setCreateDate(new Date());
                copySonModel.setUpdateDate(copySonModel.getCreateDate());
                newOldCodeMap.put(newSonPCode,sonPCode);
            }
            //批量插入第3级数据
            count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.PackageAttCateMapper.batchInsert",new JSONObject(){{put("list",sonList);}});
            logger.info("2. ==========>>>>> 属性分类复制 3级数据:{}",count);
            //3.复制属性分类的属性字典值
            for (PackageAttCate copySonModel : sonList){
                String newSonPCode = copySonModel.getPCode();
                count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.PackageAttCateMapper.copyDicData",
                        new JSONObject(){{put("newPCode",newSonPCode);put("erpCode",erpCode);put("pCode",newOldCodeMap.get(newSonPCode));put("creator",session.getUser_code());}});
                logger.info("3. ==========>>>>> 属性分类复制 字典数据:{} = {}",newOldCodeMap.get(newSonPCode),count);
            }
        }
        paramsObject.clear();
        paramsObject.put("tableName",this.getCollectionName());
        paramsObject.put("count",count);
        //2.同步前置
        String tableName = this.getCollectionName();
        syncFrontService.asyncSendFrontData(session,new JSONObject(){{
            put("tableName",tableName);
        }});
        return ServiceResponse.buildSuccess(paramsObject);
    }

    private int getMaxNum(String code, int maxNum, String dbCode) {
        int length = code.length();
        if(dbCode.length() > code.length()){
            String lastNum = dbCode.substring(length + 1);
            int num = 0;
            try{
                num = Integer.parseInt(lastNum);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(num > maxNum){
                maxNum = num;
            }
        }
        return maxNum;
    }


    //属性必选数量
    public ServiceResponse updateRequiredCount(ServiceSession session, JSONObject paramsObject) throws Exception {
        //校验必填字段
        ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"id","requiredCount");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
        paramsObject.put("modifier",session.getUser_code());
        paramsObject.put("updateDate",new Date());
        //2.同步前置
        String tableName = this.getCollectionName();
        syncFrontService.asyncSendFrontData(session,new JSONObject(){{
            put("tableName",tableName);
        }});
        return this.onUpdate(session,paramsObject);
    }

    //属性分类删除
    public ServiceResponse delete(ServiceSession session, JSONObject paramsObject) throws Exception {
        //校验必填字段
        ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"id","pCode","erpCode","level");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;

        Integer level = paramsObject.getInteger("level");
        Long id = paramsObject.getLong("id");
        if(1 == level) {
            String msg = String.format("根節點數據請不要删除!屬性類別編碼【%s】",paramsObject.getString("pCode"));
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY,msg);
        }else if(2 == level){
            int count = this.getTemplate().getSqlSessionTemplate().selectOne("beanmapper.PackageAttCateMapper.countSonPCode",paramsObject);
            if(count > 0 ){
                String msg = String.format("屬性類別編碼【%s】存在子編碼,請先删除子編碼",paramsObject.getString("pCode"));
                return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY,msg);
            }

        }else if(3 == level){
           List<PackageAttDict> dictList = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.PackageAttDictMapper.selectPCodeDic",paramsObject);
           if(dictList!=null && !dictList.isEmpty()){
               String msg = String.format("屬性類別編碼【%s】存在屬性值,請先删除屬性值",paramsObject.getString("pCode"));
               return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY,msg);
           }
        }
        paramsObject.clear();
        paramsObject.put("id",id);
        ServiceResponse response = this.onDelete(session,paramsObject);
        paramsObject.put("tableName",this.getCollectionName());
        syncFrontService.asyncSendFrontData(session,paramsObject);
        return response;
    }

    //属性分类新增或更改
    public ServiceResponse upsert(ServiceSession session, JSONObject paramsObject) throws Exception {
        //校验必填字段
        ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"pCode","pName","parentCode","erpCode","level","status");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;

        Integer level = paramsObject.getInteger("level");
        if( 1!=level && 2!=level && 3!=level){
            String msg = String.format("級別的範圍是1,2,3:入參level【%s】",level);
            return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY,msg);
        }
        Integer leafFlag = 3 == level ? 1 : 0 ;
        paramsObject.put("leafFlag",leafFlag);

        //第二级不允许重名：只针对树形数据的第二级数据
        if(2 == level){
            int existPNameCount = this.getTemplate().getSqlSessionTemplate().selectOne("beanmapper.PackageAttCateMapper.existPNameCount",paramsObject);
            if(existPNameCount > 0){
                String msg = String.format("屬性類別名稱有重復【%s】,請重命名",paramsObject.getString("pName"));
                return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY,msg);
            }
        }

        Date date = new Date();
        //新增或者更新：通过判断前端传入主键是否为空
        Long id = paramsObject.getLong("id");
        ServiceResponse response = null;
        if (id == null){//新增
            int count = this.getTemplate().getSqlSessionTemplate().selectOne("beanmapper.PackageAttCateMapper.countPCode",paramsObject);
            if(count > 0 ){
                String msg = String.format("屬性類別編碼【%s】已存在",paramsObject.getString("pCode"));
                return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY,msg);
            }
            paramsObject.put("creator",session.getUser_code());
            paramsObject.put("createDate",date);
            response = this.onInsert(session,paramsObject);
        }else{//更新
            paramsObject.put("id",id);//解决id是字符串类型基类无法更新更新
            paramsObject.put("modifier",session.getUser_code());
            paramsObject.put("updateDate",date);
            response = this.onUpdate(session,paramsObject);
        }
//        System.out.println("========================== >>>>> "+Thread.currentThread().getId()+" --- "+Thread.currentThread().getName());
        paramsObject.put("tableName",this.getCollectionName());
        syncFrontService.asyncSendFrontData(session,paramsObject);
        return response;
    }

    //属性分类树形查询
    public ServiceResponse tree(ServiceSession session, JSONObject paramsObject) throws Exception {
        paramsObject.put("page_size",Integer.MAX_VALUE);
        List<PackageAttCate> data = this.wrapQueryBeanList(session,paramsObject);
        List<PackageAttCateTree> result = null;
        if(data!=null && !data.isEmpty()){
            List<PackageAttCateTree> treeNodes = BeanUtil.copyToList(data,PackageAttCateTree.class);
            Map<Object,List<PackageAttCateTree>> groupMap= treeNodes.stream().collect(Collectors.groupingBy(PackageAttCateTree::getParentCode));
            for(PackageAttCateTree treeNode : treeNodes){
                String code = treeNode.getPCode();
                if (groupMap.containsKey(code)) {
				    treeNode.setChildren(groupMap.get(code));
			    }
            }
            result = treeNodes.stream().filter(e -> e.getParentCode().equals("0")).collect(Collectors.toList());
        }
        paramsObject.clear();
        paramsObject.put(this.getCollectionName(),JSONObject.toJSON(result));
        return ServiceResponse.buildSuccess(paramsObject);
    }


    public ServiceResponse syncOneDelete(ServiceSession session, JSONObject paramsObject) throws Exception {
        int count = this.getTemplate().getSqlSessionTemplate().delete("beanmapper.PackageAttCateMapper.delete",paramsObject);
        paramsObject.put("tableName",this.getCollectionName());
        syncFrontService.asyncSendFrontData(session,paramsObject);
        return ServiceResponse.buildSuccess(count);
    }
}
