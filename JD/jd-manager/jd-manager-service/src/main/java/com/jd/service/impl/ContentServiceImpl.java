package com.jd.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jd.common.pojo.JDReturnResult;
import com.jd.common.redis.JedisClient;
import com.jd.common.utils.JsonUtils;
import com.jd.mapper.TbContentMapper;
import com.jd.pojo.TbContent;
import com.jd.pojo.TbContentExample;
import com.jd.pojo.TbContentExample.Criteria;
import com.jd.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CONTENT_KEY}")
	private String CONTENT_KEY;

	@Override
	public JDReturnResult addContent(TbContent content) {
		// 补全属性
		content.setCreated(new Date());
		content.setUpdated(new Date());
		// 插入数据
		contentMapper.insert(content);
		//缓存同步
		jedisClient.hdel(CONTENT_KEY, content.getCategoryId().toString());
		return JDReturnResult.success();
	}

	@Override
	public List<TbContent> getContentList(long cid) {
		//1.查询缓存
		try {
			String json = jedisClient.hget(CONTENT_KEY, cid + "");
			// 判断json是否为空
			if (StringUtils.isNotBlank(json)) {
				// 把json转换成list
				List<TbContent> list = JsonUtils.jsonToList(json,
						TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//2.根据cid查询内容列表
		TbContentExample example = new TbContentExample();
		// 设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		// 执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		//3.向缓存中添加数据
		try {
			jedisClient.hset(CONTENT_KEY, cid + "",
					JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
