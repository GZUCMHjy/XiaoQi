package com.louis.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.louis.springbootinit.model.entity.Biomarkers;
import com.louis.springbootinit.service.BiomarkersService;
import com.louis.springbootinit.mapper.BiomarkersMapper;
import org.springframework.stereotype.Service;

/**
* @author 35064
* @description 针对表【biomarkers(生物标志物表)】的数据库操作Service实现
* @createDate 2024-06-07 20:39:12
*/
@Service
public class BiomarkersServiceImpl extends ServiceImpl<BiomarkersMapper, Biomarkers>
    implements BiomarkersService{

}




