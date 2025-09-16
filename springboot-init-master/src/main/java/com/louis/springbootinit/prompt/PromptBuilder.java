package com.louis.springbootinit.prompt;

import com.louis.springbootinit.model.dto.request.UserAsk;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Template builder for composing prompts from history and the current message.
 */
public abstract class PromptBuilder {

    public final String build(ArrayDeque<UserAsk> history, String message) {
        String historyBlock = formatHistory(history);
        String current = formatCurrent(message);
        return assemble(historyBlock, current);
    }

    protected String formatHistory(ArrayDeque<UserAsk> history) {
        if (history == null || history.isEmpty()) {
            return "";
        }
        return history.stream()
                .filter(Objects::nonNull)
                .map(it -> String.format("[%s] %s", it.getRole(), it.getContent()))
                .collect(Collectors.joining("\n"));
    }

    protected String formatCurrent(String message) {
        return message == null ? "" : message;
    }

    protected abstract String assemble(String history, String current);
}

