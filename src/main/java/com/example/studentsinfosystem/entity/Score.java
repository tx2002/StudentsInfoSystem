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
    private String studentId;
    private String studentName;
    private String courseName;
    private Integer term;
    private Integer score;
}
