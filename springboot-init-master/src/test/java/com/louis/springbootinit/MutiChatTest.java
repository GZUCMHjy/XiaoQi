package com.louis.springbootinit;

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import com.louis.springbootinit.model.enums.ModelEnums;
import io.reactivex.Flowable;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MutiChatTest {
    /**
     * 图片模型
     * @throws ApiException
     * @throws NoApiKeyException
     * @throws UploadFileException
     */
    public static void simpleMultiModalConversationCall()
            throws ApiException, NoApiKeyException, UploadFileException {
        MultiModalConversation conv = new MultiModalConversation();
        MultiModalMessage userMessage = MultiModalMessage.builder().role(Role.USER.getValue())
                .content(Arrays.asList(Collections.singletonMap("image", "https://dashscope.oss-cn-beijing.aliyuncs.com/images/dog_and_girl.jpeg"),
                        Collections.singletonMap("text", "这是什么?"))).build();
        MultiModalConversationParam param = MultiModalConversationParam.builder()
                .model(MultiModalConversation.Models.QWEN_VL_PLUS)
                .apiKey(ModelEnums.MODEL3.getApiKey())
                .message(userMessage)
                .build();
        // 非流式输出
        MultiModalConversationResult result = conv.call(param);
        System.out.println(result);
    }
    public static void simpleMultiModalConversationCallByFile()
            throws ApiException, NoApiKeyException, UploadFileException {
        MultiModalConversation conv = new MultiModalConversation();
        MultiModalMessage userMessage = MultiModalMessage.builder().role(Role.USER.getValue())
                .content(Arrays.asList(Collections.singletonMap("file", "D:\\szy-model\\springboot-init-master\\sql\\init_table.sql"),
                        Collections.singletonMap("text", "这是什么?"))).build();
        MultiModalConversationParam param = MultiModalConversationParam.builder()
                .model(MultiModalConversation.Models.QWEN_VL_PLUS)
                .apiKey(ModelEnums.MODEL3.getApiKey())
                .message(userMessage)
                .build();
        // 非流式输出
        MultiModalConversationResult result = conv.call(param);
        System.out.println(result);
    }
    public static void main(String[] args) {
        try {
            simpleMultiModalConversationCallByFile();
        } catch (ApiException | NoApiKeyException | UploadFileException e) {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }
}