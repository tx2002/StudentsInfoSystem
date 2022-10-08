package com.example.studentsinfosystem.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.mapper.CourseInfoMapper;
import com.example.studentsinfosystem.mapper.StudentInfoMapper;
import com.example.studentsinfosystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author TX
 * @date 2022/10/8 9:09
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentInfoMapper studentInfoMapper;
    @Autowired
    CourseInfoMapper courseInfoMapper;

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
}
