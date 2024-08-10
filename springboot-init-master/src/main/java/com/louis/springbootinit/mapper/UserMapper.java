package com.louis.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.louis.springbootinit.model.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据库操作
 */
public interface UserMapper extends BaseMapper<User> {
}




