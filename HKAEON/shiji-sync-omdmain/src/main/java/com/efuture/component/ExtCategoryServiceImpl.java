package com.efuture.component;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.efuture.common.CommonExtSyncService;
import com.efuture.model.ExtCategoryModel;

public class ExtCategoryServiceImpl extends CommonExtSyncService<ExtCategoryModel,ExtCategoryServiceImpl> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtCategoryServiceImpl.class);

    public ExtCategoryServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }


    /*@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
    @Override
    public String receive(ServiceSession session, JSONObject param) throws Exception {
        long start = System.currentTimeMillis();

        Object o = param.get(getTableName());
        SqlSessionTemplate template = getTemplate();
        Class beanClass = ExtCategoryModel.class;

        List<JSONObject> list = JSON.parseArray(JSON.toJSONStringWithDateFormat(o, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteMapNullValue), JSONObject.class);

        for (JSONObject t : list) {
            t.put("updateDate", new Date());
            t.put("createDate", new Date());
            t.put("dealStatus", 0);
        }

        template.insert("ExtCategoryModelMapper.insertAll", list);
        LOGGER.info("插入完成,size[{}], 表[{}]......", list.size(), "extcategory");
        return "success";
    }
*/

}
