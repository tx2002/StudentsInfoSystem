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
    private String className;
    private Integer term;
    private Integer point;
    private String courseName;
    private String teacher;
}
