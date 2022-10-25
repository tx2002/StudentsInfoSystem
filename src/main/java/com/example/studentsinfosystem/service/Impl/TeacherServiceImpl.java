package com.example.studentsinfosystem.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.Score;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.mapper.CourseInfoMapper;
import com.example.studentsinfosystem.mapper.ScoreMapper;
import com.example.studentsinfosystem.mapper.StudentInfoMapper;
import com.example.studentsinfosystem.mapper.TeacherInfoMapper;
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
        int judge = studentInfoMapper.insert(studentInfo);
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
     * @param studentInfo
     * @return Integer
     */
    @Override
    public Integer deleteStudentInfo(StudentInfo studentInfo) {
        QueryWrapper<StudentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentInfo.getStudentId());
        int judge = studentInfoMapper.delete(wrapper);
        return judge;
    }




    /**
     * 查询班级信息
     * @param courseName
     * @return List<StudentInfo>
     */
    @Override
    public List<StudentInfo> getAllStudentInfo(String courseName) {
        QueryWrapper<StudentInfo> wrapper = new QueryWrapper<>();
        return null;

    }

    @Override
    public List<Score> getAllStudentScore(String studentId) {
        return null;
    }

    /**
     * 查询老师自己的课程信息
     * @param username
     * @return List<CourseInfo>
     */
    @Override
    public List<CourseInfo> getAllCourseInfo( String username) {
        return null;
    }

}

