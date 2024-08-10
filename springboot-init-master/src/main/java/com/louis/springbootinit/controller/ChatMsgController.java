package com.louis.springbootinit.controller;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.louis.springbootinit.annotation.AuthCheck;
import com.louis.springbootinit.common.BaseResponse;
import com.louis.springbootinit.common.ResultUtils;
import com.louis.springbootinit.constant.UserConstant;
import com.louis.springbootinit.model.dto.ReplaceImgDTO;
import com.louis.springbootinit.model.dto.request.ChatRequest;
import com.louis.springbootinit.service.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author louis
 * @version 1.0
 * @date 2024/5/25 23:27
 */
@RestController
@RequestMapping("/sse")
@Api(value = "聊天模块", tags = "聊天模块")
@Slf4j
public class ChatMsgController {

    @Resource
    private ChatService chatService;

    @PostMapping(value = "/chat")
    @ApiOperation(value = "开启聊天")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public SseEmitter sse(@RequestBody ChatRequest param,HttpServletRequest req) {
        SseEmitter emitter = new SseEmitter();
        emitter = chatService.sseChat(param,req,emitter);
        return emitter;
    }
    @GetMapping(value = "/question")
    @ApiOperation(value = "每日一问")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<String> queryOneQuest(String modelId,HttpServletRequest req) throws NoApiKeyException, InputRequiredException {
        String question = chatService.queryOneQuest(modelId,req);
        return ResultUtils.success(question,"ok");
    }
    @GetMapping(value = "/replace")
    @ApiOperation(value = "更新图片链接",hidden = true)
    public BaseResponse<String> replace(@RequestParam String imgUrl, HttpServletRequest req) throws IOException {
        String ret = chatService.replaceImgUrl(imgUrl,req);
        return ResultUtils.success(ret,"ok");
    }

    /**
     * 下列接口已废弃（接手时，可以无需理会）
     */

    @PostMapping(value = "/chat-test")
    @ApiOperation(value = "开启聊天(测试版)",hidden = true)
    @Deprecated
    public SseEmitter sse(@RequestParam String prompt,@RequestParam String modelId,HttpServletRequest req) {
        SseEmitter emitter = new SseEmitter();
        emitter = chatService.sseChat(prompt,modelId,req,emitter);
        return emitter;
    }
    @PostMapping(value = "/chat-deprecated")
    @ApiOperation(value = "开启聊天",hidden = true)
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @Deprecated
    public Flux<String> baiLianChat(
            @RequestBody ChatRequest param,
            HttpServletResponse res,HttpServletRequest req) throws Exception, ApiException, NoApiKeyException, InputRequiredException {
        return chatService.chat(param, res, req);
    }
}
