package com.example.studentsinfosystem.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.Score;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.entity.TeacherInfo;
import com.example.studentsinfosystem.mapper.*;
import com.example.studentsinfosystem.service.TeacherService;
import com.example.studentsinfosystem.utils.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 梁鑫宇
 * @date 2022/10/11
 */

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    TeacherInfoMapper teacherInfoMapper;
    @Autowired
    StudentInfoMapper studentInfoMapper;
    @Autowired
    ScoreMapper scoreMapper;
    @Autowired
    CourseInfoMapper courseInfoMapper;


    @Override
    public StudentInfo getStudentinfo(String studentId) {
        QueryWrapper<StudentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId);
        StudentInfo studentInfo = studentInfoMapper.selectOne(wrapper);
        return studentInfo;
    }


    @Override
    public StudentInfo changeStudentinfo(String token, StudentInfo studentInfo) {
        return null;
    }

    @Override
    public String inputStudent(String token, StudentInfo studentInfo) {
        return null;
    }

    @Override
    public String deleteStudent(String token, StudentInfo studentInfo) {
        return null;
    }

    @Override
    public List<StudentInfo> getAllStudentInfo(String token, String courseName) {
        return null;
    }

    @Override
    public List<Score> getAllStudentScore(String token, String studentId) {
        return null;
    }

    @Override
    public List<CourseInfo> getAllCourseInfo(String token, String username) {
        return null;
    }
}
