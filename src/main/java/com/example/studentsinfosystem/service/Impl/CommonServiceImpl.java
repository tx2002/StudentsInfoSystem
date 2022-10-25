package com.example.studentsinfosystem.service.Impl;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.studentsinfosystem.entity.Account;
import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.Score;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.mapper.AccountMapper;
import com.example.studentsinfosystem.service.CommonService;
import com.example.studentsinfosystem.utils.ExcelListener;
import com.example.studentsinfosystem.utils.JwtToken;
import com.example.studentsinfosystem.utils.Student;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

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

        @Override
        public int inputStudentInfo(InputStream inputStream) throws IOException {
            //只有教师类才有这个权限
            ExcelListener excelListener = new ExcelListener();
            EasyExcel.read(inputStream, Student.class, excelListener).sheet().doRead();
            return 1;
        }


    }