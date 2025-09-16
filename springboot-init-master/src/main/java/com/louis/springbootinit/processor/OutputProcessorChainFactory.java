package com.louis.springbootinit.processor;

import com.louis.springbootinit.processor.impl.AggregationProcessor;
import com.louis.springbootinit.processor.impl.ImageUrlExtractorProcessor;
import org.springframework.stereotype.Component;

/**
 * Simple factory for building the output processing chain.
 */
@Component
public class OutputProcessorChainFactory {

    public ChatOutputProcessor buildDefaultChain() {
        AggregationProcessor aggregation = new AggregationProcessor();
        ImageUrlExtractorProcessor image = new ImageUrlExtractorProcessor();
        aggregation.setNext(image);
        return aggregation;
    }
}

