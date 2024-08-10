package com.louis.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.louis.springbootinit.mapper.ChatDataRecordMapper;
import com.louis.springbootinit.model.entity.ChatDataRecord;
import com.louis.springbootinit.service.ChatDataRecordService;
import org.springframework.stereotype.Service;

/**
* @author 35064
* @description 针对表【chat_data_record(聊天数据记录表)】的数据库操作Service实现
* @createDate 2024-05-30 18:00:07
*/
@Service
public class ChatDataRecordServiceImpl extends ServiceImpl<ChatDataRecordMapper, ChatDataRecord>
    implements ChatDataRecordService {

}




