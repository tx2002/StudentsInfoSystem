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
    private String username;
    private String password;
    private Integer role;
}
