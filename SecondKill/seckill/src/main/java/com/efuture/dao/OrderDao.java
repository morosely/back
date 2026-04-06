package com.efuture.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.efuture.domain.MiaoshaOrder;
import com.efuture.domain.OrderInfo;

@Mapper
public interface OrderDao {
	
	@Select("select * from miaoshaorder where userId=#{userId} and goodsId=#{goodsId}")
	public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId")long goodsId);

	@Insert("insert into orderinfo(userId, goodsId, goodsName, goodsCount, goodsPrice, orderChannel, status, createDate)values("
			+ "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
	@SelectKey(statement = "select last_insert_id()",keyProperty = "id",keyColumn = "id",resultType = long.class,before = false)
	public long insert(OrderInfo orderInfo);
	
	@Insert("insert into miaoshaorder (userId, goodsId, orderId)values(#{userId}, #{goodsId}, #{orderId})")
	public int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

	@Select("select * from orderinfo where id = #{orderId}")
	public OrderInfo getOrderById(@Param("orderId")long orderId);

	@Delete("delete from order_info")
	public void deleteOrders();

	@Delete("delete from miaosha_order")
	public void deleteMiaoshaOrders();
}
