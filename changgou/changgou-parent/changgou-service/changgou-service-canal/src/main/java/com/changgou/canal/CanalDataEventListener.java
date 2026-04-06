package com.changgou.canal;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.common.Result;
import com.xpand.starter.canal.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.changgou.content.feign.ContentFeign;
import com.changgou.content.pojo.Content;

import java.util.List;

@CanalEventListener
public class CanalDataEventListener {

//    /***
//     * 增加数据监听
//     * @param eventType
//     * @param rowData
//     */
//    @InsertListenPoint
//    public void onEventInsert(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
////        rowData.getAfterColumnsList().forEach((c) -> System.out.println("By--Annotation: " + c.getName() + " ::   " + c.getValue()));
//        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
//            System.out.println("新增数据：列名" + column.getName() + "-----变更的数据："+ column.getValue());
//        }
//    }
//    /***
//     * 修改数据监听
//     * @param rowData
//     */
//    @UpdateListenPoint
//    public void onEventUpdate(CanalEntry.RowData rowData) {
////        rowData.getAfterColumnsList().forEach((c) -> System.out.println("By--Annotation: " + c.getName() + " ::   " + c.getValue()));
//        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
//            System.out.println("修改前：列名" + column.getName() + "-----变更的数据："+ column.getValue());
//        }
//
//        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
//            System.out.println("修改后：列名" + column.getName() + "-----变更的数据："+ column.getValue());
//        }
//    }
//
//    /***
//     * 删除数据监听
//     * @param eventType
//     */
//    @DeleteListenPoint
//    public void onEventDelete(CanalEntry.EventType eventType,CanalEntry.RowData rowData) {
//        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
//            System.out.println("删除前：列名" + column.getName() + "-----变更的数据："+ column.getValue());
//        }
//    }
//    /***
//     * 自定义数据修改监听
//     * @param eventType
//     * @param rowData
//     */
//    @ListenPoint(
//            eventType = {CanalEntry.EventType.UPDATE,CanalEntry.EventType.DELETE},
//            schema = {"changgou_content"},
//            table = {"tb_content_category", "tb_content"},
//            destination = "example"
//    )
//    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
//        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
//            System.out.println("自定义-----变更前>：列名" + column.getName() + "-----变更的数据："+ column.getValue());
//        }
//
//        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
//            System.out.println("自定义-----变更后>：列名" + column.getName() + "-----变更的数据："+ column.getValue());
//        }
//    }

    @Autowired
    private ContentFeign contentFeign;
    //字符串
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ListenPoint(
            eventType = {CanalEntry.EventType.UPDATE,CanalEntry.EventType.DELETE,CanalEntry.EventType.INSERT}, // 监听类型
            schema = {"changgou_content"}, //监听数据库名称
            table = {"tb_content", "tb_content_category"}, //监听额表
            destination = "example"
    )
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.out.println("自定义监听 -----> " + eventType.name());
        //1.获取列名 为category_id的值
        String categoryId = getColumnValue(eventType, rowData);
        //2.调用feign 获取该分类下的所有的广告集合
        Result<List<Content>> categoryresut = contentFeign.findByCategory(Long.valueOf(categoryId));
        List<Content> data = categoryresut.getData();
        //3.使用redisTemplate存储到redis中
        stringRedisTemplate.boundValueOps("content_" + categoryId).set(JSON.toJSONString(data));
    }
    //自定义数据库的 操作来监听
    //destination = "example"

    private String getColumnValue(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        String categoryId = "";
        //判断 如果是删除  则获取beforlist
        if (eventType == CanalEntry.EventType.DELETE) {
            for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
                if (column.getName().equalsIgnoreCase("category_id")) {
                    categoryId = column.getValue();
                    return categoryId;
                }
            }
        } else {
            //判断 如果是添加 或者是更新 获取afterlist
            for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
                if (column.getName().equalsIgnoreCase("category_id")) {
                    categoryId = column.getValue();
                    return categoryId;
                }
            }
        }
        return categoryId;
    }

}
