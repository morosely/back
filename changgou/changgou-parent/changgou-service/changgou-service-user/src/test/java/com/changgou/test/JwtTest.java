package com.changgou.test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.*;

public class JwtTest {
    /**
     * 创建Token
     */
    @Test
    public void testCreateJwt() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1); //把日期往后增加一天,整数  往后推,负数往前移动
        Date nextDate = calendar.getTime(); //这个时间就是日期往后推一天的结果
        JwtBuilder builder = Jwts.builder()
                .setId("888")             //设置唯一编号
                .setSubject("小白")       //设置主题  可以是JSON数据
                .setIssuedAt(date)  //设置签发日期
                .setExpiration(nextDate)//用于设置过期时间 ，参数为Date类型数据
                .signWith(SignatureAlgorithm.HS256, "itcast");//设置签名 使用HS256算法，并设置SecretKey(字符串)
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("name","admin");
        userInfo.put("age",20);
        userInfo.put("address","武汉市武昌螃蟹岬");
        builder.addClaims(userInfo);
        //构建 并返回一个字符串
        System.out.println(builder.compact());
    }

    /**
     * 解析Token
     */

    @Test
    public void testParseJwt(){
        String compactJwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE1OTQ4ODk3NjEsImV4cCI6MTU5NDk3NjE2MSwiYWRkcmVzcyI6IuatpuaxieW4guatpuaYjOieg-ifueWyrCIsIm5hbWUiOiJhZG1pbiIsImFnZSI6MjB9.rMvh28POomZ2LWgwMyGhdaCnHVkAy2fGJgVYe1fZG1w";
        Claims claims = Jwts.parser().
                setSigningKey("itcast").
                parseClaimsJws(compactJwt).
                getBody();
        System.out.println(claims);
    }
}
