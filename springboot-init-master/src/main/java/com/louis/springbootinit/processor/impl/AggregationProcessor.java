package com.louis.springbootinit.processor.impl;

import com.louis.springbootinit.processor.ChatOutputProcessor;
import com.louis.springbootinit.processor.ChatProcessingContext;

/**
 * Aggregates output text.
 */
public class AggregationProcessor implements ChatOutputProcessor {

    private ChatOutputProcessor next;

    @Override
    public String process(String input, ChatProcessingContext context) {
        if (input != null) {
            context.getAggregatedText().append(input);
        }
        return next == null ? input : next.process(input, context);
    }

    @Override
    public void setNext(ChatOutputProcessor next) {
        this.next = next;
    }
}

