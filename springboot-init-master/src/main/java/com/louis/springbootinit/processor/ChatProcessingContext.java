package com.louis.springbootinit.processor;

import lombok.Data;

/**
 * Context shared across processors in the chain.
 */
@Data
public class ChatProcessingContext {
    private StringBuilder aggregatedText = new StringBuilder();
    private String lastImageUrl;
}

