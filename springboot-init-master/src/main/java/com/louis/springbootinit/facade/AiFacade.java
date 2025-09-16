package com.louis.springbootinit.facade;

import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.louis.springbootinit.manager.AiManager;
import com.louis.springbootinit.model.vo.ModelCredentials;
import io.reactivex.Flowable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Facade for AI model interactions.
 * Encapsulates parameter wiring and exposes a simple API to callers.
 */
@Component
public class AiFacade {

    @Resource
    private AiManager aiManager;

    public Flowable<ApplicationResult> streamWithSession(ModelCredentials credentials,
                                                         String prompt,
                                                         String sessionId,
                                                         boolean incremental)
            throws NoApiKeyException, InputRequiredException {
        return aiManager.streamCall(credentials.getApiKey(), credentials.getAppKey(), prompt, sessionId, incremental);
    }

    public Flowable<ApplicationResult> stream(ModelCredentials credentials,
                                              String prompt,
                                              boolean incremental)
            throws NoApiKeyException, InputRequiredException {
        return aiManager.streamCall(credentials.getApiKey(), credentials.getAppKey(), prompt, incremental);
    }

    public ApplicationResult call(ModelCredentials credentials,
                                  String prompt)
            throws NoApiKeyException, InputRequiredException {
        return aiManager.call(credentials.getApiKey(), credentials.getAppKey(), prompt);
    }
}

