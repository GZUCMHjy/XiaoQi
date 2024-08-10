package com.louis.springbootinit.model.dto;

import lombok.Data;

/**
 * @author louis
 * @version 1.0
 * @date 2024/5/31 13:09
 */
@Data
public class Conversation {
    private String title;
    private String uuid;
    private String icon;
    private String system;
    private String modelId;
}
