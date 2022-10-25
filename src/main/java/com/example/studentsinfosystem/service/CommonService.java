package com.example.studentsinfosystem.service;



import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.Score;
import com.example.studentsinfosystem.entity.StudentInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author 梁鑫宇
 * @date 2022/10/11
 */

public interface CommonService {
    // 登录
    String login(String usename, String password);

    // 上传学生信息
    int inputStudentInfo(String address) throws IOException;

    // 上传课程信息
    int inputCourseInfo(String address) throws  IOException;
}