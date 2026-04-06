package com.shiji.service;

import com.shiji.dao.AccountDao;
import com.shiji.model.Account;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AccountService {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";

    @Autowired
    private AccountDao accountDao;

    @Transactional
    public String account(String userId, int money) {
        log.info("---------->>> Account Service ... xid: " + RootContext.getXID());
        Account account = new Account();
        account.setUseId(userId);
        account = accountDao.selectOne(account);
        account.setMoney(account.getMoney() - money);
        int result = accountDao.updateByPrimaryKeySelective(account);
        log.info("----------》》》 用户账户金额变动受影响函数："+result);
        log.info("---------->>> Account Service End ... ");
        if (result == 1) {
            return SUCCESS;
        }
        return FAIL;
    }
}
