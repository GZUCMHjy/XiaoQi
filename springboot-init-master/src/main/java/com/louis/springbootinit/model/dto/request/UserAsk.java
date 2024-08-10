package com.louis.springbootinit.model.dto.request;

import lombok.Data;

import java.util.List;

/**
 * @author louis
 * @version 1.0
 * @date 2024/5/31 13:49
 */
@Data
public class UserAsk {
    /**
     * 角色
     */
    private String role;
    /**
     * 对话内容
     */
    private String content;
    /**
     * 多模态-多组上传图片数据
     */
    private List<String> imgUrls;
    /**
     * 多模态-多组上传文件数据
     */
    private List<String> fileIds;
}
