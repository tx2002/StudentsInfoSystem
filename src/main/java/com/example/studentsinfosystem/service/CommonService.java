package com.example.studentsinfosystem.service;



import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;

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

    // 上传老师信息
    int inputTeacherInfo(String address) throws  IOException;
}