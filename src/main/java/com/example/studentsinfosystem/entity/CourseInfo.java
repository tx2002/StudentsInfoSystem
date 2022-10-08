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
    private String name;
    private String teacher;
    private String teacherId;
    private String point;
    private String studentId;
    private Integer term;
}
