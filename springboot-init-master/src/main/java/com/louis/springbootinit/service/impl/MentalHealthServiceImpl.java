package com.louis.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.louis.springbootinit.model.entity.MentalHealth;
import com.louis.springbootinit.service.MentalHealthService;
import com.louis.springbootinit.mapper.MentalHealthMapper;
import org.springframework.stereotype.Service;

/**
* @author 35064
* @description 针对表【mental_health(心理健康表)】的数据库操作Service实现
* @createDate 2024-06-07 20:39:12
*/
@Service
public class MentalHealthServiceImpl extends ServiceImpl<MentalHealthMapper, MentalHealth>
    implements MentalHealthService{

}




