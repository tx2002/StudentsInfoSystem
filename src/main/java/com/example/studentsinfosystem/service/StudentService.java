package com.example.studentsinfosystem.service;

import com.example.studentsinfosystem.entity.ChooseCourse;
import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.Score;
import com.example.studentsinfosystem.entity.StudentInfo;

import java.util.List;

/**
 *
 * @author TX
 * @date 2022/10/8 9:07
 */

public interface StudentService {
    StudentInfo getinfo(String studentId);

    List<CourseInfo> getCourse(String studentId);

    List<CourseInfo> getCourse(String studentId, Integer term);

    List<Score> report(String studentId, Integer term);
    
    String getClassNameById(String studentId);

    List<ChooseCourse> chooseCourseList(String className, Integer term);
}
