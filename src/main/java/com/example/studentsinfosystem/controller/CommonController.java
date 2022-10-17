package com.example.studentsinfosystem.controller;

import com.example.studentsinfosystem.api.CommonResult;
import com.example.studentsinfosystem.entity.Account;
import com.example.studentsinfosystem.service.CommonService;
import com.example.studentsinfosystem.utils.JwtToken;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 梁鑫宇
 * @date 2022/10/11
 */
@RestController
public class CommonController {
    @Autowired
    JwtToken jwtToken = new JwtToken();

    @Autowired
    CommonService commonService;

    @PostMapping("/login")
    public CommonResult login(@RequestParam String username,@RequestParam String password){
        String result = commonService.login(username,password);
        if(result.equals("0"))
            return CommonResult.failed("用户不存在");
        if(result.equals("1"))
            return  CommonResult.failed("密码错误");
        else {
            Claims claimByToken = jwtToken.getClaimByToken(result);
            String role = claimByToken.get("role").toString();
            Map<String, String> map = new HashMap<>();
            map.put("token", result);
            map.put("role", role);
            return CommonResult.success(map);
        }

    }


}