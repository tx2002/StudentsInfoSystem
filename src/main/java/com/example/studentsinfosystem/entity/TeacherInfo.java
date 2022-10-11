package com.example.studentsinfosystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherInfo {
    // 老师工号
    private String teacherId;
    // 老师姓名
    private String teacherName;
}
