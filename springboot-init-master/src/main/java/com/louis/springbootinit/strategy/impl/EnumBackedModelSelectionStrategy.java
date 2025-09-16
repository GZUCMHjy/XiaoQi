package com.louis.springbootinit.strategy.impl;

import com.louis.springbootinit.model.enums.ModelEnums;
import com.louis.springbootinit.model.vo.ModelCredentials;
import com.louis.springbootinit.strategy.ModelSelectionStrategy;
import org.springframework.stereotype.Component;

/**
 * Default strategy that resolves credentials from {@link ModelEnums}.
 */
@Component
public class EnumBackedModelSelectionStrategy implements ModelSelectionStrategy {

    @Override
    public ModelCredentials resolveByModelId(String modelId) {
        ModelEnums model = ModelEnums.getEnumByModelId(modelId);
        if (model == null) {
            return null;
        }
        ModelCredentials credentials = new ModelCredentials();
        credentials.setModelId(model.getModelId());
        credentials.setModelName(model.getModelName());
        credentials.setAppKey(model.getAppKey());
        credentials.setApiKey(model.getApiKey());
        return credentials;
    }
}

