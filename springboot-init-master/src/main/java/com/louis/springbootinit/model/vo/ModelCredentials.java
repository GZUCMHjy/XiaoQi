package com.louis.springbootinit.model.vo;

import lombok.Data;

/**
 * Value object that holds model credentials and metadata for a selected model.
 */
@Data
public class ModelCredentials {
    private String modelId;
    private String modelName;
    private String appKey;
    private String apiKey;
}

