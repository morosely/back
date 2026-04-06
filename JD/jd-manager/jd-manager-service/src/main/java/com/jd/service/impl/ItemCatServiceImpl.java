package com.jd.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.common.pojo.EasyUITreeNode;
import com.jd.manager.pojo.CatNode;
import com.jd.manager.pojo.CatResult;
import com.jd.mapper.TbItemCatMapper;
import com.jd.pojo.TbItemCat;
import com.jd.pojo.TbItemCatExample;
import com.jd.pojo.TbItemCatExample.Criteria;
import com.jd.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public CatResult getItemCatMenu() {
		// 调用递归方法查询商品分类列表
		List catList = getItemCatMenu(0l);
		// 返回结果
		CatResult result = new CatResult();
		result.setData(catList);
		return result;
	}

	private List getItemCatMenu(Long parentId) {
		// 根据parentId查询列表
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//向list中添加节点
		int count = 0;
		List resultList = new ArrayList<>();
		for (TbItemCat tbItemCat : list) {
			// 如果是父节点
			if (tbItemCat.getIsParent()) {
				CatNode node = new CatNode();
				node.setUrl("/products/" + tbItemCat.getId() + ".html");
				// 如果当前节点为第一级节点
				if (tbItemCat.getParentId() == 0) {
					node.setName("<a href='/products/" + tbItemCat.getId()
							+ ".html'>" + tbItemCat.getName() + "</a>");
				} else {
					node.setName(tbItemCat.getName());
				}
				node.setItem(getItemCatMenu(tbItemCat.getId()));
				// 把node添加到列表
				resultList.add(node);
				count ++;
				//第一层只取14条记录
				if (parentId == 0 && count >=14) {
					break;
				}
			} else {
				// 如果是叶子节点
				String item = "/products/" + tbItemCat.getId() + ".html|"
						+ tbItemCat.getName();
				resultList.add(item);
			}
		}
		return resultList;
	}

	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		// 根据父节点id查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		// 设置查询条件
		Criteria criteria = example.createCriteria();
		// 设置parentid
		criteria.andParentIdEqualTo(parentId);
		// 执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		// 转换成EasyUITreeNode列表
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			// 如果节点下有子节点“closed”，如果没有子节点“open”
			node.setState(tbItemCat.getIsParent() ? "closed" : "open");
			// 添加到节点列表
			resultList.add(node);
		}
		return resultList;
	}

}
