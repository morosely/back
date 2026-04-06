package com.efuture.eshop.inventory.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.efuture.eshop.inventory.dao.RedisDAO;
import com.efuture.eshop.inventory.mapper.GoodsMapper;
import com.efuture.eshop.inventory.model.Goods;
import com.efuture.eshop.inventory.service.GoodsService;

@Service("goodsService")  
public class GoodsServiceImpl implements GoodsService {

	@Resource
	private GoodsMapper goodsMapper;
	@Resource
	private RedisDAO redisDAO;
	
	@Override
	public void updateGoods(Goods goods) {
		goodsMapper.updateGoods(goods);
	}

	@Override
	public void removeGoodsCache(Goods goods) {
		String key = "eshop:goods:" + goods.getGoodsId();
		redisDAO.delete(key);
	}

	@Override
	public Goods findGoods(Long goodsId) {
		return goodsMapper.findGoods(goodsId);
	}

	//设置商品库存的缓存
	@Override
	public void setGoodsCache(Goods goods) {
		String key = "eshop:goods:" + goods.getGoodsId();
		redisDAO.set(key, String.valueOf(goods.getStock()));
	}

	//获取商品库存的缓存
	public Goods getGoodsCache(Long goodsId) {
		Integer stock = 0;
		String key = "eshop:goods:" + goodsId;
		String result = redisDAO.get(key);
		
		if(result != null && !"".equals(result)) {
			try {
				stock = Integer.valueOf(result);
				return new Goods(goodsId, stock);
			} catch (Exception e) {
				e.printStackTrace(); 
			}
		}
		return null;
	}
}
