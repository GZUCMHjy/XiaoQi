package com.louis.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.louis.springbootinit.model.entity.BodyHealth;
import com.louis.springbootinit.service.BodyHealthService;
import com.louis.springbootinit.mapper.BodyHealthMapper;
import org.springframework.stereotype.Service;

/**
* @author 35064
* @description 针对表【body_health(身体健康信息表)】的数据库操作Service实现
* @createDate 2024-06-07 20:39:12
*/
@Service
public class BodyHealthServiceImpl extends ServiceImpl<BodyHealthMapper, BodyHealth>
    implements BodyHealthService{

}




