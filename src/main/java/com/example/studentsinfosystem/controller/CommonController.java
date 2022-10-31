package com.example.studentsinfosystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.studentsinfosystem.api.CommonResult;
import com.example.studentsinfosystem.entity.CourseInfo;
import com.example.studentsinfosystem.entity.Score;
import com.example.studentsinfosystem.mapper.CourseInfoMapper;
import com.example.studentsinfosystem.mapper.ScoreMapper;
import com.example.studentsinfosystem.service.CommonService;
import com.example.studentsinfosystem.service.TeacherService;
import com.example.studentsinfosystem.utils.JwtToken;
import com.example.studentsinfosystem.utils.MultipartFileToFile;
import io.jsonwebtoken.Claims;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 梁鑫宇
 * @date 2022/10/11
 */
@RestController
public class CommonController {
    @Autowired
    JwtToken jwtToken = new JwtToken();

    @Autowired
    CommonService commonService;

    @Autowired
    ScoreMapper scoreMapper;

    @Autowired
    CourseInfoMapper courseInfoMapper;

    @Autowired
    TeacherService teacherService;

    /**
     * 登录
     * @param username
     * @param password
     * @return token
     */
    @PostMapping("/login")
    public CommonResult login(@RequestParam String username,@RequestParam String password){
        String result = commonService.login(username,password);
        if(result.equals("0"))
            return CommonResult.failed("用户不存在");
        if(result.equals("1"))
            return  CommonResult.failed("密码错误");
        else {
            Claims claimByToken = jwtToken.getClaimByToken(result);
            String role = claimByToken.get("role").toString();
            Map<String, String> map = new HashMap<>();
            map.put("token", result);
            map.put("role", role);
            return CommonResult.success(map);
        }

    }

    /**
     * 通过文件导入学生信息
     * 有个小问题，如果老师的文件格式不正确，将有问题
     * @param token
     * @param file
     * @return 提示信息
     * @throws Exception
     */
    @PostMapping("/inputstudentinfo")
    public CommonResult inputStudentInfo(@RequestHeader String token,
                                         @RequestBody MultipartFile file) throws Exception {
        Claims claims = jwtToken.getClaimByToken(token);
        if(claims.get("role").equals(0)){
            File files = MultipartFileToFile.multipartFileToFile(file);
            String address = files.getAbsolutePath();
            int judge = commonService.inputStudentInfo(address);
            if(judge==1)
                return CommonResult.success("导入成功");
            else
                return CommonResult.failed("导入失败");
        }
        else
            return CommonResult.failed("无权限");
    }

    /**
     * 导入课程
     * @param token
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/inputcourseinfo")
    public CommonResult inputCoursseInfo(@RequestHeader String token,
                                         @RequestBody MultipartFile file) throws Exception {
        Claims claims = jwtToken.getClaimByToken(token);
        if (claims.get("role").equals(0)) {
            File files = MultipartFileToFile.multipartFileToFile(file);
            String address = files.getAbsolutePath();
            int judge = commonService.inputCourseInfo(address);
            if (judge == 1)
                return CommonResult.success("导入成功");
            else
                return CommonResult.failed("导入失败");
        } else
            return CommonResult.failed("无权限");
    }

    @PostMapping("/inputteacherinfo")
    public CommonResult inputTeacherInfo(@RequestHeader String token,
                                         @RequestBody MultipartFile file) throws Exception{
        Claims claims = jwtToken.getClaimByToken(token);
        if(claims.get("role").equals(0)){
            File files = MultipartFileToFile.multipartFileToFile(file);
            String address = files.getAbsolutePath();
            int judge = commonService.inputTeacherInfo(address);
            if (judge == 1)
                return CommonResult.success("导入成功");
            else
                return CommonResult.failed("导入失败");
        } else
            return CommonResult.failed("无权限");
    }

    /**
     * 查询所教课程中学生的成绩
     * @param token
     * @param courseName
     * @return
     */
    @GetMapping("/outputstudentscore")
    public CommonResult outputStudentScore(HttpServletResponse response,
                                      @RequestHeader String token,
                                      @RequestParam String courseName) {
        Claims claims = jwtToken.getClaimByToken(token);
        QueryWrapper<Score> wrapper = new QueryWrapper<>();
        wrapper.eq("course_name", courseName);
        List<Score> list = scoreMapper.selectList(wrapper);
        if (claims.get("role").equals(1)) {
            if (!list.isEmpty()) {
                // 创建表格
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

                // 生成一个本地时间，用于命名
                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String time = format.format(now);
                String fileName = courseName + "学生成绩信息" + time + ".xlsx";


                int rowNum = 1;
                for (Score score : list) {
                    XSSFRow row1 = sheet.createRow(rowNum);
                    row1.createCell(0).setCellValue(score.getStudentId());
                    row1.createCell(1).setCellValue(score.getStudentName());
                    row1.createCell(2).setCellValue(score.getCourseName());
                    row1.createCell(3).setCellValue(score.getTerm());
                    row1.createCell(4).setCellValue(score.getScore());
                    row1.createCell(5).setCellValue(score.getPoint());
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
                // Minio
                try {
                    MinioClient minioClient =
                            MinioClient.builder()
                                    .endpoint("http://1.117.115.133:9000")
                                    .credentials("7bhUZhY8i0RBpiGU", "KlPntiSmUQylYvmlsYatnVOAV64icHW4")
                                    .build();
                    boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("student").build());
                    if (!found) {
                        minioClient.makeBucket(MakeBucketArgs.builder().bucket("student").build());
                    } else {
                        System.out.println("Bucket 'student' already exists.");
                    }

                    minioClient.uploadObject(
                            UploadObjectArgs.builder()
                                    .bucket("student")
                                    .object(fileName)
                                    .filename("/home/android/" + fileName)
                                    .build());

                } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
                    System.out.println("Error occurred: " + e);
                    System.out.println("上传失败");
                }
                return CommonResult.success("http://1.117.115.133:9000/student/" + fileName);
            } else
                return CommonResult.failed("下载的目标为空");
        } else
            return CommonResult.failed("无下载权限");
    }

    /**
     * 导出课程内学生信息
     * @param response
     * @param token
     * @param courseName
     * @return
     */
    @GetMapping("/outputstudentinfo")
    public CommonResult outputStudentInfo(HttpServletResponse response,
                                         @RequestHeader String token,
                                         @RequestParam String courseName) {
        Claims claims = jwtToken.getClaimByToken(token);
        QueryWrapper<CourseInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_name", courseName);
        List<CourseInfo> list = courseInfoMapper.selectList(wrapper);
        if (claims.get("role").equals(1) || claims.get("role").equals(0)) {
            if (!list.isEmpty()) {
                // 创建表格
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
                cell0.setCellValue("课程名");
                cell0.setCellStyle(cellStyle);
                Cell cell1 = row0.createCell(1);
                cell1.setCellValue("任课老师");
                Cell cell2 = row0.createCell(2);
                cell2.setCellValue("老师工号");
                Cell cell3 = row0.createCell(3);
                cell3.setCellValue("学分");
                Cell cell4 = row0.createCell(4);
                cell4.setCellValue("学生学号");
                Cell cell5 = row0.createCell(5);
                cell5.setCellValue("开课学期");

                // 生成一个本地时间，用于命名
                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String time = format.format(now);
                String fileName = courseName + "信息(学生)" + time + ".xlsx";


                int rowNum = 1;
                for (CourseInfo courseInfo : list) {
                    XSSFRow row1 = sheet.createRow(rowNum);
                    row1.createCell(0).setCellValue(courseInfo.getCourseName());
                    row1.createCell(1).setCellValue(courseInfo.getTeacher());
                    row1.createCell(2).setCellValue(courseInfo.getTeacherId());
                    row1.createCell(3).setCellValue(courseInfo.getPoint());
                    row1.createCell(4).setCellValue(courseInfo.getStudentId());
                    row1.createCell(5).setCellValue(courseInfo.getTerm());
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
                // Minio
                try {
                    MinioClient minioClient =
                            MinioClient.builder()
                                    .endpoint("http://1.117.115.133:9000")
                                    .credentials("7bhUZhY8i0RBpiGU", "KlPntiSmUQylYvmlsYatnVOAV64icHW4")
                                    .build();
                    boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("student").build());
                    if (!found) {
                        minioClient.makeBucket(MakeBucketArgs.builder().bucket("student").build());
                    } else {
                        System.out.println("Bucket 'student' already exists.");
                    }

                    minioClient.uploadObject(
                            UploadObjectArgs.builder()
                                    .bucket("student")
                                    .object(fileName)
                                    .filename("/home/android/" + fileName)
                                    .build());

                } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
                    System.out.println("Error occurred: " + e);
                    System.out.println("上传失败");
                }
                return CommonResult.success("http://1.117.115.133:9000/student/" + fileName);
            } else
                return CommonResult.failed("下载的目标为空");
        } else
            return CommonResult.failed("无下载权限");
    }

    /**
     * 导出所教课程信息
     * @param response
     * @param token
     * @return
     */
    @GetMapping("/outputcourseinfo")
    public CommonResult outputCourseInfo(HttpServletResponse response,
                                         @RequestHeader String token) {
        Claims claims = jwtToken.getClaimByToken(token);
        List<CourseInfo> courseInfos = teacherService.getCourse((String) claims.get("username"));
        if (claims.get("role").equals(1) || claims.get("role").equals(0)) {
            if (!courseInfos.isEmpty()) {
                // 创建表格
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
                cell0.setCellValue("课程名");
                cell0.setCellStyle(cellStyle);
                Cell cell1 = row0.createCell(1);
                cell1.setCellValue("任课老师");
                Cell cell2 = row0.createCell(2);
                cell2.setCellValue("学分");
                Cell cell3 = row0.createCell(3);
                cell3.setCellValue("开课学期");

                // 生成一个本地时间，用于命名
                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String time = format.format(now);
                String fileName =  "所教课程信息" + time + ".xlsx";


                int rowNum = 1;
                for (CourseInfo i : courseInfos) {
                    XSSFRow row1 = sheet.createRow(rowNum);
                    row1.createCell(0).setCellValue(i.getCourseName());
                    row1.createCell(1).setCellValue(i.getTeacher());
                    row1.createCell(2).setCellValue(i.getPoint());
                    row1.createCell(3).setCellValue(i.getTerm());
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
                // Minio 统一的命字都是student
                try {
                    MinioClient minioClient =
                            MinioClient.builder()
                                    .endpoint("http://1.117.115.133:9000")
                                    .credentials("7bhUZhY8i0RBpiGU", "KlPntiSmUQylYvmlsYatnVOAV64icHW4")
                                    .build();
                    boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("student").build());
                    if (!found) {
                        minioClient.makeBucket(MakeBucketArgs.builder().bucket("student").build());
                    } else {
                        System.out.println("Bucket 'student' already exists.");
                    }

                    minioClient.uploadObject(
                            UploadObjectArgs.builder()
                                    .bucket("student")
                                    .object(fileName)
                                    .filename("/home/android/" + fileName)
                                    .build());

                } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
                    System.out.println("Error occurred: " + e);
                    System.out.println("上传失败");
                }
                return CommonResult.success("http://1.117.115.133:9000/student/" + fileName);
            } else
                return CommonResult.failed("下载的目标为空");
        } else
            return CommonResult.failed("无下载权限");
    }
}