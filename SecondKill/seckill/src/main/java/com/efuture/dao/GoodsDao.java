package com.efuture.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.efuture.domain.MiaoshaGoods;
import com.efuture.vo.GoodsVo;

@Mapper
public interface GoodsDao {
	
	@Select("select g.*,mg.* from miaoshagoods mg left join goods g on mg.goodsId = g.id")
	public List<GoodsVo> listGoodsVo();

	@Select("select g.*,mg.* from miaoshagoods mg left join goods g on mg.goodsId = g.id where g.id = #{goodsId}")
	public GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);

	@Update("update miaoshagoods set stockCount = stockCount - 1 where goodsId = #{goodsId} and stockCount > 0")
	public int reduceStock(MiaoshaGoods g);
	
	@Update("update miaosha_goods set stock_count = #{stockCount} where goods_id = #{goodsId}")
	public int resetStock(MiaoshaGoods g);
}
