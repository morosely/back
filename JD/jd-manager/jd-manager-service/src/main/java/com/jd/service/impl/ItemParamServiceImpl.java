package com.jd.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.common.pojo.JDReturnResult;
import com.jd.mapper.TbItemParamMapper;
import com.jd.pojo.TbItemParam;
import com.jd.pojo.TbItemParamExample;
import com.jd.pojo.TbItemParamExample.Criteria;
import com.jd.service.ItemParamService;

@Service
public class ItemParamServiceImpl implements ItemParamService {

	@Autowired
	private TbItemParamMapper itemParamMapper;
	
	@Override
	public JDReturnResult getItemParamByCid(long cid) {
		TbItemParamExample example = new TbItemParamExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemCatIdEqualTo(cid);
		List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);
		//判断是否查询到结果
		if (list != null && list.size() > 0) {
			return JDReturnResult.success(list.get(0));
		}
		return JDReturnResult.success();
	}
	
	@Override
	public JDReturnResult insertItemParam(TbItemParam itemParam) {
		//补全pojo
		itemParam.setCreated(new Date());
		itemParam.setUpdated(new Date());
		//插入到规格参数模板表
		itemParamMapper.insert(itemParam);
		return JDReturnResult.success();
	}
}
