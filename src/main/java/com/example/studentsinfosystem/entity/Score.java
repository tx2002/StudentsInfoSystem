package com.example.studentsinfosystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author TX
 * @date 2022/10/8 7:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    // 学生学号
    private String studentId;
    // 学生姓名
    private String studentName;
    // 课程名称
    private String courseName;
    // 开课学期
    private Integer term;
    // 课程成绩
    private Integer score;
}
