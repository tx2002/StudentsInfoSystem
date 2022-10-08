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
    private String name;
    private String studentId;
    private String className;
}
