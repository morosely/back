package com.efuture.component;

import com.efuture.common.CommonSyncService;
import com.efuture.model.SaleGoodsItemsModel;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaleGoodsItemsServiceImpl extends CommonSyncService<SaleGoodsItemsModel, SaleGoodsItemsServiceImpl> {
    public SaleGoodsItemsServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }

    @Override
    public void resetIdByCode() {
        //1.设置可售商品ID(ssgid)
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("targetTable", "salegoodsitems");
        paramMap.put("sourceTable", "goods");
        paramMap.put("targetCode", "ggoodsCode");
        paramMap.put("sourceCode", "goodsCode");
        paramMap.put("targetId", "gsgid");
        paramMap.put("sourceId", "sgid");
        int updateIdCount = this.getSqlSessionTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
        this.logger.info("==========》》》设置组成码的商品Id。更新数量：" + updateIdCount);
        
        //2.更新goods的组成码（香港箱码）的商品类型
        int goodsTypeCount = this.getSqlSessionTemplate().update("GoodsModelMapper.updateGoodsType", null);
        this.logger.info("==========》》》更新goods的组成码（香港箱码）的商品类型。更新数量：" + goodsTypeCount);
    }
}
