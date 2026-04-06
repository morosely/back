package com.shiji.dao;

import com.shiji.model.Order;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.base.BaseInsertProvider;

public interface OrderDao extends Mapper<Order> {

}
