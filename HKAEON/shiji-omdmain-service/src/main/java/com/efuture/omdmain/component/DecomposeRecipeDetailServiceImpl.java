package com.efuture.omdmain.component;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.DecomposeRecipeDetailModel;
import com.efuture.omdmain.service.DecomposeRecipeDetailService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

/**
 * 分解配方单明细Service
 *
 */
public class DecomposeRecipeDetailServiceImpl extends JDBCCompomentServiceImpl<DecomposeRecipeDetailModel> implements DecomposeRecipeDetailService {

	public DecomposeRecipeDetailServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	// 删除分解配方单明细
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse delete(ServiceSession session, JSONObject paramsObject) {
		
		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (!paramsObject.containsKey("drdids")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "分解配方单明细ID不能为空");
		}
		
		JSONArray dRDList = paramsObject.getJSONArray("drdids");
		if (dRDList.isEmpty()) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "请勾选要删除的配方明细.");
		}
		
		FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        long rows = template.getSqlSessionTemplate().delete("beanmapper.DecomposeRecipeDetailModelMapper.deleteByDrdid", paramsObject);
        
		return ServiceResponse.buildSuccess("删除成功, 共删除" + rows + "条记录.");
	}

}
