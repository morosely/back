package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.common.Result;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SkuEsMapper skuEsMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Override
    public void importEs() {
        //1.调用 goods微服务的fegin 查询 符合条件的sku的数据
        Result<List<Sku>> skuResult = skuFeign.findAll();
        List<Sku> data = skuResult.getData();//sku的列表
        //将sku的列表 转换成es中的skuinfo的列表
        List<SkuInfo> skuInfos = JSON.parseArray(JSON.toJSONString(data), SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            //获取规格的数据  {"电视音响效果":"立体声","电视屏幕尺寸":"20英寸","尺码":"165"}
            //转成MAP  key: 规格的名称  value:规格的选项的值
            Map<String, Object> map = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(map);
        }

        // 2.调用spring data elasticsearch的API 导入到ES中
        skuEsMapper.saveAll(skuInfos);
    }

    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        //1.获取关键字的值
        String keywords = searchMap.get("keywords");

        //2.判断是否为空 如果 为空 给一个默认 值:华为
        if (StringUtils.isEmpty(keywords)) {
            keywords = "华为";
        }

        //3.创建 查询构建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        //3.设置查询的条件
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name", keywords));
        // terms  指定分组的一个别名
        // field 指定要分组的字段名
        // size 指定查询结果的数量 默认是10个
        // 3.1.1 商品分类的列表展示: 按照商品分类的名称来分组
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategorygroup").field("categoryName").size(50));
        // 3.1.2 设置分组条件  商品品牌
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrandgroup").field("brandName").size(50));
        //3.1.3 设置分组条件  商品的规格
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpecgroup").field("spec.keyword").size(10000));

        //3.2.1 过滤查询的条件设置 商品品类的条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String category = searchMap.get("category");
        if(!StringUtils.isEmpty(category)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryName", category));
        }
        // 3.2.2 过滤查询的条件设置   商品品牌的条件
        String brand = searchMap.get("brand");
        if(!StringUtils.isEmpty(brand)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("brandName", brand));
        }
        //构建过滤查询
        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);
        //4.构建查询对象
        NativeSearchQuery query = nativeSearchQueryBuilder.build();

        //5.执行查询
        AggregatedPage<SkuInfo> skuPage = elasticsearchTemplate.queryForPage(query, SkuInfo.class);
        // 5.1 获取分组结果  商品分类
        StringTerms stringTermsCategory = (StringTerms) skuPage.getAggregation("skuCategorygroup");
        List<String> categoryList = getStringsCategoryList(stringTermsCategory);
        // 5.2 获取分组结果  商品品牌
        StringTerms stringTermsBrand = (StringTerms) skuPage.getAggregation("skuBrandgroup");
        List<String> brandList = getStringsBrandList(stringTermsBrand);
        // 5.3 获取分组结果  商品规格数据
        StringTerms stringTermsSpec = (StringTerms) skuPage.getAggregation("skuSpecgroup");
        Map<String, Set<String>> specMap = getStringSetMap(stringTermsSpec);

        //6.返回结果
        Map resultMap = new HashMap<>();
        resultMap.put("rows", skuPage.getContent());// 商品分类的列表数据
        resultMap.put("total", skuPage.getTotalElements());
        resultMap.put("totalPages", skuPage.getTotalPages());
        resultMap.put("categoryList", categoryList);
        resultMap.put("brandList", brandList);
        resultMap.put("specMap", specMap);

        return resultMap;
    }

    //获取分类列表数据
    private List<String> getStringsCategoryList(StringTerms stringTermsCategory) {
        List<String> categoryList = new ArrayList<>();
        if (stringTermsCategory != null) {
            for (StringTerms.Bucket bucket : stringTermsCategory.getBuckets()) {
                String keyAsString = bucket.getKeyAsString();//分组的值
                categoryList.add(keyAsString);
            }
        }
        return categoryList;
    }

    //获取分类品牌信息
    private List<String> getStringsBrandList(StringTerms stringTermsBrand) {
        List<String> brandList = new ArrayList<>();
        if (stringTermsBrand != null) {
            for (StringTerms.Bucket bucket : stringTermsBrand.getBuckets()) {
                brandList.add(bucket.getKeyAsString());
            }
        }
        return brandList;
    }


    //获取规格列表数据
    private Map<String, Set<String>> getStringSetMap(StringTerms stringTermsSpec) {
        Map<String, Set<String>> specMap = new HashMap<String, Set<String>>();
        Set<String> specList = new HashSet<>();
        //Json串可鞥有重复的规格值，去重 ???
        if (stringTermsSpec != null) {
            for (StringTerms.Bucket bucket : stringTermsSpec.getBuckets()) {
                specList.add(bucket.getKeyAsString());
            }
        }
        //去重后，再统计
        for (String specjson : specList) {
            Map<String, String> map = JSON.parseObject(specjson, Map.class);
            for (Map.Entry<String, String> entry : map.entrySet()) {//{颜色=墨色, 版本=6GB+64GB}
                String key = entry.getKey();        //规格名字
                String value = entry.getValue();    //规格选项值
                //获取当前规格名字对应的规格数据
                Set<String> specValues = specMap.get(key);
                if (specValues == null) {
                    specValues = new HashSet<String>();
                }
                //将当前规格加入到集合中
                specValues.add(value);
                //将数据存入到specMap中
                specMap.put(key, specValues);
            }
        }
        return specMap;
    }
}
