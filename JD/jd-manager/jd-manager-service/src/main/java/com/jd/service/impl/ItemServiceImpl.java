package com.jd.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jd.common.pojo.EasyUIDataGridResult;
import com.jd.common.pojo.JDReturnResult;
import com.jd.common.redis.JedisClient;
import com.jd.common.utils.IDUtils;
import com.jd.common.utils.JsonUtils;
import com.jd.mapper.TbItemDescMapper;
import com.jd.mapper.TbItemMapper;
import com.jd.mapper.TbItemParamItemMapper;
import com.jd.pojo.TbItem;
import com.jd.pojo.TbItemDesc;
import com.jd.pojo.TbItemExample;
import com.jd.pojo.TbItemParamItem;
import com.jd.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name="itemChangeTopicDestination")
	private Destination topicDestination;
	@Value("${ITEM_INFO}")
	private String ITEM_INFO;
	@Value("${TIEM_EXPIRE}")
	private Integer TIEM_EXPIRE;
	@Autowired
	private JedisClient jedisClient;

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//查询数据库之前先查询缓存
		try {
			String json = jedisClient.get(ITEM_INFO + ":" + itemId  + ":DESC");
			if (StringUtils.isNotBlank(json)) {
				// 把json数据转换成pojo
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存中没有查询数据库
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		try {
			//把查询结果添加到缓存
			jedisClient.set(ITEM_INFO + ":" + itemId  + ":DESC", JsonUtils.objectToJson(itemDesc));
			//设置过期时间，提高缓存的利用率
			jedisClient.expire(ITEM_INFO + ":" + itemId  + ":DESC", TIEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}
	
	@Override
	public TbItem getItemById(long itemId) {
		//查询数据库之前先查询缓存
		try {
			String json = jedisClient.get(ITEM_INFO + ":" + itemId  + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				// 把json数据转换成pojo
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存中没有查询数据库
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		try {
			//把查询结果添加到缓存
			jedisClient.set(ITEM_INFO + ":" + itemId  + ":BASE", JsonUtils.objectToJson(item));
			//设置过期时间，提高缓存的利用率
			jedisClient.expire(ITEM_INFO + ":" + itemId  + ":BASE", TIEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		// 取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);

		// 创建返回结果对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);

		return result;
	}

	@Override
	public JDReturnResult addItem(TbItem item, String desc,String itemParams) {
		//生成商品id
		final long itemId = IDUtils.genItemId();
		Date currentData = new Date();
		//补全item的属性
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(currentData);
		item.setUpdated(currentData);
		//1.向商品表插入数据
		itemMapper.insert(item);
		//创建一个商品描述表对应的pojo
		TbItemDesc itemDesc = new TbItemDesc();
		//补全pojo的属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(currentData);
		itemDesc.setCreated(currentData);
		//2.向商品描述表插入数据
		itemDescMapper.insert(itemDesc);
		//3.添加商品规格
		TbItemParamItem itemParamItem = new TbItemParamItem();
		itemParamItem.setItemId(itemId);
		itemParamItem.setParamData(itemParams);
		itemParamItem.setCreated(currentData);
		itemParamItem.setUpdated(currentData);
		
		itemParamItemMapper.insert(itemParamItem);
		//4发送一个商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});
		//返回结果
		return JDReturnResult.success();
	}
}
