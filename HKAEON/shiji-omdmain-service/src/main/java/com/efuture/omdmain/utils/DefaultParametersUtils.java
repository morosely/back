package com.efuture.omdmain.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.product.model.BeanConstant;
import com.product.model.RowMap;
import com.product.model.ServiceSession;

public class DefaultParametersUtils {
	
//	private static final Log LOG = LogFactory.getLog(DefaultParametersUtils.class);
	
	public static JSONObject transformParam(ServiceSession session,JSONObject params){
		return transformParam(session,params,false,true);
	}
	
	public static JSONObject transformParam(ServiceSession session,JSONObject params,boolean insert){
		return transformParam(session,params,false,insert);
	}
	
	public static JSONObject transformParam(ServiceSession session,JSONObject params,boolean updatetime,boolean insert){
		Integer pageno = BeanConstant.QueryPage.DEFAULT_PAGENO;
		Integer pageSize = BeanConstant.QueryPage.DEFAULT_PAGESIZE;
		if (params.containsKey(BeanConstant.QueryField.PARAMKEY_PAGESIZE)) {
			pageSize = params.getInteger(BeanConstant.QueryField.PARAMKEY_PAGESIZE);
		}else{
			pageSize = 50;
		}
		if (params.containsKey(BeanConstant.QueryField.PARAMKEY_PAGENO)) {
			pageno = params.getInteger(BeanConstant.QueryField.PARAMKEY_PAGENO) - 1;
			pageno *= pageSize;
		}else{
			pageno = 0;
		}
		params.put(BeanConstant.QueryField.PARAMKEY_PAGENO, pageno);
		params.put(BeanConstant.QueryField.PARAMKEY_PAGESIZE, pageSize);
		if(!params.containsKey("entId")){
			params.put("entId", session.getEnt_id());
		}
		
		if(updatetime){
			params.put("updateDate", new Date());
		}
		if(insert){
			if(!params.containsKey("status")){
				params.put("status", "1");
			}
		}
		return params;
	}

	public static void addSplitPageParams(JSONObject paramsObject) {
      //设置默认分页参数
      if(!paramsObject.containsKey("page_size")) {
        paramsObject.put("page_size", 10);
      }
      if(!paramsObject.containsKey("page_no")){
        paramsObject.put("page_no", 0);
      }else{
        paramsObject.put("page_no", (paramsObject.getInteger("page_no")-1)*paramsObject.getInteger("page_size"));
      }
    }
	
	/**
	 * @Title: 			removeEmptyParams
	 * @Description: 	移除值为空字符串的key
	 * @param: 			@param paramsObject   
	 * @return: 		void   
	 * @throws
	 */
	public static void removeEmptyParams(JSONObject paramsObject) {
	  if(paramsObject == null) return;
	  
	  Iterator<String> keys = paramsObject.keySet().iterator();
	  List<String> keysStore = new ArrayList<>();
	  
	  while(keys.hasNext()) {
	    String key = keys.next();
	    if("".equals(paramsObject.getString(key))) {
	      keysStore.add(key);
	    }
	  }
	  
	  for(String key: keysStore) {
	    paramsObject.remove(key);
	  }
	}
	
	public static void numberFormat(List<RowMap> rowMap, String dataSyntax, String... fields) {
      if(CollectionUtils.isEmpty(rowMap) || StringUtils.isEmpty(dataSyntax) || fields.length <= 0) {
        return ;
      }
      
      for(RowMap map: rowMap) {
        for(String key: fields) {
         if(map.get(key) == null) continue;
         String value = new java.text.DecimalFormat(dataSyntax).format(map.get(key));
         map.put(key, value);
        }
      }
    }
	
	public static void numberFormat(JSONArray rowMap, String dataSyntax, String... fields) {
      if(CollectionUtils.isEmpty(rowMap) || StringUtils.isEmpty(dataSyntax) || fields.length <= 0) {
        return ;
      }
      
      for(int i=0; i<rowMap.size(); i++) {
        JSONObject map = rowMap.getJSONObject(i);
        for(String key: fields) {
         if(map.get(key) == null) continue;
         String value = new java.text.DecimalFormat(dataSyntax).format(map.get(key));
         map.put(key, value);
        }
      }
    }
	
	public static void removeRepeateParams4Long(List<Long> params) {
	  if(CollectionUtils.isEmpty(params)) return;
	  
	  for(int i = 0; i < params.size(); i++) {
	    for(int j = i + 1; j < params.size(); j++) {
	      if(params.get(i).longValue() == params.get(j).longValue()) {
	        params.remove(j);
	        --j;
	      }
	    }
	  }
	}
	
	public static void removeRepeateParams4String(List<String> params) {
      if(CollectionUtils.isEmpty(params)) return;
      
      for(int i = 0; i < params.size(); i++) {
        for(int j = i + 1; j < params.size(); j++) {
          if(params.get(i).equals(params.get(j))) {
            params.remove(j);
            --j;
          }
        }
      }
    }
	
	public static void removeRepeateParams4Object(List<Object> params) {
	  if(CollectionUtils.isEmpty(params)) return;
      
      for(int i = 0; i < params.size(); i++) {
        for(int j = i + 1; j < params.size(); j++) {
          if(params.get(i).equals(params.get(j))) {
            params.remove(j);
            --j;
          }
        }
      }
	}
	
	public static JSONArray filterByFields(JSONArray array, String fields) {
	  if(StringUtils.isEmpty(fields) || array == null || array.isEmpty()) {
	    return array;
	  }

	  String[] fieldsArray = fields.trim().split(",");
	  JSONArray result = new JSONArray();
	  for(int i=0; i<array.size(); i++) {
	    JSONObject obj = array.getJSONObject(i);
	    JSONObject tmp = new JSONObject();
	    for(String s: fieldsArray) {
	      Object value = obj.get(s);
	      tmp.put(s, value == null ? "-" : value);
	    }
	    result.add(tmp);
	  }
	  
	  return result;
	}
	
	public static List<Map<String, Object>> filterByFields(List<Map<String, Object>> list, String fields) {
	  if(StringUtils.isEmpty(fields) || CollectionUtils.isEmpty(list)) {
        return list;
      }

      String[] fieldsArray = fields.trim().split(",");
      List<Map<String, Object>> result = new ArrayList<>();
      for(int i=0; i<list.size(); i++) {
        Map<String, Object> obj = list.get(i);
        Map<String, Object> tmp = new HashMap<>();
        for(String s: fieldsArray) {
          Object value = obj.get(s);
          tmp.put(s, value == null ? "-" : value);
        }
        result.add(tmp);
      }
      
      return result;
    }
	
	public static Map<String, Object> filterByFields(Map<String, Object> map, String fields){
	  if(StringUtils.isEmpty(fields) || map == null || map.isEmpty()) {
        return map;
      }
	  
	  Map<String, Object> result = new HashMap<>();
	  String[] fieldsArray = fields.trim().split(",");
	  for(String s : fieldsArray) {
	    Object value = map.get(s);
	    result.put(s, value == null ? "-" : value);
	  }
	  
	  return result;
	}
	
	public static void formatParameter2Array(JSONObject paramsObject, String[] fields) {
		if (fields == null || fields.length <= 0) {
			return;
		}

		for (String s : fields) {
			if (String.class.isInstance(paramsObject.get(s)) || Integer.class.isInstance(paramsObject.get(s))) {
				List<Object> tmp = Arrays.asList(paramsObject.get("s"));
				paramsObject.put(s, tmp);
			}
		}
	}
	
	public static boolean regexValidate(String regex, String value){
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(value);
		return match.matches();
	}
	
	public static void main(String[] args){
		String regex  = "^\\d{0,1}\\.\\d{0,4}$|^\\d{0,1}$";
		String regex2 = "^\\d{0,9}\\.\\d{0,3}$|^\\d{0,9}$";
		String value = "0";
		if(regexValidate(regex, value)){
			System.out.println("符合");
		}else{
			System.out.print("不符合.");
		}
	}
}
