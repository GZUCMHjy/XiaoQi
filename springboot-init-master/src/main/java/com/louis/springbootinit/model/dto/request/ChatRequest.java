package com.louis.springbootinit.model.dto.request;

import lombok.Data;

import java.util.ArrayDeque;
import java.util.List;

/**
 * @author louis
 * @version 1.0
 * @date 2024/5/25 23:31
 */
@Data
public class ChatRequest {
    /**
     * 用户输入的文本数据
     */
    private String message;
    /**
     * 记录当前对话窗口的历史内容
     */
    private ArrayDeque<UserAsk> history;
    /**
     * 当前使用的模型id
     */
    private String modelId;
    /**
     * 当前对话窗口的uuid
     */
    private String uuid;
    /**
     * 多模态-多组上传图片数据
     */
    private List<String> imgUrls;
    /**
     * 多模态-多组上传文件数据
     */
    private List<String> fileIds;
}
