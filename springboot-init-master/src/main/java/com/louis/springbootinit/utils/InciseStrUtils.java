package com.louis.springbootinit.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author louis
 * @version 1.0
 * @date 2024/7/28 1:27
 * 切割字符串工具
 */
public class InciseStrUtils {
    /**
     * 切割URL
     * @param text
     * @return
     */
    public static String extractUrl(String text) {
        String urlPattern = "!\\[.*?\\]\\((.*?)\\)";
        Pattern pattern = Pattern.compile(urlPattern);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
