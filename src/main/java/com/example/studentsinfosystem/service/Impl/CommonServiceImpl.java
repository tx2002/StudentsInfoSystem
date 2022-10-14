package com.example.studentsinfosystem.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.studentsinfosystem.entity.Account;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.mapper.AccountMapper;
import com.example.studentsinfosystem.service.CommonService;
import com.example.studentsinfosystem.utils.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author 梁鑫宇
 * @date 2022/10/11
 */
@Service

public class CommonServiceImpl implements CommonService {

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    JwtToken jwtToken;

    @Override
    public String login(String usename, String password) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("username", usename);
        Account account = accountMapper.selectOne(wrapper);
        if(account == null){
            // 没查到这个用户名
            return "0";
        }
        if(account.getPassword().equals(password)){
            return jwtToken.jwt(account);
        }
        else
            // 密码错误
            return "1";
    }
}
