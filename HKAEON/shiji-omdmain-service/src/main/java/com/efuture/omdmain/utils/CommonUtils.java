package com.efuture.omdmain.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huangzhengwei on 2018/9/6.
 *
 * @Desciption:
 */
public class CommonUtils<T> {

    public  HashMap<Long, List<T>> getPidEnty(List<T> allEnty){

        HashMap<Long, List<T>> pidEnty = new HashMap<>();
        for (T t : allEnty) {
            JSONObject tmpObject = JSON.parseObject(JSON.toJSONString(t));
            if(tmpObject ==null || !tmpObject.containsKey("level") || StringUtils.isEmpty(tmpObject.get("level"))) continue;
            if(tmpObject.getInteger("level") != 1 && (!tmpObject.containsKey("parentId") ||
                    tmpObject.getLong("parentId") == null)) continue;
            Long key;
            key = tmpObject.getInteger("level").equals(1) ? -1l:tmpObject.getLong("parentId");
            if(key != null){
                List<T> tmpCategoryTreeBeans = null;
                if(!pidEnty.containsKey(key)){
                    tmpCategoryTreeBeans = new ArrayList<>();
                    pidEnty.put(key, tmpCategoryTreeBeans);
                }else {
                    tmpCategoryTreeBeans = pidEnty.get(key);
                }
                tmpCategoryTreeBeans.add(t);
            }
        }
        return pidEnty;
    }
//        for (CategoryTreeBean category : allCategorys) {
//            if(category.getLevel() != 1 && category.getParentId() == null) continue;
//            List<CategoryTreeBean> tmpCategoryTreeBeans = null;
//            Long key = null ;
//            key = category.getLevel()==1 ? -1l:category.getParentId();
//            if(key != null){
//                if(!pidCategoryTree.containsKey(key)){
//                    tmpCategoryTreeBeans = new ArrayList<>();
//                    pidCategoryTree.put(key, tmpCategoryTreeBeans);
//                }else {
//                    tmpCategoryTreeBeans = pidCategoryTree.get(key);
//                }
//                tmpCategoryTreeBeans.add(category);
//            }
//        }
//    }
}
