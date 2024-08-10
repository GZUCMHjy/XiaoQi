package com.louis.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.louis.springbootinit.model.entity.MedicalRecords;
import com.louis.springbootinit.service.MedicalRecordsService;
import com.louis.springbootinit.mapper.MedicalRecordsMapper;
import org.springframework.stereotype.Service;

/**
* @author 35064
* @description 针对表【medical_records(医疗记录表)】的数据库操作Service实现
* @createDate 2024-06-07 20:39:12
*/
@Service
public class MedicalRecordsServiceImpl extends ServiceImpl<MedicalRecordsMapper, MedicalRecords>
    implements MedicalRecordsService{

}




