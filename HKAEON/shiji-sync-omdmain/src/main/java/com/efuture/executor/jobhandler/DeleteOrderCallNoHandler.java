package com.efuture.executor.jobhandler;

import com.efuture.component.SSHService;
import com.efuture.mapper.OrderCallNoMapper;
import com.efuture.utils.SSHEntity;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@JobHandler("deleteOrderCallNoHandler")
@Component
@Slf4j
public class DeleteOrderCallNoHandler extends IJobHandler {

    public Logger logger = LoggerFactory.getLogger(DeleteOrderCallNoHandler.class);
    @Autowired
    private OrderCallNoMapper orderCallNoMapper;
    @Autowired
    private SSHService sshService;
    @Resource(name = "ssh")
    private SSHEntity sshEntity;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        //计算当天凌晨时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date today = new Date();
        String zeroTime = formatter.format(today);

        //计算当前时间点的前一个月时间点
        Date endDate = new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(endDate);
        rightNow.add(Calendar.MONTH,-1);
        Date startDate = rightNow.getTime();
        String oneMonth = formatter.format(startDate);

        //删除IDC数据
        String deleteSQL = String.format("delete from ordercallno where updateDate <='%s'",zeroTime);
        int deleteCount = orderCallNoMapper.delete(deleteSQL);
        String deleteSQLHis = String.format("delete from ordercallnohis where updateDate <='%s'",oneMonth);
        logger.info(" ==========>>>>> 【DeleteOrderCallNoHandler IDC删除 orderCallNo:】{}",deleteCount);
        int deleteCountHis = orderCallNoMapper.delete(deleteSQLHis);
        logger.info(" ==========>>>>> 【DeleteOrderCallNoHandler IDC删除 orderCallNoHis:】{}",deleteCountHis);

        //删除前置机数据
        ReturnT returnT = ReturnT.SUCCESS;
        String param = "sh /root/yihaitao/orderCallNo.sh \""+ deleteSQL + ";\"";
        logger.info("【DeleteOrderCallNoHandler SSH:{}】",sshEntity);
        logger.info("【DeleteOrderCallNoHandler 删除前置 orderCallNo Java Shell】 1.=====》 param:{}",param);
        StringBuilder result = sshService.exec(sshEntity,param);
        logger.info("【DeleteOrderCallNoHandler 删除前置 orderCallNo Java Shell】 2.=====》result:\n{}",result);

        param = "sh /root/yihaitao/orderCallNo.sh \""+ deleteSQLHis + ";\"";
        logger.info("【DeleteOrderCallNoHandler 删除前置 orderCallNoHis Java Shell】 1.=====》 param:{}",param);
        result = sshService.exec(sshEntity,param);
        logger.info("【【DeleteOrderCallNoHandler 删除前置 orderCallNoHis Java Shell】 2.=====》result:\n{}",result);

        returnT.setMsg("SUCCESS");
        return returnT;
    }
}
