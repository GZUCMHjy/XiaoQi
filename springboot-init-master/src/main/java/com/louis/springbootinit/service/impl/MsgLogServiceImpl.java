package com.louis.springbootinit.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.louis.springbootinit.common.ErrorCode;
import com.louis.springbootinit.exception.BusinessException;
import com.louis.springbootinit.mapper.MsgLogMapper;
import com.louis.springbootinit.model.dto.ChatData;
import com.louis.springbootinit.model.dto.Conversation;
import com.louis.springbootinit.model.dto.ConversationVO;
import com.louis.springbootinit.model.entity.ChatDataRecord;
import com.louis.springbootinit.model.entity.MsgLog;
import com.louis.springbootinit.service.ChatDataRecordService;
import com.louis.springbootinit.service.MsgLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author 35064
* @description 针对表【msg_log(聊天记录表)】的数据库操作Service实现
* @createDate 2024-05-30 18:00:07
*/
@Service
public class MsgLogServiceImpl extends ServiceImpl<MsgLogMapper, MsgLog>
    implements MsgLogService {

    @Resource
    private ChatDataRecordService chatDataRecordService;

    /**
     * 保存历史对话
     * @param conversationVO
     * @return
     */
    @Override
    @Transactional
    public ConversationVO saveConversation(ConversationVO conversationVO,String userId) {
        List<Conversation> conversationList = conversationVO.getConversationList();
        Map<String, List<ChatData>> chatDataMap = conversationVO.getChatDataMap();
        // 保存chatDataMap
        for(Conversation co : conversationList){
            String uuid = co.getUuid();
            List<ChatData> chatDatas = chatDataMap.get(uuid);
            for(ChatData data : chatDatas){
                String status = data.getStatus();
                ChatDataRecord chatDataRecord = new ChatDataRecord();
                if("".equals(status) || status == null){
                    // 说明是用户发起的提问
                    chatDataRecord.setRole("user");// 用户角色
                }else{
                    // 模型回答的数据保存
                    chatDataRecord.setRole("assistant");// 大模型角色
                    try{
                        chatDataRecord.setStatus("success");// 默认成功
                    }catch(Exception e){
                        throw new BusinessException(ErrorCode.OPERATION_ERROR);
                    }
                }
                chatDataRecord.setUuid(uuid);
                chatDataRecord.setDatetime(new DateTime(data.getDateTime()));
                chatDataRecord.setId(data.getId());// 主键
                chatDataRecord.setRequestid(data.getRequestId());
                chatDataRecord.setText(data.getText());
                // 保存
                chatDataRecordService.saveOrUpdate(chatDataRecord);
            }
        }
        List<MsgLog> msgLogs = new ArrayList<>();
        for(int i = 0 ; i < conversationList.size(); i++){
            MsgLog msgLog = new MsgLog();
            BeanUtils.copyProperties(conversationList.get(i),msgLog);
            msgLog.setSysteminfo(conversationList.get(i).getSystem());
            msgLog.setUserId(userId);
            msgLog.setModelid(conversationList.get(i).getModelId());
            msgLogs.add(msgLog);
        }
        // 保存或者更新
        this.saveOrUpdateBatch(msgLogs);
        return conversationVO;
    }

    /**
     * 获取当前用户与模型开启的会话列表
     * @param id
     * @param modelId
     * @return
     */
    @Override
    public List<Conversation> queryUserModelConversation(Long id, String modelId) {
        QueryWrapper<MsgLog> msgLogQueryWrapper = new QueryWrapper<>();
        msgLogQueryWrapper.eq("userId",id);
        msgLogQueryWrapper.eq("modelId",modelId);
        List<MsgLog> list = this.list(msgLogQueryWrapper);
        List<Conversation> conversations = list.stream().map(msgLog -> {
            Conversation conversation = new Conversation();
            BeanUtils.copyProperties(msgLog, conversation);
            return conversation;
        }).collect(Collectors.toList());
        return conversations;
    }
    @Override
    public ConversationVO queryUserModelConversation(Long id) {
        QueryWrapper<MsgLog> msgLogQueryWrapper = new QueryWrapper<>();
        msgLogQueryWrapper.eq("userId",id);
        List<MsgLog> list = this.list(msgLogQueryWrapper);
        List<Conversation> conversations = list.stream().map(msgLog -> {
            Conversation conversation = new Conversation();
            BeanUtils.copyProperties(msgLog, conversation);
            conversation.setSystem(msgLog.getSysteminfo());
            conversation.setModelId(msgLog.getModelid());
            return conversation;
        }).collect(Collectors.toList());
        HashMap<String, List<ChatData>> map = new HashMap<>();
        for(MsgLog msgLog : list){
            String uuid = msgLog.getUuid();
            QueryWrapper<ChatDataRecord> chatDataRecordQueryWrapper = new QueryWrapper<>();
            chatDataRecordQueryWrapper.eq("uuid",uuid);
            List<ChatDataRecord> chatDataRecords = chatDataRecordService.list(chatDataRecordQueryWrapper);
            List<ChatData> chatDataList = chatDataRecords.stream().map(chatDataRecord -> {
                ChatData chatData = new ChatData();
                BeanUtils.copyProperties(chatDataRecord, chatData);
                chatData.setRequestId(chatDataRecord.getRequestid());
                if(chatDataRecord.getStatus() == null){
                    // 用户发出聊天请求默认成功
                    chatData.setStatus("success");
                }else{
                    chatData.setStatus(chatDataRecord.getStatus());
                }
                chatData.setDateTime(chatDataRecord.getDatetime().toString());
                return chatData;
            }).collect(Collectors.toList());
            map.put(uuid,chatDataList);
        }
        ConversationVO conversationVO = new ConversationVO();
        conversationVO.setConversationList(conversations);
        conversationVO.setChatDataMap(map);
        return conversationVO;
    }

    /**
     * 获取当前窗口的历史对话记录
     * @param uuid
     * @return
     */
    @Override
    public Map<String, List<ChatData>> queryUserCurrentConversation(String uuid) {
        QueryWrapper<ChatDataRecord> chatDataRecordQueryWrapper = new QueryWrapper<>();
        chatDataRecordQueryWrapper.eq("uuid",uuid);
        List<ChatDataRecord> list = chatDataRecordService.list(chatDataRecordQueryWrapper);
        HashMap<String, List<ChatData>> stringListHashMap = new HashMap<>();
        List<ChatData> chatDataList = list.stream().map(chatDataRecord -> {
            ChatData chatData = new ChatData();
            BeanUtils.copyProperties(chatDataRecord, chatData);
            return chatData;
        }).collect(Collectors.toList());
        stringListHashMap.put(uuid,chatDataList);
        return stringListHashMap;
    }
    @Override
    public Map<String, List<ChatData>> queryUserAllConversation(String userId,String modelId) {
        List<Conversation> conversations = queryUserModelConversation(Long.valueOf(userId), modelId);
        HashMap<String, List<ChatData>> stringListHashMap = new HashMap<>();
        QueryWrapper<ChatDataRecord> chatDataRecordQueryWrapper = new QueryWrapper<>();

        for(Conversation co: conversations){
            String uuid = co.getUuid();
            chatDataRecordQueryWrapper.eq("uuid",uuid);
            List<ChatDataRecord> list = chatDataRecordService.list(chatDataRecordQueryWrapper);
            List<ChatData> chatDataList = list.stream().map(chatDataRecord -> {
                ChatData chatData = new ChatData();
                BeanUtils.copyProperties(chatDataRecord, chatData);
                return chatData;
            }).collect(Collectors.toList());
            stringListHashMap.put(uuid,chatDataList);
            chatDataRecordQueryWrapper.clear();// 清除拼接的查询条件
        }
        return stringListHashMap;
    }
}




