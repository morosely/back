package com.efuture.omdmain.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.product.model.BeanConstant;

public class JSONSerializeUtill{

	public static Object toNameJSONByKey(Object data, Map<String, Object> config) {
		JSONObject jsondata = null;
		// 先把对象转为JSONObject
		try {
			if (config == null || config.size() < 1) {
				jsondata = JSON.parseObject(JSON.toJSONStringWithDateFormat(
						data, "yyyy-MM-dd HH:mm:ss"));
			} else {
				Set<String> keySet = config.keySet();
				jsondata = JSON.parseObject(JSON.toJSONString(data,
						new NameFilter() {
							@Override
							public String process(Object object, String name,
									Object value) {
								for (String field : keySet) {
									if (name.equals(field))
										return (String) config.get(field);
								}
								return name;
							}
						}, SerializerFeature.WriteDateUseDateFormat));
			}
		} catch (Exception ex) {
			return data;
		}

		return jsondata;
	}
	

	public static Object toNameJSONByValue(Object data,
			Map<String, Object> config) {
		JSONObject jsondata = null;
		try {
			if (config == null || config.size() < 1) {
				jsondata = JSON.parseObject(JSON.toJSONStringWithDateFormat(
						data, "yyyy-MM-dd HH:mm:ss"));
			} else {
				Set<Entry<String, Object>> keySet = config.entrySet();
				jsondata = JSON.parseObject(JSON.toJSONString(data,
						new NameFilter() {
							@Override
							public String process(Object object, String name,
									Object value) {
								for (Entry<String, Object> entry : keySet) {
									if (name.equals(entry.getValue()))
										return (String) entry.getKey();
								}
								return name;
							}
						}, SerializerFeature.WriteDateUseDateFormat));
			}
		} catch (Exception ex) {
			return data;
		}

		return jsondata;
	}
	
	public static Object toValueJSONByKey(Object data,
			Map<String, String> config,String flag1,String flag2) {
		JSONObject jsondata = null;
		try {
			if (config == null || config.size() < 1) {
				jsondata = JSON.parseObject(JSON.toJSONStringWithDateFormat(
						data, "yyyy-MM-dd HH:mm:ss"));
			} else {
				Set<String> keySet = config.keySet();
				jsondata = JSON.parseObject(JSON.toJSONString(data,
						new ValueFilter() {
							@Override
							public Object process(Object object, String name,
									Object value) {
								for (String field : keySet) {
									if (name.equals(field)){
										Map<String,Object> map = replitMap(config.get(field),flag1,flag2);
										if(map.containsKey(String.valueOf(value))){
											return map.get(String.valueOf(value));
										}
									}
								}
								return value;
							}
						}, SerializerFeature.WriteDateUseDateFormat));
			}
		} catch (Exception ex) {
			return data;
		}

		return jsondata;
	}
	
	public static Object toValueJSONByKey(Object data,
			Map<String, Object> config) {
		JSONObject jsondata = null;
		try {
			if (config == null || config.size() < 1) {
				jsondata = JSON.parseObject(JSON.toJSONStringWithDateFormat(
						data, "yyyy-MM-dd HH:mm:ss"));
			} else {
				jsondata = JSON.parseObject(JSON.toJSONString(data,
						new ValueFilter() {
							@Override
							public Object process(Object object, String name,
									Object value) {
								if(name.equals(BeanConstant.QueryField.PARAMKEY_ORDERFLD)){
									if(value instanceof String){
										String s = (String)value;
										if(!s.isEmpty()) {
											String orderField = onContins(s,config);
											if(!orderField.isEmpty()){
												return orderField;
											}
										}
									}
								}
								if(value instanceof String){
									String st = (String)value;
									if("Y".equalsIgnoreCase(st)){
										return true;
									}
									if("N".equalsIgnoreCase(st)){
										return false;
									}
								}
								return value;
							}
						}, SerializerFeature.WriteDateUseDateFormat));
			}
		} catch (Exception ex) {
			return data;
		}

		return jsondata;
	}
	
	private static Map<String,Object> replitMap(String str,String flag1,String flag2){
		Map<String,Object> map = new HashMap<String,Object>();
		String ss[] = str.split(flag1);
		if(ss.length>0){
			for(int i =0 ;i<ss.length;i++){
				String[] kv = ss[i].split(flag2);
				if(kv.length==2){
					map.put(kv[0], kv[1]);
				}
			}
		}
		
		return map;
	}
	
	private static String onContins(String value ,Map<String, Object> config){
		String orderField ="";
		String[] ss =  value.split(",");
		for(int i=0;i<ss.length;i++){
			if(config.containsKey(ss[i])){
				if(i==0){
					orderField += config.get(ss[i]);
				}else{
					orderField += ","+config.get(ss[i]);
				}
			}

		}
		return orderField;
	}
	
	
//	 public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
//		 ConcurrentHashMap<Object, Boolean> map = new ConcurrentHashMap<>(16);
//		 return t -> map.putIfAbsent(keyExtractor.apply(t),Boolean.TRUE) == null; 
//		 // 这个也可以，不过感觉效率要低一些，线程不是那么安全
//		 // Set<Object> seen = ConcurrentHashMap.newKeySet(); 
//		 // return t -> seen.add(keyExtractor.apply(t)); }
//	 }
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object distinctByKey(Object o){
		if(o instanceof List){
			List<?> list = (List<?>) o;
			List newjson = new ArrayList();
			list.stream().distinct().forEach(newjson::add);
			return newjson;
		}
		return null;
	}

	
//	public static void main(String[] args) {
//		Map<String, String> config = new HashMap<String, String>();
//		config.put("lastflag", "1:Y,0:N");
//		Map<String, Object> config1 = new HashMap<String, Object>();
//		config1.put("lastflag", "leafFlag");
//		
//		Map<String, Object> config2 = new HashMap<String, Object>();
//		config2.put("lastflag", "leafFlag");
//		config2.put("aa", "code");
//		config2.put("aa1", "name");
//		config2.put("aa11", "cs");
//		
//		JSONObject jsondata = new JSONObject();
//		jsondata.put("lastflag", "Y");
//		jsondata.put("aa", "0");
//		jsondata.put("aa11", "11");
//		jsondata.put("aa112", "234");
//		jsondata.put("order_field" , "lastflag,aa,aa11");
//		
//		jsondata = (JSONObject) toValueJSONByKey(jsondata,config2);
//		System.out.println(jsondata);
////		System.out.println(toValueJSONByKey(jsondata,config2));
//		
//		
////		List<Map> list = new ArrayList<>();
////		List<Map> collect = list.stream().filter(distinctByKey(Map::get)).collect(Collectors.toList());
////		System.out.println(JSON.toJSONString(collect));
//
//		List<Map> list = new ArrayList<>();
//		Map map1 = new HashMap();
//		map1.put("a", "1");
//		Map map2 = new HashMap();
//		map2.put("a", "1");
//		Map map3 = new HashMap();
//		map3.put("a", "11");
//		list.add(map1);
//		list.add(map2);
//		list.add(map3);
//		list.stream().distinct().forEach(System.out::println);
//		
//	}
}
