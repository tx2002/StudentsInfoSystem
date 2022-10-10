package com.example.studentsinfosystem.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.studentsinfosystem.entity.ChooseCourse;
import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.Score;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.mapper.ChooseCourseMapper;
import com.example.studentsinfosystem.mapper.CourseInfoMapper;
import com.example.studentsinfosystem.mapper.ScoreMapper;
import com.example.studentsinfosystem.mapper.StudentInfoMapper;
import com.example.studentsinfosystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
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
     *
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
}
