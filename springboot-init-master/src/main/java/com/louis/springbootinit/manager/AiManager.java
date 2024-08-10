package com.louis.springbootinit.manager;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.app.RagApplicationParam;
import com.alibaba.dashscope.common.History;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import io.reactivex.Flowable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author louis
 * @version 1.0
 * @date 2024/6/7 17:15
 */
@Component
public class AiManager {
    public AiManager(){}


    /**
     * 初始化模型参数（并默认开启历史对话）
      * @param apiKey
     * @param appKey
     * @param prompt
     * @param sessionId
     * @return
     * @throws NoApiKeyException
     * @throws InputRequiredException
     */
    public Flowable<ApplicationResult> streamCall(String apiKey,String appKey,String prompt,String sessionId) throws NoApiKeyException, InputRequiredException {
        RagApplicationParam ragApplicationParam = initRagApplicationParam(apiKey, appKey, prompt,sessionId);
        Application application = new Application();
        return application.streamCall(ragApplicationParam);
    }
    public Flowable<ApplicationResult> streamCall(String apiKey,String appKey,String prompt,String sessionId,boolean isIncrement) throws NoApiKeyException, InputRequiredException {
        RagApplicationParam ragApplicationParam = initRagApplicationParam(apiKey, appKey, prompt,sessionId,isIncrement);
        Application application = new Application();
        return application.streamCall(ragApplicationParam);
    }
    public Flowable<ApplicationResult> streamCall(String apiKey,String appKey,String prompt) throws NoApiKeyException, InputRequiredException {
        ApplicationParam applicationParam = initAppParam(apiKey, appKey, prompt);
        Application application = new Application();
        return application.streamCall(applicationParam);
    }
    public Flowable<ApplicationResult> streamCall(String apiKey,String appKey,String prompt,boolean isIncrementalOutputOutput) throws NoApiKeyException, InputRequiredException {
        RagApplicationParam ragApplicationParam = initRagApplicationParam(apiKey, appKey, prompt,isIncrementalOutputOutput);
        Application application = new Application();
        return application.streamCall(ragApplicationParam);
    }
    public ApplicationResult call(String apiKey,String appKey,String prompt,String sessionId) throws NoApiKeyException, InputRequiredException {
        RagApplicationParam ragApplicationParam = initRagApplicationParam(apiKey, appKey, prompt,sessionId);
        Application application = new Application();
        return application.call(ragApplicationParam);
    }
    public ApplicationResult call(String apiKey,String appKey,String prompt) throws NoApiKeyException, InputRequiredException {
        RagApplicationParam ragApplicationParam = initRagApplicationParam(apiKey, appKey, prompt);
        Application application = new Application();
        return application.call(ragApplicationParam);
    }
    /**
     * 初始化应用参数(默认开启增量输出)
     * @param apiKey
     * @param appKey
     * @param prompt
     * @return
     */
    public ApplicationParam initAppParam(String apiKey, String appKey, String prompt,String sessionId){
        ApplicationParam applicationParam= ApplicationParam.builder()
                .apiKey(apiKey)
                .appId(appKey)
                .sessionId(sessionId)
                .prompt(prompt)
                .incrementalOutput(true)
                .build();
        return applicationParam;
    }

    /**
     * 初始化模型（默认关闭增式输出）
     * @param apiKey
     * @param appKey
     * @param prompt
     * @return
     */
    public ApplicationParam initAppParam(String apiKey, String appKey, String prompt){
        ApplicationParam applicationParam= ApplicationParam.builder()
                .apiKey(apiKey)
                .appId(appKey)
                .prompt(prompt)
                .incrementalOutput(false)
                .build();
        return applicationParam;
    }

    /**
     * 初始化(增强)应用参数
     * @param apiKey
     * @param appKey
     * @param prompt
     * @return
     */
    public RagApplicationParam initRagApplicationParam(String apiKey, String appKey, String prompt){
        RagApplicationParam param = RagApplicationParam.builder()
                .apiKey(apiKey)
                .appId(appKey)
                .prompt(prompt)
                .build();
        return param;
    }

    /**
     * 是否开启增量输出
     * @param apiKey
     * @param appKey
     * @param prompt
     * @param isIncrementalOutputOutput
     * @return
     */
    public RagApplicationParam initRagApplicationParam(String apiKey, String appKey, String prompt,boolean isIncrementalOutputOutput){
        RagApplicationParam param = RagApplicationParam.builder()
                .apiKey(apiKey)
                .appId(appKey)
                .prompt(prompt)
                .incrementalOutput(isIncrementalOutputOutput)
                .build();
        return param;
    }
    public RagApplicationParam initRagApplicationParam(String apiKey, String appKey, String prompt,String sessionId){
        RagApplicationParam param = RagApplicationParam.builder()
                .apiKey(apiKey)
                .appId(appKey)
                .sessionId(sessionId)
                .prompt(prompt)
                .build();
        return param;
    }

    /**
     * 初始化参数（是否开启增量输出 + 上下文记忆）
     * @param apiKey
     * @param appKey
     * @param prompt
     * @param sessionId
     * @param isPicture
     * @return
     */
    public RagApplicationParam initRagApplicationParam(String apiKey, String appKey, String prompt,String sessionId,boolean isPicture){
        RagApplicationParam param = RagApplicationParam.builder()
                .apiKey(apiKey)
                .appId(appKey)
                .sessionId(sessionId)
                .incrementalOutput(isPicture)
                .prompt(prompt)
                .build();
        return param;
    }
}
