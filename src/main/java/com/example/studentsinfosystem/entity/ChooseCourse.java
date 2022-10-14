package com.example.studentsinfosystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author TX
 * @date 2022/10/10 14:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChooseCourse {
    // 开课班级名称
    private String className;
    // 开课学期
    private Integer term;
    // 学分
    private String point;
    // 课程名称
    private String courseName;
    // 老师姓名
    private String teacher;
}
