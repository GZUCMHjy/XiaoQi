package com.louis.springbootinit.prompt;

import org.springframework.stereotype.Component;

/**
 * Simple Chinese prompt builder that appends a hint to use history context.
 */
@Component
public class SimpleChinesePromptBuilder extends PromptBuilder {

    @Override
    protected String assemble(String history, String current) {
        if (history == null || history.isEmpty()) {
            return current;
        }
        return current + "?结合历史对话：" + history;
    }
}

