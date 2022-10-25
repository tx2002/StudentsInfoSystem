package com.example.studentsinfosystem.controller;

import com.example.studentsinfosystem.api.CommonResult;
import com.example.studentsinfosystem.service.CommonService;
import com.example.studentsinfosystem.utils.JwtToken;
import com.example.studentsinfosystem.utils.MultipartFileToFile;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    /**
     * 通过文件导入学生信息
     * 有个小问题，如果老师的文件格式不正确，将有问题
     * @param token
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/inputstudentinfo")
    public CommonResult inputStudentInfo(@RequestHeader String token,
                                         @RequestBody MultipartFile file) throws Exception {
        Claims claims = jwtToken.getClaimByToken(token);
        if(claims.get("role").equals(1)){
            File files = MultipartFileToFile.multipartFileToFile(file);
            String address = files.getAbsolutePath();
            int judge = commonService.inputStudentInfo(address);
            if(judge==1)
                return CommonResult.success("导入成功");
            else
                return CommonResult.failed("导入失败");
        }
        else
            return CommonResult.failed("无权限");
    }

}