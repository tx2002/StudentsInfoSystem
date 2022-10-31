package com.example.studentsinfosystem.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.studentsinfosystem.entity.Account;
import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.Score;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.mapper.AccountMapper;
import com.example.studentsinfosystem.mapper.CourseInfoMapper;
import com.example.studentsinfosystem.mapper.ScoreMapper;
import com.example.studentsinfosystem.mapper.StudentInfoMapper;
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


        @Override
        public String login(String usename, String password) {
            QueryWrapper<Account> wrapper = new QueryWrapper<>();
            wrapper.eq("username", usename);
            Account account = accountMapper.selectOne(wrapper);
            if(account == null){
                // 没查到这个用户名
                return "0";
            }
            if(account.getPassword().equals(password)){
                return jwtToken.jwt(account);
            }
            else
                // 密码错误
                return "1";
        }

    /**
     * 整体传文件
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
        for (int i=1; i<=rowTotalCount; i++){
            StudentInfo studentInfo = new StudentInfo();
            Account account = new Account();
            account.setRole(2);
            for (int j=0; j<=columnTotalCount; j++){
                switch (j){
                    case 0:
                        XSSFRow row0 = sheet.getRow(i);
                        row0.getCell(j).setCellType(CellType.STRING);
                        Cell cell0 = row0.getCell(j);
                        account.setUsername(cell0.getStringCellValue());
                        account.setPassword(cell0.getStringCellValue().substring(3,9));
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

            judge = studentInfoMapper.insert(studentInfo);
            accountMapper.insert(account);
        }
        return judge;
    }

    /**
     * 上传课程信息
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
        for (int i=1; i<=rowTotalCount; i++){
            CourseInfo courseInfo = new CourseInfo();
            for(int j=0; j<=columnTotalCount; j++){
                switch (j){
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
                        courseInfo.setTerm((int)cell5.getNumericCellValue());
                }
            }
            judge = courseInfoMapper.insert(courseInfo);
        }
        return judge;
    }

    @Override
    public String outputStudentScore(String courseName ) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);
        XSSFSheet sheet = workbook.createSheet("Sheet1");
        Row row0 = sheet.createRow(0);
        Cell cell0 = row0.createCell(0);
        cell0.setCellValue("学号");
        cell0.setCellStyle(cellStyle);
        Cell cell1 = row0.createCell(1);
        cell1.setCellValue("姓名");
        Cell cell2 = row0.createCell(2);
        cell2.setCellValue("课程名");
        Cell cell3 = row0.createCell(3);
        cell3.setCellValue("开课学期");
        Cell cell4 = row0.createCell(4);
        cell4.setCellValue("分数");
        Cell cell5 = row0.createCell(5);
        cell5.setCellValue("学分");

        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(now);
        String str = "\\home\\Android\\excelout\\";
        String address = str+courseName+"学生成绩信息"+time+".xlsx";
        // 测试输出
        System.out.println(address);

        QueryWrapper<Score> wrapper = new QueryWrapper<>();
        wrapper.eq("course_name", courseName);
        List<Score> list = scoreMapper.selectList(wrapper);

        int rowNum = 1;
        for(Score score : list){
            XSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(score.getStudentId());
            row1.createCell(1).setCellValue(score.getStudentName());
            row1.createCell(2).setCellValue(score.getCourseName());
            row1.createCell(3).setCellValue(score.getTerm());
            row1.createCell(4).setCellValue(score.getScore());
            row1.createCell(5).setCellValue(score.getPoint());
            rowNum++;
        }


        try {
            OutputStream os = new FileOutputStream(address);
            workbook.write(os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }
}