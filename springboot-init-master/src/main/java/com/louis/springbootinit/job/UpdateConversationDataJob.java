//package com.louis.springbootinit.job;
//
//import com.alibaba.dashscope.app.ApplicationResult;
//import com.alibaba.dashscope.exception.InputRequiredException;
//import com.alibaba.dashscope.exception.NoApiKeyException;
//import com.alibaba.dashscope.utils.JsonUtils;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.louis.springbootinit.manager.AiManager;
//import com.louis.springbootinit.model.dto.ChatData;
//import com.louis.springbootinit.model.dto.ConversationVO;
//import com.louis.springbootinit.model.entity.HistoryData;
//import com.louis.springbootinit.model.entity.TaskInfo;
//import com.louis.springbootinit.model.enums.ModelEnums;
//import com.louis.springbootinit.service.HistoryDataService;
//import com.louis.springbootinit.service.TaskInfoService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import javax.annotation.Resource;
//import java.util.*;
//
///**
// * @author louis
// * @version 1.0
// * @date 2024/6/9 21:15
// */
//@Component
//@Slf4j
//public class UpdateConversationDataJob {
//
//    @Resource
//    private HistoryDataService historyDataService;
//    @Resource
//    private TaskInfoService taskService;
//    // 每晚12:30执行任务
//    // todo 自定义线程池，并发执行任务
//    @Scheduled(cron = "0 30 0 * * ?")
//    public void updateConversation(){
//        // 拉取未执行的任务
//        List<TaskInfo> tasks = taskService.list(new QueryWrapper<TaskInfo>().eq("status", 0));
//        if(CollectionUtils.isEmpty(tasks)){
//            // 无任务消费
//            return;
//        }
//        for(TaskInfo task : tasks){
//            String uuid = task.getUuid();
//            String prompt = task.getPrompt();
//            Long userId = task.getUserId();
//            // 目标数据
//            HistoryData targetHistoryData = historyDataService.getOne(new QueryWrapper<HistoryData>().eq("userId", userId));
//            String context = targetHistoryData.getContext();
//            ConversationVO conversationVO = JsonUtils.fromJson(context, ConversationVO.class);
//            Map<String, List<ChatData>> chatDataMap = conversationVO.getChatDataMap();
//            List<ChatData> chatDatas = chatDataMap.get(uuid);
//            for(int i = 0; i < chatDatas.size(); i++){
//                if(chatDatas.get(i).getText().contains(prompt)){
//                    // todo 有问题（全量替换，导致不符内容消失）
//                    chatDatas.get(i + 1).setText(task.getPicUrl());// 更新图片url
//                    break;
//                }
//            }
//            chatDataMap.put(uuid,chatDatas);
//            conversationVO.setChatDataMap(chatDataMap);
//            targetHistoryData.setContext(conversationVO.toString());
//            historyDataService.updateById(targetHistoryData);// 更新conversation
//            task.setStatus(1);// 已执行
//            task.setUpdatedAt(new Date());
//            taskService.updateById(task);
//        }
//    }
//}
