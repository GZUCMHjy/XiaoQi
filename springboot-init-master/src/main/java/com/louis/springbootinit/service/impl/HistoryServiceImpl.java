package com.louis.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.louis.springbootinit.model.entity.History;
import com.louis.springbootinit.service.HistoryService;
import com.louis.springbootinit.mapper.HistoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 35064
* @description 针对表【history】的数据库操作Service实现
* @createDate 2024-06-13 16:10:07
*/
@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History>
    implements HistoryService{

}




