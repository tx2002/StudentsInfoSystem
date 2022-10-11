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
    /**
     * 根据学号获取学生信息
     * @param studentId
     * @return
     */
    StudentInfo getinfo(String studentId);

    /**
     * 根据学号获取学生课程
     * @param studentId
     * @return
     */
    List<CourseInfo> getCourse(String studentId);

    /**
     * 根据学号和学期获取学生课程
     * @param studentId
     * @param term
     * @return
     */
    List<CourseInfo> getCourse(String studentId, Integer term);

    /**
     * 成绩单
     * @param studentId
     * @param term
     * @return
     */
    List<Score> report(String studentId, Integer term);

    /**
     * 根据学号获取班级
     * @param studentId
     * @return
     */
    String getClassNameById(String studentId);

    /**
     * 根据班级获取选课列表
     * @param className
     * @param term
     * @return
     */
    List<ChooseCourse> chooseCourseList(String className, Integer term);

    /**
     * 根据工号获取老师姓名
     * @param teacher
     * @return
     */
    String getTeacherIdByName(String teacher);

    /**
     * 新增选课
     * @param newCourse
     * @return
     */
    Integer insert(CourseInfo newCourse);

    /**
     * 退选课程
     * @param studentId
     * @param courseName
     * @return
     */
    int deleteCourse(String studentId, String courseName);
}
