package com.louis.springbootinit.strategy;

import com.louis.springbootinit.model.vo.ModelCredentials;

/**
 * Strategy for resolving model credentials by model id or other attributes.
 */
public interface ModelSelectionStrategy {

    /**
     * Resolve credentials for the given model id.
     *
     * @param modelId model identifier from client request
     * @return credentials including appKey and apiKey
     */
    ModelCredentials resolveByModelId(String modelId);
}

