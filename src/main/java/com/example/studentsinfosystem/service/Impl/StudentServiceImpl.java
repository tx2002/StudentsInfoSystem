package com.example.studentsinfosystem.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.studentsinfosystem.entity.*;
import com.example.studentsinfosystem.mapper.*;
import com.example.studentsinfosystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学生相关Service层
 * @author TX
 * @date 2022/10/8 9:09
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentInfoMapper studentInfoMapper;
    @Autowired
    CourseInfoMapper courseInfoMapper;
    @Autowired
    ScoreMapper scoreMapper;
    @Autowired
    ChooseCourseMapper chooseCourseMapper;
    @Autowired
    TeacherInfoMapper teacherInfoMapper;
    @Autowired
    AccountMapper accountMapper;

    @Override
    public StudentInfo getinfo(String studentId) {
        QueryWrapper<StudentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId);
        StudentInfo studentInfo = studentInfoMapper.selectOne(wrapper);
        return studentInfo;
    }

    @Override
    public List<CourseInfo> getCourse(String studentId) {
        QueryWrapper<CourseInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId);
        List<CourseInfo> list = courseInfoMapper.selectList(wrapper);
        return list;
    }

    @Override
    public List<CourseInfo> getCourse(String studentId, Integer term) {
        QueryWrapper<CourseInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId)
                .eq("term", term);
        List<CourseInfo> list = courseInfoMapper.selectList(wrapper);
        return list;
    }

    @Override
    public List<Score> report(String studentId, Integer term) {
        QueryWrapper<Score> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId)
                .eq("term", term);
        List<Score> list = scoreMapper.selectList(wrapper);
        return list;
    }

    @Override
    public String getClassNameById(String studentId) {
        QueryWrapper<StudentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId);
        String className = studentInfoMapper.selectOne(wrapper).getClassName();
        return className;
    }

    /**
     * 根据班级查询可选课程
     * @param className
     * @param term
     * @return
     */
    @Override
    public List<ChooseCourse> chooseCourseList(String className, Integer term) {
        QueryWrapper<ChooseCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("class_name", className)
                .eq("term", term);
        List<ChooseCourse> courses = chooseCourseMapper.selectList(wrapper);
        return courses;
    }

    @Override
    public String getTeacherIdByName(String teacher) {
        QueryWrapper<TeacherInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_name", teacher);
        String teacherId = teacherInfoMapper.selectOne(wrapper).getTeacherId();
        return teacherId;
    }

    @Override
    public Integer insert(CourseInfo newCourse) {
        int insert = courseInfoMapper.insert(newCourse);
        return insert;
    }

    @Override
    public int deleteCourse(String studentId, String courseName) {
        QueryWrapper<CourseInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId)
                        .eq("course_name", courseName);
        int delete = courseInfoMapper.delete(wrapper);
        return delete;
    }

    @Override
    public String changePassword(String studentId, String oldPassword, String newPassword) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("username", studentId);
        String password = accountMapper.selectOne(wrapper).getPassword();
        if (newPassword.isEmpty()) {
            return "新密码不能为空！";
        }
        if (oldPassword.equals(password)) {
            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper
                    .eq("username",studentId)
                    .set("password",newPassword);
            accountMapper.update(null,updateWrapper);
            return "修改密码成功！";
        } else {
            return "旧密码错误！";
        }
    }
}
