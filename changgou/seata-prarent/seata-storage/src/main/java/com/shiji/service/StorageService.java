package com.shiji.service;


import com.shiji.dao.StorageDao;
import com.shiji.model.Storage;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@Slf4j
public class StorageService {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";

    @Autowired
    private StorageDao storageDao;

    @Transactional
    public String storage(String commodityCode,int count) {
        log.info("----------》》》 Storage Service Begin ... xid: " + RootContext.getXID());
        Storage storage = new Storage();
        storage.setCommodityCode(commodityCode);
        storage = storageDao.selectOne(storage);
        //人为制造异常
//        Integer nowStorageCount = storage.getCount();
//        if(nowStorageCount == 98){
//            throw new RuntimeException("库存服务人为异常。。。");
//        }
        storage.setCount(storage.getCount() - count);
        int result = storageDao.updateByPrimaryKeySelective(storage);
        log.info("----------》》》 库存受影响函数："+result);
        log.info("----------》》》 Storage Service End ... ");
        if (result == 1) {
            return SUCCESS;
        }
        return FAIL;
    }
}
