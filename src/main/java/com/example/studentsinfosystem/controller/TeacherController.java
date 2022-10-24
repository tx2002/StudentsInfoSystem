package com.example.studentsinfosystem.controller;

import com.example.studentsinfosystem.api.CommonResult;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.service.TeacherService;
import com.example.studentsinfosystem.utils.JwtToken;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 梁鑫宇
 * @data 2022/10/11
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    JwtToken jwtToken;

    @Autowired
    TeacherService teacherService;

    /**
     * 显示学生信息
     * @param token
     * @return StudentInfo
     */
    @GetMapping("/getstudentinfo")
    public CommonResult getinfo(@RequestHeader String token){
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(1) || claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            StudentInfo getinfo = teacherService.getStudentinfo(studentId);
            if (getinfo != null) {
                return CommonResult.success(getinfo);
            } else {
                return CommonResult.failed("未查询到该学生");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 新增学生信息
     * @param token
     * @param studentInfo
     * @return String成功与否
     */
    @PostMapping("/entryInformation")
    public CommonResult entryinfo(@RequestHeader String token,
                                  @RequestParam StudentInfo studentInfo) {
        Claims claims = jwtToken.getClaimByToken(token);
        if(claims.get("role").equals(1)){
            // 判断是否已有该学生信息

            String studentId = (String) claims.get("username");
            StudentInfo getinfo = teacherService.getStudentinfo(studentId);
            if (getinfo != null) {
                return CommonResult.failed("该生信息已存在");
            } else if(teacherService.inputStudent(studentInfo).equals("0"))
                return CommonResult.success("增加成功");
            else
                return CommonResult.failed("增加失败");
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 改变学生信息
     * @param token
     * @param studentInfo
     * @return StudentInfo
     */
    @PostMapping("/change")
    public CommonResult changeinfo(@RequestHeader String token,
                                   @RequestParam StudentInfo studentInfo){
        Claims claims = jwtToken.getClaimByToken(token);
        if(claims.get("role").equals(1)){
            StudentInfo show = teacherService.changeStudentinfo(studentInfo);
            return CommonResult.success(show);
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 删除学生信息
     * @param token
     * @param studentInfo
     * @return String 成功与否
     */
    @PostMapping("/delete")
    public CommonResult deleteStudent(@RequestHeader String token,
                                      @RequestParam StudentInfo studentInfo){
        Claims claims = jwtToken.getClaimByToken(token);
        if(claims.get("role").equals(1)){
            String show = teacherService.deleteStudent(studentInfo);
            if(show != null) {
                return CommonResult.success(show);
            }
            else
                return CommonResult.failed("不存在该生信息");
        } else {
            return CommonResult.failed("无权限");
        }
    }

    @GetMapping("getstudentclss")
    public CommonResult getStudentClass(@RequestHeader String token,
                                        @RequestParam String courseName){
        Claims claims = jwtToken.getClaimByToken(token);
        if(claims.get("role").equals(1)){
            return CommonResult.success("null");
        }
        else {
            return CommonResult.failed("无权限");
        }
    }
}