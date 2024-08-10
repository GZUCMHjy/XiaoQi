package com.louis.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.louis.springbootinit.mapper.TaskInfoMapper;
import com.louis.springbootinit.model.entity.TaskInfo;
import com.louis.springbootinit.service.TaskInfoService;
import org.springframework.stereotype.Service;

/**
* @author 35064
* @description 针对表【task_info(任务信息表)】的数据库操作Service实现
* @createDate 2024-07-28 01:48:10
*/
@Service
public class TaskInfoServiceImpl extends ServiceImpl<TaskInfoMapper, TaskInfo>
    implements TaskInfoService {

}




