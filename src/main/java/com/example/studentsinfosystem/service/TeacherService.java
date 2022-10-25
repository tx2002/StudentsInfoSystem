package com.example.studentsinfosystem.service;

import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.Score;
import com.example.studentsinfosystem.entity.StudentInfo;

import java.util.List;

/**
 * @author 梁鑫宇
 * @date 2022/10/11
 */

public interface TeacherService {
    StudentInfo getStudentinfo(String studentId);
    Integer addStudentInfo(StudentInfo studentInfo);
    Integer changeStudentInfo(StudentInfo studentInfo);
    Integer deleteStudentInfo(String studentId);
    List<CourseInfo> getCourseStudentInfo(String courseName, String teacherId);
    List<CourseInfo> getCourse(String username);
}