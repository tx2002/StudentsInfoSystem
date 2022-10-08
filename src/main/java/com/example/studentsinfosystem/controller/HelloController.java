package com.example.studentsinfosystem.controller;

import com.example.studentsinfosystem.api.CommonResult;
import com.example.studentsinfosystem.entity.Account;
import com.example.studentsinfosystem.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TX
 * @date 2022/10/5 19:49
 */

@RestController

public class HelloController {
    @Autowired
    AccountMapper accountMapper;

    @PostMapping("/hello")
    public CommonResult hello() {
        Account account = new Account();
        account.setUsername("B20030530");
        account.setPassword("123456");
        account.setRole(1);
        int insert = accountMapper.insert(account);
        return CommonResult.success(insert);
    }
}
