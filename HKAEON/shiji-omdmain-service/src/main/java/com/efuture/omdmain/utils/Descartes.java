package com.efuture.omdmain.utils;

import java.util.ArrayList;
import java.util.List;


/** 
* @author yihaitao
* @time 2018年5月16日 下午4:37:02 
* 
*/
import com.efuture.omdmain.model.SonGoodsPropertyBean;

public class Descartes {

	/** 
     * 递归实现dimValue中的笛卡尔积，结果放在result中 
     * @param originalValue 原始数据 
     * @param resultValue 结果数据 
     */  
	public static void recursive (List<List<SonGoodsPropertyBean>> originalValue, List<List<SonGoodsPropertyBean>> resultValue, int layer, List<SonGoodsPropertyBean> curList) {  
        if (layer < originalValue.size() - 1) {  
            if (originalValue.get(layer).size() == 0) {  
                recursive(originalValue, resultValue, layer + 1, curList);  
            } else {  
                for (int i = 0; i < originalValue.get(layer).size(); i++) {  
                    List<SonGoodsPropertyBean> list = new ArrayList<SonGoodsPropertyBean>(curList);  
                    list.add(originalValue.get(layer).get(i));  
                    recursive(originalValue, resultValue, layer + 1, list);  
                }  
            }  
        } else if (layer == originalValue.size() - 1) {  
            if (originalValue.get(layer).size() == 0) {  
                resultValue.add(curList);  
            } else {  
                for (int i = 0; i < originalValue.get(layer).size(); i++) {  
                    List<SonGoodsPropertyBean> list = new ArrayList<SonGoodsPropertyBean>(curList);  
                    list.add(originalValue.get(layer).get(i));  
                    resultValue.add(list);  
                }  
            }  
        }  
    }  
 
    public static void main (String[] args) {  
        List<SonGoodsPropertyBean> list1 = new ArrayList<SonGoodsPropertyBean>();  
        SonGoodsPropertyBean son11 = new SonGoodsPropertyBean();
        son11.setPropertyCode("1");
        son11.setPropertyName("尺寸");
        son11.setPropertyDesc("35");
        list1.add(son11);  
        SonGoodsPropertyBean son12 = new SonGoodsPropertyBean();
        son11.setPropertyCode("1");
        son12.setPropertyName("尺寸");
        son12.setPropertyDesc("36");
        list1.add(son12);  
  
        List<SonGoodsPropertyBean> list2 = new ArrayList<SonGoodsPropertyBean>();  
        SonGoodsPropertyBean son21 = new SonGoodsPropertyBean();
        son11.setPropertyCode("2");
        son21.setPropertyName("颜色");
        son21.setPropertyDesc("白色");
        list2.add(son21);  
        SonGoodsPropertyBean son22 = new SonGoodsPropertyBean();
        son11.setPropertyCode("2");
        son22.setPropertyName("颜色");
        son22.setPropertyDesc("黑色");
        list2.add(son22);   
  
        List<SonGoodsPropertyBean> list3 = new ArrayList<SonGoodsPropertyBean>();  
        SonGoodsPropertyBean son31 = new SonGoodsPropertyBean();
        son11.setPropertyCode("3");
        son31.setPropertyName("材料");
        son31.setPropertyDesc("塑料");
        list3.add(son31);  
        SonGoodsPropertyBean son32 = new SonGoodsPropertyBean();
        son11.setPropertyCode("3");
        son32.setPropertyName("材料");
        son32.setPropertyDesc("硅胶");
        list3.add(son32); 
        SonGoodsPropertyBean son33 = new SonGoodsPropertyBean();
        son11.setPropertyCode("3");
        son33.setPropertyName("材料");
        son33.setPropertyDesc("玻璃");
        list3.add(son33);

        List<List<SonGoodsPropertyBean>> dimValue = new ArrayList<List<SonGoodsPropertyBean>>();  
        dimValue.add(list1);  
        dimValue.add(list2);   
        dimValue.add(list3);   
  
        // 递归实现笛卡尔积  
        List<List<SonGoodsPropertyBean>> recursiveResult = new ArrayList<List<SonGoodsPropertyBean>>();  
        recursive(dimValue, recursiveResult, 0, new ArrayList<SonGoodsPropertyBean>());  
  
        System.out.println("递归实现笛卡尔乘积: 共 " + recursiveResult.size() + " 个结果");  
        for (List<SonGoodsPropertyBean> list : recursiveResult) {  
            for (SonGoodsPropertyBean model : list) {  
                System.out.print(model.getPropertyDesc() + " ");  
            }  
            System.out.println(list);  
        }  
    }  
}
