package com.efuture.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Liyj
 * @Description:
 * @Date: 2022/3/17 15:45
 */
public class BeanUtils {
    //动态生产SQL查询select字段，只用循环一次（针对批量插入时）
    public static List xmlList(List list){
        return list!=null && !list.isEmpty()?Arrays.asList(list.get(0)):list;
    }

     public static String formatTime(Date date){
         if(date!=null) {
             return com.product.util.DateTimeFormatUtil.toDateTime(date);
         }
        return null;
     }

    public static <T> T transform(Object src, Class<T> clazz) {
        if (src == null) {
            return null;
        } else {
            Object instance = null;

            try {
                instance = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            org.springframework.beans.BeanUtils.copyProperties(src, instance, getNullPropertyNames(src));
            return (T) instance;
        }
    }

    private static String[] getNullPropertyNames(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set emptyNames = (Set) Arrays.stream(pds).filter((pd) -> {
            return src.getPropertyValue(pd.getName()) == null;
        }).map(FeatureDescriptor::getName).distinct().collect(Collectors.toSet());
        String[] result = new String[emptyNames.size()];
        return (String[]) emptyNames.toArray(result);
    }
}

