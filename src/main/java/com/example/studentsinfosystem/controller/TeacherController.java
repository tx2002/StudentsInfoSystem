package com.example.studentsinfosystem.controller;

import com.example.studentsinfosystem.api.CommonResult;
import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.Score;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.entity.TeacherInfo;
import com.example.studentsinfosystem.service.StudentService;
import com.example.studentsinfosystem.service.TeacherService;
import com.example.studentsinfosystem.utils.JwtToken;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    @Autowired
    StudentService studentService;

    /**
     * 显示学生信息
     * @param token
     * @return TeacherInfo
     */
    @GetMapping("/getinfo")
    public CommonResult getinfo(@RequestHeader String token){
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(1)){
            String teacherId = (String) claims.get("username");
            TeacherInfo getinfo = teacherService.getinfo(teacherId);
            if (getinfo != null) {
                return CommonResult.success(getinfo);
            } else {
                return CommonResult.failed("未查询到老师信息");
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
    @PostMapping("/addstudentinfo")
    public CommonResult addStudentInfo(@RequestHeader String token,
                                       @RequestBody StudentInfo studentInfo) {
        Claims claims = jwtToken.getClaimByToken(token);
        if(claims.get("role").equals(1)){
            // 判断是否已有该学生信息
            String studentId = studentInfo.getStudentId();
            StudentInfo getinfo = studentService.getinfo(studentId);
            if (getinfo != null) {
                return CommonResult.failed("该生信息已存在");
            } else if(teacherService.addStudentInfo(studentInfo)==1)
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
    @PostMapping("/changestudentinfo")
    public CommonResult changeStudentInfo(@RequestHeader String token,
                                          @RequestBody StudentInfo studentInfo){
        Claims claims = jwtToken.getClaimByToken(token);
        if(claims.get("role").equals(1)){
            if(teacherService.changeStudentInfo(studentInfo)==1)
                return CommonResult.success("修改成功");
            else
                return  CommonResult.failed("修改失败");
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 删除学生信息
     * @param token
     * @param studentId
     * @return String 成功与否
     */
    @PostMapping("/deletestudentinfo")
    public CommonResult deleteStudentInfo(@RequestHeader String token,
                                          @RequestParam String studentId){
        Claims claims = jwtToken.getClaimByToken(token);
        if(claims.get("role").equals(1)){
            if(teacherService.deleteStudentInfo(studentId)==1)
                return CommonResult.success("删除成功");
            else
                return CommonResult.failed("删除失败");
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 查询所教的课程信息
     * @param token
     * @return CourseInfo
     */
    @GetMapping("/getcourse")
    public  CommonResult getCourse(@RequestHeader String token){
        Claims claims = jwtToken.getClaimByToken(token);
        if(claims.get("role").equals(1)){
            Set<String> courseInfos = teacherService.getCourse((String) claims.get("username"));
            return CommonResult.success(courseInfos);
        }
        else
            return CommonResult.failed("无权限");
    }

    /**
     * 查询某一课程中的学生信息
     * @param token
     * @param courseName
     * @return
     */
    @GetMapping("/getcoursestudentinfo")
    public CommonResult getCourseStudentInfo(@RequestHeader String token,
                                             @RequestParam String courseName){
        Claims claims = jwtToken.getClaimByToken(token);
        if(claims.get("role").equals(1)){
            List<CourseInfo> coursestudentInfos = teacherService.getCourseStudentInfo(courseName, (String) claims.get("username"));
            return CommonResult.success(coursestudentInfos);
        }
        else
            return CommonResult.failed("无权限");
    }

    /**
     * 修改学生成绩
     * @param token
     * @param score
     * @return String，成功与否的信息
     */
    @PostMapping("/changescore")
    public CommonResult changeScore(@RequestHeader String token,
                                    @RequestParam Integer score,
                                    @RequestParam String courseName,
                                    @RequestParam String studentId){
        Claims claims = jwtToken.getClaimByToken(token);
        if(claims.get("role").equals(1)){
            if (teacherService.changeStudentScore(score,courseName,studentId) == 1)
                return CommonResult.success("修改成功");
            else
                return CommonResult.failed("修改失败");
        }
        else
            return CommonResult.failed("无权限");
    }
}