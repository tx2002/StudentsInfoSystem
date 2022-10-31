package com.example.studentsinfosystem.controller;

import com.example.studentsinfosystem.api.CommonResult;
import com.example.studentsinfosystem.entity.ChooseCourse;
import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.Score;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.service.StudentService;
import com.example.studentsinfosystem.utils.JwtToken;
import io.jsonwebtoken.Claims;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 学生功能相关的接口
 * @author TX
 * @date 2022/10/8 8:55
 */
@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    JwtToken jwtToken = new JwtToken();

    @Autowired
    StudentService studentService;

    /**
     * 学生查询自己个人信息
     * @param token
     * @return
     */
    @GetMapping("/getinfo")
    public CommonResult getinfo(@RequestHeader String token){
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(1) || claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            StudentInfo getinfo = studentService.getinfo(studentId);
            if (getinfo != null) {
                return CommonResult.success(getinfo);
            } else {
                return CommonResult.failed("未查询到该学生");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 学生查询自己的全部课程
     * @param token
     * @return
     */
    @GetMapping("/getCourse")
    public CommonResult getCourse(@RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            List<CourseInfo> list = studentService.getCourse(studentId);
            if (!list.isEmpty()) {
                return CommonResult.success(list);
            } else {
                return CommonResult.failed("无课程信息");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 学生查询成绩单
     * @param term 学期数
     * @param token
     * @return
     */
    @GetMapping("/report")
    public CommonResult report(@RequestParam Integer term, @RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            List<Score> list = studentService.report(studentId, term);
            if (!list.isEmpty()) {
                return CommonResult.success(list);
            } else {
                return CommonResult.failed("无成绩信息");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 查询学生可选课的列表
     * @param term 学期数
     * @param token
     * @return
     */
    @GetMapping("/chooseCourseList")
    public CommonResult chooseCourseList(@RequestParam Integer term, @RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            // 根据学号查询学生班级
            String className = studentService.getClassNameById(studentId);
            // 根据班级查询该班可选课程
            List<ChooseCourse> courses = studentService.chooseCourseList(className, term);
            // 查询学生已选课程
            List<CourseInfo> courseChoosed = studentService.getCourse(studentId, term);
            Set<String> set = new HashSet<>();
            for (CourseInfo i : courseChoosed) {
                set.add(i.getCourseName());
            }
            // 根据已选课程，对选课列表进行更新
            for (ChooseCourse i : courses) {
                if (set.contains(i.getCourseName())) {
                    i.setClassName("已选");
                } else {
                    i.setClassName("未选");
                }
            }
            if (!courses.isEmpty()) {
                return CommonResult.success(courses);
            } else {
                return CommonResult.failed("无选课信息");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 学生新增选课
     * @param courseName
     * @param term
     * @param token
     * @return
     */
    @PostMapping("/addCourse")
    public CommonResult addCourse(@RequestParam String courseName, @RequestParam Integer term, @RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            // 根据学号查询学生班级
            String className = studentService.getClassNameById(studentId);
            // 根据班级查询该班可选课程
            List<ChooseCourse> courses = studentService.chooseCourseList(className, term);
            ChooseCourse addCourse = new ChooseCourse();
            for (ChooseCourse i : courses) {
                if (i.getCourseName().equals(courseName)) {
                    addCourse = i;
                }
            }
            // 在学生课程信息中新增
            CourseInfo newCourse = new CourseInfo();
            newCourse.setCourseName(addCourse.getCourseName());
            newCourse.setStudentId(studentId);
            newCourse.setPoint(addCourse.getPoint());
            newCourse.setTeacher(addCourse.getTeacher());
            newCourse.setTeacherId(studentService.getTeacherIdByName(addCourse.getTeacher()));
            newCourse.setTerm(addCourse.getTerm());
            int insert = studentService.insert(newCourse);

            // 在成绩表中新增
            Score newScore = new Score();
            newScore.setScore(-1);
            newScore.setTerm(newCourse.getTerm());
            newScore.setPoint(newCourse.getPoint());
            newScore.setCourseName(newCourse.getCourseName());
            newScore.setStudentId(studentId);
            // 根据学号查询学生姓名
            newScore.setStudentName(studentService.getStudentNameById(studentId));
            int insert2 = studentService.insertScore(newScore);

            if (insert == 1) {
                return CommonResult.success("选课成功！");
            } else {
                return CommonResult.failed("选课失败!");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 学生退选课
     * @param courseName
     * @param token
     * @return
     */
    @PostMapping("/deleteCourse")
    public CommonResult deleteCourse(@RequestParam String courseName, @RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            // 在课程信息表中删除
            int delete = studentService.deleteCourse(studentId, courseName);
            // 在成绩表中删除
            int delete2 = studentService.deleteScore(studentId, courseName);

            if (delete == 1) {
                return CommonResult.success("退选成功！");
            } else {
                return CommonResult.failed("退选失败!");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @param token
     * @return
     */
    @PostMapping("/changePassword")
    public CommonResult changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(0) || claims.get("role").equals(1) || claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            String res = studentService.changePassword(studentId, oldPassword, newPassword);
            if (res.equals("修改密码成功！")) {
                return CommonResult.success(res);
            } else {
                return CommonResult.failed(res);
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 下载选课信息
     * @param response
     * @param term
     * @param token
     * @return
     */
    @GetMapping("/outChooseCourse")
    public CommonResult outChooseCourse(HttpServletResponse response, @RequestParam Integer term, @RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            // 根据学号查询学生班级
            String className = studentService.getClassNameById(studentId);
            // 根据班级查询该班可选课程
            List<ChooseCourse> courses = studentService.chooseCourseList(className, term);
            // 查询学生已选课程
            List<CourseInfo> courseChoosed = studentService.getCourse(studentId, term);
            Set<String> set = new HashSet<>();
            for (CourseInfo i : courseChoosed) {
                set.add(i.getCourseName());
            }
            // 根据已选课程，对选课列表进行更新
            for (ChooseCourse i : courses) {
                if (set.contains(i.getCourseName())) {
                    i.setClassName("已选");
                } else {
                    i.setClassName("未选");
                }
            }
            if (!courses.isEmpty()) {
                // 进行文件操作，返回Excel
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
                cell0.setCellValue("课程名称");
                cell0.setCellStyle(cellStyle);
                Cell cell1 = row0.createCell(1);
                cell1.setCellValue("学分");
                Cell cell2 = row0.createCell(2);
                cell2.setCellValue("开课学期");
                Cell cell3 = row0.createCell(3);
                cell3.setCellValue("老师");
                Cell cell4 = row0.createCell(4);
                cell4.setCellValue("是否已选");

                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                String time = format.format(now);
                String fileName = studentId+"-"+time+".xlsx";

                int rowNum = 1;
                for(ChooseCourse i : courses){
                    XSSFRow row1 = sheet.createRow(rowNum);
                    row1.createCell(0).setCellValue(i.getCourseName());
                    row1.createCell(1).setCellValue(i.getPoint());
                    row1.createCell(2).setCellValue(i.getTerm());
                    row1.createCell(3).setCellValue(i.getTeacher());
                    row1.createCell(4).setCellValue(i.getClassName());
                    rowNum++;
                }
                //描述内容在传输过程中的编码格式，BINARY可能不止包含非ASCII字符，还可能不是一个短行（超过1000字符）。
                response.setHeader("Content-Transfer-Encoding", "binary");
                //must-revalidate：强制页面不缓存，post-check=0, pre-check=0：0秒后，在显示给用户之前，该对象被选中进行更新过
                response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                //表示响应可能是任何缓存的，即使它只是通常是非缓存或可缓存的仅在非共享缓存中。
                response.setHeader("Pragma", "public");
                //告诉浏览器这个文件的名字和类型，attachment：作为附件下载；inline：直接打开
                response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\"", fileName));

                try {
                    OutputStream os = response.getOutputStream();
                    workbook.write(os);
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return CommonResult.success("下载成功");
            } else {
                return CommonResult.failed("无选课信息");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }

    /**
     * 下载课程信息
     * @param response
     * @param token
     * @return
     */
    @GetMapping("/outCourseInfo")
    public CommonResult outCourseInfo(HttpServletResponse response, @RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            List<CourseInfo> list = studentService.getCourse(studentId);
            if (!list.isEmpty()) {
                // 进行文件操作，返回Excel
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
                cell0.setCellValue("课程名称");
                cell0.setCellStyle(cellStyle);
                Cell cell1 = row0.createCell(1);
                cell1.setCellValue("老师");
                Cell cell2 = row0.createCell(2);
                cell2.setCellValue("老师工号");
                Cell cell3 = row0.createCell(3);
                cell3.setCellValue("学分");
                Cell cell4 = row0.createCell(4);
                cell4.setCellValue("学生学号");
                Cell cell5 = row0.createCell(5);
                cell4.setCellValue("开课学期");

                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                String time = format.format(now);
                String fileName = studentId+"-"+time+".xlsx";

                int rowNum = 1;
                for(CourseInfo i : list){
                    XSSFRow row1 = sheet.createRow(rowNum);
                    row1.createCell(0).setCellValue(i.getCourseName());
                    row1.createCell(1).setCellValue(i.getTeacher());
                    row1.createCell(2).setCellValue(i.getTeacherId());
                    row1.createCell(3).setCellValue(i.getPoint());
                    row1.createCell(4).setCellValue(i.getStudentId());
                    row1.createCell(5).setCellValue(i.getTerm());
                    rowNum++;
                }
                //描述内容在传输过程中的编码格式，BINARY可能不止包含非ASCII字符，还可能不是一个短行（超过1000字符）。
                response.setHeader("Content-Transfer-Encoding", "binary");
                //must-revalidate：强制页面不缓存，post-check=0, pre-check=0：0秒后，在显示给用户之前，该对象被选中进行更新过
                response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                //表示响应可能是任何缓存的，即使它只是通常是非缓存或可缓存的仅在非共享缓存中。
                response.setHeader("Pragma", "public");
                //告诉浏览器这个文件的名字和类型，attachment：作为附件下载；inline：直接打开
                response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\"", fileName));

                try {
                    OutputStream os = response.getOutputStream();
                    workbook.write(os);
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return CommonResult.success("下载成功");
            } else {
                return CommonResult.failed("无课程信息");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }

    @GetMapping("/outReport")
    public CommonResult outReport(HttpServletResponse response, @RequestParam Integer term, @RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        // 权限判断
        if(claims.get("role").equals(2)){
            String studentId = (String) claims.get("username");
            List<Score> list = studentService.report(studentId, term);
            if (!list.isEmpty()) {
                // 进行文件操作，返回Excel
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
                cell0.setCellValue("学生学号");
                cell0.setCellStyle(cellStyle);
                Cell cell1 = row0.createCell(1);
                cell1.setCellValue("学生姓名");
                Cell cell2 = row0.createCell(2);
                cell2.setCellValue("课程名称");
                Cell cell3 = row0.createCell(3);
                cell3.setCellValue("开课学期");
                Cell cell4 = row0.createCell(4);
                cell4.setCellValue("学分");
                Cell cell5 = row0.createCell(5);
                cell5.setCellValue("成绩");

                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                String time = format.format(now);
                String fileName = studentId+"-"+time+".xlsx";

                int rowNum = 1;
                for(Score i : list){
                    XSSFRow row1 = sheet.createRow(rowNum);
                    row1.createCell(0).setCellValue(i.getStudentId());
                    row1.createCell(1).setCellValue(i.getStudentName());
                    row1.createCell(2).setCellValue(i.getCourseName());
                    row1.createCell(3).setCellValue(i.getTerm());
                    row1.createCell(4).setCellValue(i.getPoint());
                    if (i.getScore() == -1) {
                        row1.createCell(5).setCellValue("未考");
                    } else {
                        row1.createCell(5).setCellValue(i.getScore());
                    }
                    rowNum++;
                }
                //描述内容在传输过程中的编码格式，BINARY可能不止包含非ASCII字符，还可能不是一个短行（超过1000字符）。
                response.setHeader("Content-Transfer-Encoding", "binary");
                //must-revalidate：强制页面不缓存，post-check=0, pre-check=0：0秒后，在显示给用户之前，该对象被选中进行更新过
                response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                //表示响应可能是任何缓存的，即使它只是通常是非缓存或可缓存的仅在非共享缓存中。
                response.setHeader("Pragma", "public");
                //告诉浏览器这个文件的名字和类型，attachment：作为附件下载；inline：直接打开
                response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\"", fileName));

                try {
                    OutputStream os = response.getOutputStream();
                    workbook.write(os);
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return CommonResult.success("下载成功");
            } else {
                return CommonResult.failed("无成绩信息");
            }
        } else {
            return CommonResult.failed("无权限");
        }
    }
}
