package com.efuture.component;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.efuture.utils.SSHEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class SSHService {
    private final static Logger logger = LoggerFactory.getLogger(SSHService.class);
    private Connection connect;

    public StringBuilder exec(SSHEntity sshEntity, String shell) throws IOException {
        InputStream inputStream = null;
        StringBuilder result = new StringBuilder();
        StringBuilder errResult = new StringBuilder();
        try {
            // 认证登录信息
            logger.info("【Java Shell】 1.1 sshEntity =====> {}",sshEntity);
            Boolean sshLogin = this.login(sshEntity);
            if (sshLogin) {
                logger.info("【Java Shell】 1.2 ssh login =====> {}",sshLogin);
                // 登陆成功则打开一个会话
                Session session = connect.openSession();
                logger.info("【Java Shell】 1.3 exec shell =====> {}",shell);
                session.execCommand(shell);
                inputStream = new StreamGobbler(session.getStdout());;
                result.append(this.processStdout(inputStream));
                InputStream errorStream = new StreamGobbler(session.getStderr());
                errResult.append(this.processStdout(errorStream));
                //session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS , 30000);
                // 等待，除非1.连接关闭；2.输出数据传送完毕；3.进程状态为退出；4.超时
                //调用这个方法在执行linux命令时，会避免环境变量读取不全的问题，这里有许多标识可以用，比如当exit命令执行后或者超过了timeout时间,则session关闭
                session.waitForCondition(ChannelCondition.EXIT_STATUS, 10000);//在调用getExitStatus时，要先调用WaitForCondition方法
                logger.info("【Java Shell】 1.4 session.getExitStatus(): " + session.getExitStatus());
                if(errResult!=null && errResult.toString().trim().length() > 0) {
                    logger.info("【Java Shell】 1.5 errResult: " + errResult);
                }

            }
        } finally {
            if(null != connect){
                connect.close();
            }
            if (null != inputStream) {
                inputStream.close();
            }
        }
        return result;
    }

    private boolean login(SSHEntity sshEntity) {
        connect = new Connection(sshEntity.getHost(), sshEntity.getPort());
        try {
            connect.connect();
            return connect.authenticateWithPassword(sshEntity.getUser(), sshEntity.getPassword());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private StringBuilder processStdout(InputStream in) {
        byte[] buf = new byte[1024];
        StringBuilder builder = new StringBuilder();
        try {
            int length;
            while ((length = in.read(buf)) != -1) {
                builder.append(new String(buf, 0, length));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }
}
