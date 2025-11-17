package com.efuture.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface OrderCallNoMapper {
    @Insert("${sql}")
    int insert(@Param("sql") String sql);
    @Update("${sql}")
    int update(@Param("sql") String sql);
    @Delete("${sql}")
    int delete( @Param("sql") String sql);
    @Select("${sql}")
    JSONObject selectOneSql(@Param("sql") String sql);
    @Select("${sql}")
    List<JSONObject> selectListSql(@Param("sql") String sql);
    @Select("${sql}")
    @ResultType(com.alibaba.fastjson.JSONObject.class)
    public List<com.alibaba.fastjson.JSONObject> selectListSqlFastjson(@Param("sql") String sql);
    @Select("${sql}")
    int count(@Param("sql") String sql);
}