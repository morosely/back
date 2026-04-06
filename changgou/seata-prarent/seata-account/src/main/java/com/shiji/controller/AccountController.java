package com.shiji.controller;

import com.shiji.service.AccountService;
import org.springframework.beans.factory.  annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;


    @PostMapping(value = "/account", produces = "application/json")
    public String account(String userId, int money) {
       return accountService.account(userId,money);
    }
}
