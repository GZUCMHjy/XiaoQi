package com.louis.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.louis.springbootinit.model.dto.ChatData;
import com.louis.springbootinit.model.dto.Conversation;
import com.louis.springbootinit.model.dto.ConversationVO;
import com.louis.springbootinit.model.entity.MsgLog;

import java.util.List;
import java.util.Map;

/**
* @author 35064
* @description 针对表【msg_log(聊天记录表)】的数据库操作Service
* @createDate 2024-05-30 18:00:07
*/
public interface MsgLogService extends IService<MsgLog> {

    ConversationVO saveConversation(ConversationVO conversationVO,String userId);

    List<Conversation> queryUserModelConversation(Long id, String modelId);
    ConversationVO queryUserModelConversation(Long id);
    Map<String, List<ChatData>> queryUserCurrentConversation(String uuid);

    Map<String, List<ChatData>> queryUserAllConversation(String userId,String modelId);
}
