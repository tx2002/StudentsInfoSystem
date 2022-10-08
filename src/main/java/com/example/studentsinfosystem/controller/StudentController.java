package com.example.studentsinfosystem.controller;

import com.example.studentsinfosystem.api.CommonResult;
import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.service.StudentService;
import com.example.studentsinfosystem.utils.JwtToken;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author TX
 * @date 2022/10/8 8:55
 */
@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    JwtToken jwtToken = new JwtToken();

    @Autowired
    StudentService studentService;

    /**
     * 学生查询自己个人信息
     * @param token
     * @return
     */
    @GetMapping("/getinfo")
    public CommonResult getinfo(@RequestHeader String token){
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(1) || claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            StudentInfo getinfo = studentService.getinfo(studentId);
            if (getinfo != null) {
                return CommonResult.success(getinfo);
            } else {
                return CommonResult.failed("未查询到该学生");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }

    @GetMapping("/getCourse")
    public CommonResult getCourse(@RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            List<CourseInfo> list = studentService.getCourse(studentId);
            if (!list.isEmpty()) {
                return CommonResult.success(list);
            } else {
                return CommonResult.failed("无课程信息");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }
}
