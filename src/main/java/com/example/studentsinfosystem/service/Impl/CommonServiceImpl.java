package com.example.studentsinfosystem.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.studentsinfosystem.entity.Account;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.mapper.AccountMapper;
import com.example.studentsinfosystem.mapper.StudentInfoMapper;
import com.example.studentsinfosystem.service.CommonService;
import com.example.studentsinfosystem.utils.JwtToken;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;

import java.io.IOException;

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
}