package com.example.studentsinfosystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author TX
 * @date 2022/10/8 7:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    // 课程名称
    private String courseName;
    // 老师姓名
    private String teacher;
    // 老师工号
    private String teacherId;
    // 学分
    private String point;
    // 学生学号
    private String studentId;
    // 开课学期
    private Integer term;
}
