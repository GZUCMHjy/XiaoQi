package com.louis.springbootinit.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author louis
 * @version 1.0
 * @date 2024/5/30 17:01
 */
@Data
public class ConversationVO {
    /**
     * 当前对话窗口的基本信息
     */
    private Conversation currentConversation;
    /**
     * 当前模型下的窗口数
     */
    private List<Conversation> conversationList;
    /**
     * key - 对话窗口uuid
     * value - 对话窗口的历史对话记录
     */
    private Map<String,List<ChatData>> chatDataMap;
}
