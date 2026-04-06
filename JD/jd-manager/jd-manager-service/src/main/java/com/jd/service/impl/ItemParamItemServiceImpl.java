package com.jd.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.common.utils.JsonUtils;
import com.jd.mapper.TbItemParamItemMapper;
import com.jd.pojo.TbItemParamItem;
import com.jd.pojo.TbItemParamItemExample;
import com.jd.pojo.TbItemParamItemExample.Criteria;
import com.jd.service.ItemParamItemService;

@Service
public class ItemParamItemServiceImpl implements ItemParamItemService {
	
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;

	@Override
	public String getItemParamByItemId(long itemId) {
		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
		if (null == list || list.isEmpty()) {
			return "";
		}
		//取出参数信息
		TbItemParamItem itemParamItem = list.get(0);
		String paramData = itemParamItem.getParamData();
		//把json数据转换成java对象
		List<Map> paramList = JsonUtils.jsonToList(paramData, Map.class);
		//将参数信息转换成html
		StringBuffer sb = new StringBuffer(); 
		//sb.append("<div>");
		sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\">\n");
		sb.append("    <tbody>\n");
		for (Map map : paramList) {
			sb.append("        <tr>\n");
			sb.append("            <th class=\"tdTitle\" colspan=\"2\">"+map.get("group")+"</th>\n");
			sb.append("        </tr>\n");
			List<Map> params = (List<Map>) map.get("params");
			for (Map itemMap : params) {
				sb.append("        <tr>\n");
				sb.append("            <td class=\"tdTitle\">"+itemMap.get("k")+"</td>\n");
				sb.append("            <td>"+itemMap.get("v")+"</td>\n");
				sb.append("        </tr>\n");
			}
		}
		sb.append("    </tbody>\n");
		sb.append("</table>");
		//sb.append("</div>");
		return sb.toString();
	}
}
