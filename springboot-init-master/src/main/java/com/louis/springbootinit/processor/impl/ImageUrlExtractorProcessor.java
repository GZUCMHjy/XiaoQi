package com.louis.springbootinit.processor.impl;

import com.louis.springbootinit.processor.ChatOutputProcessor;
import com.louis.springbootinit.processor.ChatProcessingContext;
import com.louis.springbootinit.utils.InciseStrUtils;

/**
 * Extracts image URL if present in markdown-like pattern and stores in context.
 */
public class ImageUrlExtractorProcessor implements ChatOutputProcessor {

    private ChatOutputProcessor next;

    @Override
    public String process(String input, ChatProcessingContext context) {
        String aggregated = context.getAggregatedText().toString();
        String url = InciseStrUtils.extractUrl(aggregated);
        if (url != null) {
            context.setLastImageUrl(url);
        }
        return next == null ? input : next.process(input, context);
    }

    @Override
    public void setNext(ChatOutputProcessor next) {
        this.next = next;
    }
}

