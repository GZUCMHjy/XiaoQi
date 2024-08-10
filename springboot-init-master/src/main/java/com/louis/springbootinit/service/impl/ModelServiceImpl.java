package com.louis.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.louis.springbootinit.mapper.ModelMapper;
import com.louis.springbootinit.model.entity.Model;
import com.louis.springbootinit.service.ModelService;
import org.springframework.stereotype.Service;
/**
* @author 35064
* @description 针对表【model】的数据库操作Service实现
* @createDate 2024-05-30 16:25:09
*/
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model>
    implements ModelService {

}




