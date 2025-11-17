package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.OrderCallNo;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class OrderCallNoServiceImpl extends CommonServiceImpl<OrderCallNo,OrderCallNoServiceImpl> {
    public OrderCallNoServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
        super(mybatisTemplate, collectionName, keyfieldName);
    }

    public ServiceResponse upsert(ServiceSession session, JSONObject paramsObject) throws Exception {
        /**
         * 订单叫号状态 0-已取消/1-制作中/2-待取餐/3-已取餐'
         * 制作中：门店号、档口编码、档口名称、叫餐号、叫号状态、订单号、款机号
         * 待取餐：门店号、档口编码、叫餐号、叫号状态、款机号
         * 已取餐：门店号、档口编码、叫餐号、叫号状态、款机号
         * 已取消：门店号、叫号状态、款机号、订单号
         */
        //1.数据必填校验
        ServiceResponse result = ServiceResponse.buildSuccess("success") ;
        Integer status = paramsObject.getInteger("status");
        switch(status){
            case 0: //已取消：门店号、叫号状态、款机号、订单号
                result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"shopCode","status","creator","orderId");
                break;
            case 1://制作中：门店号、档口编码、档口名称、叫餐号、叫号状态、订单号、款机号
                result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"shopCode","stallCode","stallName","callNo","status","orderId","creator");
                break;
            case 2://待取餐：门店号、档口编码、叫餐号、叫号状态、款机号
                result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"shopCode","stallCode","callNo","status","creator");
                break;
            case 3://已取餐：门店号、档口编码、叫餐号、叫号状态、款机号
                result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"shopCode","stallCode","callNo","status","creator");
                break;
            default:
                result = ServiceResponse.buildFailure(session,ResponseCode.Exception.PARAM_IS_EMPTY,"訂單叫號狀態只能是:0-已取消/1-製作中/2-待取餐/3-已取餐。實際入參【status】:" + status);
                break;
        }
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;

        //2.更新或者插入操作
        Date now = new Date();
        paramsObject.put("createDate",now);
        paramsObject.put("updateDate",now);
        paramsObject.put("modifier",paramsObject.getString("creator"));
        String erpCode = paramsObject.getString("erpCode") == null ? "002" : paramsObject.getString("erpCode");
        paramsObject.put("erpCode",erpCode);
        paramsObject.put("entId",session.getEnt_id());
        paramsObject.put("id", UniqueID.getUniqueID(true));

        //2.1 输入错误叫餐号逻辑判断（0,2,3的叫号状态，数据库一定存在数据）
        //int count = this.getTemplate().getSqlSessionTemplate().selectOne("beanmapper.OrderCallNoMapper.count",paramsObject);
        OrderCallNo orderCallNo = this.getTemplate().getSqlSessionTemplate().selectOne("beanmapper.OrderCallNoMapper.count",paramsObject);
        int count = orderCallNo == null ? 0 : 1;
        if(count == 0 && (status == 0 || status == 2 || status == 3)){
            String msg ;
            switch(status){
                case 0: //已取消：门店号、叫号状态、款机号、订单号
                    msg = String.format("叫餐號不存在!【門店號:%s,叫號狀態:%s,訂單號:%s,款機號:%s】",paramsObject.getString("shopCode"),paramsObject.getString("status"),
                            paramsObject.getString("orderId"),paramsObject.getString("creator"));
                    return ServiceResponse.buildFailure(session,ResponseCode.Exception.PARAM_IS_EMPTY,msg);
                case 2://待取餐：门店号、档口编码、叫餐号、叫号状态、款机号
                    msg = String.format("叫餐號不存在!【門店號:%s,檔口編碼:%s,叫餐號:%s,叫號狀態:%s,款機號:%s】",paramsObject.getString("shopCode"),paramsObject.getString("stallCode"),
                            paramsObject.getString("callNo"),paramsObject.getString("status"),paramsObject.getString("creator"));
                    return ServiceResponse.buildFailure(session,ResponseCode.Exception.PARAM_IS_EMPTY,msg);
                case 3://已取餐：门店号、档口编码、叫餐号、叫号状态、款机号
                    msg = String.format("叫餐號不存在!【門店號:%s,檔口編碼:%s,叫餐號:%s,叫號狀態:%s,款機號:%s】",paramsObject.getString("shopCode"),paramsObject.getString("stallCode"),
                            paramsObject.getString("callNo"),paramsObject.getString("status"),paramsObject.getString("creator"));
                    return ServiceResponse.buildFailure(session,ResponseCode.Exception.PARAM_IS_EMPTY,msg);
                default:
                    break;
            }
        }else if(count == 1){
            //修复重复输入叫餐号（数据库status:3已取款，端传入状态status:2）
            /**
             * 35405 取餐号状态接口允许status由3改为2
             * 场景：顾客同一个档口点了多个餐（只会生成一个取餐号），后厨不会等到所有餐准备齐全再叫号，而是出一个餐就叫号
             * 问题：一个取餐号只能叫一次，叫过的号就无法再扫码叫号提醒顾客取餐
             * 调整：
             * 1、中台服务：取消status状态的控制，允许status 由3改为2
             * 2、安卓大屏：不在大屏中的取餐号，输入后直接调用中台更新取餐号status为 2
             */
            int dbStatus = orderCallNo.getStatus().intValue();
            //if((3 == dbStatus || 0 == dbStatus) && (2 == status || 3 == status)){
            if((3 == dbStatus || 0 == dbStatus) && (3 == status)){
                String msg = String.format("叫餐號已取餐或失效【門店號:%s,檔口編碼:%s,叫餐號:%s,叫號狀態:%s,款機號:%s】",paramsObject.getString("shopCode"),paramsObject.getString("stallCode"),
                        paramsObject.getString("callNo"),paramsObject.getString("status"),paramsObject.getString("creator"));
                return ServiceResponse.buildFailure(session,ResponseCode.Exception.PARAM_IS_EMPTY,msg);
            }
            //不允许状态变更：制作中->已取餐
            if(1 == dbStatus &&  3 == status){
                String msg = String.format("無法取餐，正在製作中【門店號:%s,檔口編碼:%s,叫餐號:%s,叫號狀態:%s,款機號:%s】",paramsObject.getString("shopCode"),paramsObject.getString("stallCode"),
                        paramsObject.getString("callNo"),paramsObject.getString("status"),paramsObject.getString("creator"));
                return ServiceResponse.buildFailure(session,ResponseCode.Exception.PARAM_IS_EMPTY,msg);
            }
        }

        //2.2 更新或者保存
        paramsObject.put("count",count);
        count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.OrderCallNoMapper.upsert",paramsObject);
        log.info("=========>>>【OrderCallNoServiceImpl-upsert】=========>>> count:"+count);

        //3.数据返回
        paramsObject.clear();
        paramsObject.put(this.getCollectionName(),count);
        return ServiceResponse.buildSuccess(paramsObject);
    }

    public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
        Boolean stallGroup = paramsObject.getBoolean("stallGroup");
        String erpCode = session.getErpCode() != null ? session.getErpCode() : paramsObject.getString("erpCode") != null ? paramsObject.getString("erpCode") : "002";
        paramsObject.put("erpCode",erpCode);

        if(stallGroup!=null && stallGroup){
            ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"shopCode");
            if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
            Object paramStallCode = paramsObject.get("stallCode");//可能是字符串或者数组
            List<Map> stallList = null;
            if(paramStallCode == null){
                //查询门店所有档口(模式四)
                stallList = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.queryShopStall", paramsObject);
            }else{//查询一个门店的档口(模式四)
                if(paramStallCode instanceof List){//一个门店的多个档口
                    stallList = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.queryShopMoreStall", paramsObject);
                }else{//一个门店的一个档口(模式四)
                    stallList = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.queryShopOneStall", paramsObject);
                }
            }
            List<OrderCallNo> data = this.wrapQueryBeanList(session,paramsObject);
            Map<String,List<OrderCallNo>> callNoMap = data.stream().collect(Collectors.groupingBy(OrderCallNo::getStallCode));
            for(int i=0; i<stallList.size(); i++){
                Map stallMap = stallList.get(i);
                String stallCode = stallMap.get("stallCode").toString();
                if(callNoMap.containsKey(stallCode)){
                    stallMap.put(this.getCollectionName(),callNoMap.get(stallCode));
                }else{
                    stallMap.put(this.getCollectionName(), Arrays.asList());
                }
            }
            paramsObject.clear();
            paramsObject.put("stallCodes",stallList);
            return ServiceResponse.buildSuccess(paramsObject);
        }
        return this.onQuery(session,paramsObject);
    }
}
