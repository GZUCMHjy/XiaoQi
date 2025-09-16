package com.louis.springbootinit.processor;

/**
 * Chain of Responsibility node for processing chat outputs incrementally.
 */
public interface ChatOutputProcessor {
    /**
     * @param input new incremental text
     * @param context mutable processing context
     * @return processed text to emit; can be same as input
     */
    String process(String input, ChatProcessingContext context);

    void setNext(ChatOutputProcessor next);
}

