package com.louis.springbootinit.service;

import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.louis.springbootinit.model.dto.request.ChatRequest;
import com.louis.springbootinit.model.entity.Model;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author louis
 * @version 1.0
 * @date 2024/5/25 22:39
 */
public interface ChatService {

    /**
     * 开启对话
     * @param param
     * @param res
     * @return
     * @throws ApiException
     * @throws NoApiKeyException
     * @throws InputRequiredException
     */
    public Flux<String> chat(ChatRequest param, HttpServletResponse res, HttpServletRequest req) throws ApiException, NoApiKeyException, InputRequiredException, IOException;

    /**
     * 单指令调用大模型（测试版）
     * @param prompt
     * @param res
     * @return
     * @throws ApiException
     * @throws NoApiKeyException
     * @throws InputRequiredException
     * @throws IOException
     */
    public Flux<String> chat(String prompt, HttpServletResponse res) throws ApiException, NoApiKeyException, InputRequiredException, IOException;

    SseEmitter sseChat(String prompt, String modelId, HttpServletRequest req, SseEmitter emitter);

    SseEmitter sseChat(ChatRequest param, HttpServletRequest req, SseEmitter emitter);

    String queryOneQuest(String modelId,HttpServletRequest req) throws NoApiKeyException, InputRequiredException;

    String replaceImgUrl(String imgUrl, HttpServletRequest req) throws IOException;
}
