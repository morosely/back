package com.efuture.executor.jobhandler;

import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.parser.Token;
import com.efuture.component.SSHService;
import com.efuture.utils.SSHEntity;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@JobHandler("shellHandler")
@Component
public class ShellHandler extends IJobHandler {
    private final Logger logger = LoggerFactory.getLogger(ShellHandler.class);
    @Autowired
    private SSHService sshService;

    @Resource(name = "ssh")
    private SSHEntity sshEntity;

    @Override
    public ReturnT<String> execute(String sql) throws Exception {
        ReturnT returnT = ReturnT.SUCCESS;
        //1.判断sql是否是查询语句
        if(!isSelect(sql)){
            returnT.setContent("【不是查询语句】："+sql);
            returnT.setMsg("FAIL");
            return returnT;
        }else{
            //2.执行查询sql
            String param = "sh /root/yihaitao/execFront.sh \""+ sql + ";\"";
            logger.info("【Java Shell】 1.=====》 param:{}",param);
            StringBuilder result = sshService.exec(sshEntity,param);
            logger.info("【Java Shell】 2.=====》result:\n{}",result);
            returnT.setContent(result);
            returnT.setMsg("SUCCESS");
            return returnT;
        }
    }

    public static boolean isSelect(String sql) {
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, "mysql");
        return Token.SELECT.equals(parser.getExprParser().getLexer().token());
    }
}
