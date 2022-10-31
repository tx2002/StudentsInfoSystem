package com.example.studentsinfosystem.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.studentsinfosystem.entity.*;
import com.example.studentsinfosystem.mapper.*;
import com.example.studentsinfosystem.service.CommonService;
import com.example.studentsinfosystem.utils.JwtToken;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
     *
     * @author 梁鑫宇
     * @date 2022/10/11
     */
    @Service
    public class CommonServiceImpl implements CommonService {

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    JwtToken jwtToken;

    @Autowired
    StudentInfoMapper studentInfoMapper;

    @Autowired
    CourseInfoMapper courseInfoMapper;

    @Autowired
    ScoreMapper scoreMapper;

    @Autowired
    TeacherInfoMapper teacherInfoMapper;

    @Override
    public String login(String usename, String password) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("username", usename);
        Account account = accountMapper.selectOne(wrapper);
        if (account == null) {
            // 没查到这个用户名
            return "0";
        }
        if (account.getPassword().equals(password)) {
            return jwtToken.jwt(account);
        } else
            // 密码错误
            return "1";
    }

    /**
     * 整体传学生信息
     *
     * @param address
     * @return
     * @throws IOException
     */
    @Override
    public int inputStudentInfo(String address) throws IOException {
        int judge = 0;
        FileInputStream inputStream = new FileInputStream(address);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowTotalCount = sheet.getLastRowNum();
        XSSFRow rowAll = sheet.getRow(0);
        int columnTotalCount = rowAll.getLastCellNum();
        for (int i = 1; i <= rowTotalCount; i++) {
            StudentInfo studentInfo = new StudentInfo();
            Account account = new Account();
            account.setRole(2);
            for (int j = 0; j <= columnTotalCount; j++) {
                switch (j) {
                    case 0:
                        XSSFRow row0 = sheet.getRow(i);
                        row0.getCell(j).setCellType(CellType.STRING);
                        Cell cell0 = row0.getCell(j);
                        account.setUsername(cell0.getStringCellValue());
                        account.setPassword(cell0.getStringCellValue().substring(3, 9));
                        studentInfo.setStudentId(cell0.getStringCellValue());
                    case 1:
                        XSSFRow row1 = sheet.getRow(i);
                        row1.getCell(j).setCellType(CellType.STRING);
                        Cell cell1 = row1.getCell(j);
                        studentInfo.setStudentName(cell1.getStringCellValue());
                    case 2:
                        XSSFRow row2 = sheet.getRow(i);
                        row2.getCell(j).setCellType(CellType.STRING);
                        Cell cell2 = row2.getCell(j);
                        studentInfo.setClassName(cell2.getStringCellValue());
                }
            }

            // 插入学生类
            // 插入用户类
            judge = studentInfoMapper.insert(studentInfo);
            accountMapper.insert(account);
        }
        return judge;
    }

    /**
     * 上传课程信息
     *
     * @param address
     * @return Integer
     * @throws IOException
     */
    @Override
    public int inputCourseInfo(String address) throws IOException {
        int judge = 0;
        FileInputStream inputStream = new FileInputStream(address);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowTotalCount = sheet.getLastRowNum();
        XSSFRow rowAll = sheet.getRow(0);
        int columnTotalCount = rowAll.getLastCellNum();
        for (int i = 1; i <= rowTotalCount; i++) {
            CourseInfo courseInfo = new CourseInfo();
            for (int j = 0; j <= columnTotalCount; j++) {
                switch (j) {
                    case 0:
                        XSSFRow row0 = sheet.getRow(i);
                        row0.getCell(j).setCellType(CellType.STRING);
                        Cell cell0 = row0.getCell(j);
                        courseInfo.setCourseName(cell0.getStringCellValue());
                    case 1:
                        XSSFRow row1 = sheet.getRow(i);
                        row1.getCell(j).setCellType(CellType.STRING);
                        Cell cell1 = row1.getCell(j);
                        courseInfo.setTeacher(cell1.getStringCellValue());
                    case 2:
                        XSSFRow row2 = sheet.getRow(i);
                        row2.getCell(j).setCellType(CellType.STRING);
                        Cell cell2 = row2.getCell(j);
                        courseInfo.setTeacherId(cell2.getStringCellValue());
                    case 3:
                        XSSFRow row3 = sheet.getRow(i);
                        row3.getCell(j).setCellType(CellType.STRING);
                        Cell cell3 = row3.getCell(j);
                        courseInfo.setPoint(cell3.getStringCellValue());
                    case 4:
                        XSSFRow row4 = sheet.getRow(i);
                        row4.getCell(j).setCellType(CellType.STRING);
                        Cell cell4 = row4.getCell(j);
                        courseInfo.setStudentId(cell4.getStringCellValue());
                    case 5:
                        XSSFRow row5 = sheet.getRow(i);
                        row5.getCell(j).setCellType(CellType.NUMERIC);
                        Cell cell5 = row5.getCell(j);
                        courseInfo.setTerm((int) cell5.getNumericCellValue());
                }
            }
            judge = courseInfoMapper.insert(courseInfo);
        }
        return judge;
    }

    /**
     * 批量导入老师信息
     *
     * @param address
     * @return
     * @throws IOException
     */
    @Override
    public int inputTeacherInfo(String address) throws IOException {
        int judge = 0;
        FileInputStream inputStream = new FileInputStream(address);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowTotalCount = sheet.getLastRowNum();
        XSSFRow rowAll = sheet.getRow(0);
        int columnTotalCount = rowAll.getLastCellNum();
        for (int i = 1; i <= rowTotalCount; i++) {
            TeacherInfo teacherInfo = new TeacherInfo();
            Account account = new Account();
            account.setRole(2);
            for (int j = 0; j <= columnTotalCount; j++) {
                switch (j) {
                    case 0:
                        XSSFRow row0 = sheet.getRow(i);
                        row0.getCell(j).setCellType(CellType.STRING);
                        Cell cell0 = row0.getCell(j);
                        account.setUsername(cell0.getStringCellValue());
                        account.setPassword(cell0.getStringCellValue().substring(1, 7));
                        teacherInfo.setTeacherId(cell0.getStringCellValue());
                    case 1:
                        XSSFRow row1 = sheet.getRow(i);
                        row1.getCell(j).setCellType(CellType.STRING);
                        Cell cell1 = row1.getCell(j);
                        teacherInfo.setTeacherName(cell1.getStringCellValue());
                }

                // 插入学生
                // 插入用户
                judge = teacherInfoMapper.insert(teacherInfo);
                accountMapper.insert(account);
            }
        }
        return judge;
    }
}

