package com.example.studentsinfosystem.service;

import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.entity.TeacherInfo;

import java.util.List;

/**
 * @author 梁鑫宇
 * @date 2022/10/11
 */

public interface TeacherService {
    TeacherInfo getinfo(String teacherId);
    Integer addStudentInfo(StudentInfo studentInfo);
    Integer changeStudentInfo(StudentInfo studentInfo);
    Integer deleteStudentInfo(String studentId);
    List<CourseInfo> getCourseStudentInfo(String courseName, String teacherId);
    List<CourseInfo> getCourse(String username);
    Integer changeStudentScore(Integer score,String courseName,String studentId);
}