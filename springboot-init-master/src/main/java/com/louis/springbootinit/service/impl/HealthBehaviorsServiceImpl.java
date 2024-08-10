package com.louis.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.louis.springbootinit.model.entity.HealthBehaviors;
import com.louis.springbootinit.service.HealthBehaviorsService;
import com.louis.springbootinit.mapper.HealthBehaviorsMapper;
import org.springframework.stereotype.Service;


/**
* @author 35064
* @description 针对表【health_behaviors(健康行为表)】的数据库操作Service实现
* @createDate 2024-06-07 20:39:12
*/
@Service
public class HealthBehaviorsServiceImpl extends ServiceImpl<HealthBehaviorsMapper, HealthBehaviors>
    implements HealthBehaviorsService{

}




