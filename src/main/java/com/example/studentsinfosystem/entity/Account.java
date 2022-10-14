package com.example.studentsinfosystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author TX
 * @date 2022/10/8 7:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @TableId(type= IdType.AUTO)
    private Integer id;
    // 用户名
    private String username;
    // 密码
    private String password;
    private Integer role;// 1为老师，2为学生，3为管理员
}
