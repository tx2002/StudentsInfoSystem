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

    StudentInfo changeStudentinfo(StudentInfo studentInfo);

    String  inputStudent(StudentInfo studentInfo);

    String deleteStudent(StudentInfo studentInfo);

    List<StudentInfo> getAllStudentInfo(String token, String courseName);

    List<Score> getAllStudentScore(String token, String studentId);

    List<CourseInfo> getAllCourseInfo(String token, String username);
}