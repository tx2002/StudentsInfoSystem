package com.example.studentsinfosystem.controller;

import com.example.studentsinfosystem.api.CommonResult;
import com.example.studentsinfosystem.entity.ChooseCourse;
import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.Score;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.service.StudentService;
import com.example.studentsinfosystem.utils.JwtToken;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @PostMapping("/report")
    public CommonResult report(@RequestParam Integer term, @RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            List<Score> list = studentService.report(studentId, term);
            if (!list.isEmpty()) {
                return CommonResult.success(list);
            } else {
                return CommonResult.failed("无成绩信息");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }

    @PostMapping("/chooseCourseList")
    public CommonResult chooseCourseList(@RequestParam Integer term, @RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            // 根据学号查询学生班级
            String className = studentService.getClassNameById(studentId);
            // 根据班级查询该班可选课程
            List<ChooseCourse> courses = studentService.chooseCourseList(className, term);
            // 查询学生已选课程
            List<CourseInfo> courseChoosed = studentService.getCourse(studentId, term);
            Set<String> set = new HashSet<>();
            for (CourseInfo i : courseChoosed) {
                set.add(i.getCourseName());
            }
            // 根据已选课程，对选课列表进行更新
            for (ChooseCourse i : courses) {
                if (set.contains(i.getCourseName())) {
                    i.setClassName("已选");
                } else {
                    i.setClassName("未选");
                }
            }
            if (!courses.isEmpty()) {
                return CommonResult.success(courses);
            } else {
                return CommonResult.failed("无选课信息");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }
}
