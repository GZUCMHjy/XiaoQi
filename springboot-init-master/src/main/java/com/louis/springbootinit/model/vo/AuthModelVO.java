package com.louis.springbootinit.model.vo;

import lombok.Data;

/**
 * @author louis
 * @version 1.0
 * @date 2024/7/10 20:35
 */
@Data
public class AuthModelVO {
    /**
     * 模型id
     */
    private String modelId;
    /**
     * 是否开启使用权限
     */
    private boolean isOpen;
}
