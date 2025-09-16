package com.louis.springbootinit.job;

import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.louis.springbootinit.facade.AiFacade;
import com.louis.springbootinit.model.enums.ModelEnums;
import com.louis.springbootinit.model.vo.ModelCredentials;
import com.louis.springbootinit.strategy.ModelSelectionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author louis
 * @version 1.0
 * @date 2024/6/9 21:15
 */
@Component
@Slf4j
public class GenerateQuestionsJob {

    @Resource
    private AiFacade aiFacade;
    @Resource
    private ModelSelectionStrategy modelSelectionStrategy;
    // 使用ConcurrentHashMap 提高并发能力且线程安全
    private Map<String, List<String>> mapToQuestion = new ConcurrentHashMap<>();
    private final Map<String, ModelEnums> map = new HashMap<>();
    private final static int MAX_QUESTIONS = 5;
    // 每晚十二点数据预热
    @Scheduled(cron = "0 0 0 * * ?")
    public void generateQuestion() throws NoApiKeyException, InputRequiredException {
        map.put(ModelEnums.MODEL1.getModelId(),ModelEnums.MODEL1);
        map.put(ModelEnums.MODEL2.getModelId(),ModelEnums.MODEL2);
        map.put(ModelEnums.MODEL3.getModelId(),ModelEnums.MODEL3);
        map.put(ModelEnums.MODEL4.getModelId(),ModelEnums.MODEL4);
        map.put(ModelEnums.MODEL5.getModelId(),ModelEnums.MODEL5);
        map.put(ModelEnums.MODEL6.getModelId(),ModelEnums.MODEL6);
        // todo 可以自定义线程池，并发创建题目（提高性能）
        for(int i = 0 ; i < 6 ; i++){
            // 六个专题
            List<String> arrayList = new ArrayList<>();
            for(int j = 0 ; j < MAX_QUESTIONS ; j++){
                // 每个专题5个题目
                ModelEnums modelEnums = map.get(String.valueOf(i));
                ModelCredentials credentials = modelSelectionStrategy.resolveByModelId(modelEnums.getModelId());
                log.info(modelEnums+"");
                String modelName = modelEnums.getModelName().substring(0,6);
                String prompt = "给我提一个有关于"+modelName+"相关领域的问题题目吗?" +
                        "题目格式举例:如何大力发展中医药产业发展? " +
                        "你只需要提供问题题目即可，只要一个，不要有其他的多余的话";
                ApplicationResult call = aiFacade.call(credentials, prompt);
                String curQuestion = call.getOutput().getText();
                arrayList.add(curQuestion);
            }
            // 避免覆盖原有值
            mapToQuestion.putIfAbsent(map.get(String.valueOf(i)).getModelId(),arrayList);
        }
    }
    public  Map<String, List<String>> getMapToQuestion() {
        return new HashMap<>(mapToQuestion);
    }

    // Model selection is handled by the injected strategy
}
