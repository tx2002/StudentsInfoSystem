package com.example.studentsinfosystem.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.studentsinfosystem.entity.*;
import com.example.studentsinfosystem.mapper.*;
import com.example.studentsinfosystem.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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
    @Autowired
    AccountMapper accountMapper;

    /**
     * 查询学生信息
     * @param studentId
     * @return studentInfo
     */
    @Override
    public StudentInfo getStudentinfo(String studentId) {
        QueryWrapper<StudentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId);
        return studentInfoMapper.selectOne(wrapper);
    }

    /**
     * 增加学生信息
     * @param studentInfo
     * @return Integer
     */
    @Override
    public Integer addStudentInfo(StudentInfo studentInfo) {
        // 新增学生信息
        int judge = studentInfoMapper.insert(studentInfo);
        // 新增账户
        Account account = new Account();
        account.setRole(1);
        account.setUsername(studentInfo.getStudentId());
        account.setPassword(studentInfo.getStudentId());
        accountMapper.insert(account);
        return judge;
    }

    /**
     * 修改学生信息
     * @param studentInfo
     * @return Integer
     */
    @Override
    public Integer changeStudentInfo(StudentInfo studentInfo) {
        QueryWrapper<StudentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentInfo.getStudentId());
        int judge = studentInfoMapper.update(studentInfo,wrapper);
        return judge;
    }

    /**
     * 删除学生信息
     * @param studentId
     * @return Integer
     */
    @Override
    public Integer deleteStudentInfo(String studentId) {
        QueryWrapper<StudentInfo> wrapper1 = new QueryWrapper<>();
        QueryWrapper<Account> wrapper2 = new QueryWrapper<>();
        wrapper1.eq("student_id", studentId);
        wrapper2.eq("username", studentId);
        int judge = studentInfoMapper.delete(wrapper1);
        // 删除账户信息
        accountMapper.delete(wrapper2);
        return judge;
    }


    /**
     * 查询所教某一课程中的学生信息
     * @param courseName
     * @param teacherId
     * @return List<StudentInfo>
     */
    @Override
    public List<CourseInfo> getCourseStudentInfo(String courseName, String teacherId) {
        QueryWrapper<CourseInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_name", courseName)
                .eq("teacher_id", teacherId);
        return courseInfoMapper.selectList(wrapper);
    }

    /**
     * 查询老师自己的课程信息
     * @param username
     * @param
     * @return List<CourseInfo>
     */
    @Override
    public List<CourseInfo> getCourse(String username) {
        QueryWrapper<CourseInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", username);
        return courseInfoMapper.selectList(wrapper);
    }
}

