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
 * 学生功能相关的接口
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

    /**
     * 学生查询自己的全部课程
     * @param token
     * @return
     */
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

    /**
     * 学生查询成绩单
     * @param term 学期数
     * @param token
     * @return
     */
    @GetMapping("/report")
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

    /**
     * 查询学生可选课的列表
     * @param term 学期数
     * @param token
     * @return
     */
    @GetMapping("/chooseCourseList")
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

    /**
     * 学生新增选课
     * @param courseName
     * @param term
     * @param token
     * @return
     */
    @PostMapping("/addCourse")
    public CommonResult addCourse(@RequestParam String courseName, @RequestParam Integer term, @RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            // 根据学号查询学生班级
            String className = studentService.getClassNameById(studentId);
            // 根据班级查询该班可选课程
            List<ChooseCourse> courses = studentService.chooseCourseList(className, term);
            ChooseCourse addCourse = new ChooseCourse();
            for (ChooseCourse i : courses) {
                if (i.getCourseName().equals(courseName)) {
                    addCourse = i;
                }
            }
            // 在学生课程中新增
            CourseInfo newCourse = new CourseInfo();
            newCourse.setCourseName(addCourse.getCourseName());
            newCourse.setStudentId(studentId);
            newCourse.setPoint(addCourse.getPoint());
            newCourse.setTeacher(addCourse.getTeacher());
            newCourse.setTeacherId(studentService.getTeacherIdByName(addCourse.getTeacher()));
            newCourse.setTerm(addCourse.getTerm());
            int insert = studentService.insert(newCourse);
            if (insert == 1) {
                return CommonResult.success("选课成功！");
            } else {
                return CommonResult.failed("选课失败!");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 学生退选课
     * @param courseName
     * @param token
     * @return
     */
    @PostMapping("/deleteCourse")
    public CommonResult deleteCourse(@RequestParam String courseName, @RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            int delete = studentService.deleteCourse(studentId, courseName);
            if (delete == 1) {
                return CommonResult.success("退选成功！");
            } else {
                return CommonResult.failed("退选失败!");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @param token
     * @return
     */
    @PostMapping("/changePassword")
    public CommonResult changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(0) || claims.get("role").equals(1) || claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            String res = studentService.changePassword(studentId, oldPassword, newPassword);
            if (res.equals("修改密码成功！")) {
                return CommonResult.success(res);
            } else {
                return CommonResult.failed(res);
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }
}
