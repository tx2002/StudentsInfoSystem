//package com.example.studentsinfosystem.utils;
//
//import com.alibaba.excel.context.AnalysisContext;
//import com.alibaba.excel.event.AnalysisEventListener;
//import com.example.studentsinfosystem.entity.StudentInfo;
//import com.example.studentsinfosystem.service.TeacherService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class ExcelListener extends AnalysisEventListener<Student> {
//
//    @Autowired
//    TeacherService teacherService;
//    // 一行行读取Excel中的内容（表头不会读取）
//    @Override
//    public void invoke(Student student, AnalysisContext analysisContext) {
//        StudentInfo studentInfo = new StudentInfo();
//        studentInfo.setStudentId(student.getStudentId());
//        studentInfo.setStudentName(student.getName());
//        studentInfo.setClassName(student.getClassName());
//    }
//
//    // 读取完成后做的内容
//    @Override
//    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//
//    }
//}
