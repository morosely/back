package com.efuture.omdmain.component;

import java.io.OutputStream;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.FileProcessorUtils;

/**
 *      导出
 */
public class ExportExcelServiceImpl extends JDBCCompomentServiceImpl<Object> {

	public ExportExcelServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
		super(mybatisTemplate, collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}
	
	public ServiceResponse onExportData(ServiceSession session, String params, OutputStream os) {
		try {
			JSONObject jsonparam = JSONObject.parseObject(params);
			String type = jsonparam.getString("type");
			String token = jsonparam.getString("token");
			// 处理ent_id
			if (StringUtils.isEmpty(session)) {
				session = new ServiceSession();
				session.setEnt_id(0L);
			} else {
				if (StringUtils.isEmpty(session.getEnt_id())) {
					session.setEnt_id(0L);
				}
			}
			jsonparam.put("url", jsonparam.getString("url") + "&token=" + token+ "&ent_id=" + session.getEnt_id());
			String url = jsonparam.getString("url");
			System.out.println("=================url:" + url);
			String jparams = jsonparam.getString("params");
			String cols = jsonparam.getString("cols");
			String tname = jsonparam.getString("tname");
			String istree = jsonparam.getString("istree");
			String childname = jsonparam.getString("childname");
			FileProcessorUtils.exportFile(type, url, jparams, cols, tname, istree, childname, os);
			return ServiceResponse.buildSuccess("");
		} catch (Exception e) {
			ServiceResponse sr = new ServiceResponse();
			sr.setReturncode(ResponseCode.EXCEPTION);
			sr.setData("导出文件失败：" + e.getMessage());
			return sr;
		}
	}

}
