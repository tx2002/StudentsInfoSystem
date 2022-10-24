package com.example.studentsinfosystem.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.studentsinfosystem.entity.StudentInfo;
import com.example.studentsinfosystem.entity.TeacherInfo;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TX
 * @date 2022/10/8 8:22
 */
@Repository
public interface TeacherInfoMapper extends BaseMapper<TeacherInfo> {
}
