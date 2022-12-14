package com.example.studentsinfosystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author TX
 * @date 2022/10/8 8:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfo {
    @TableId(type= IdType.AUTO)
    private Integer id;
    // 学生姓名
    private String studentName;
    // 学生学号
    private String studentId;
    // 学生班级
    private String className;
}
