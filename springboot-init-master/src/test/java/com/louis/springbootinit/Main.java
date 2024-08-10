package com.louis.springbootinit;

import com.alibaba.dashscope.app.*;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.louis.springbootinit.utils.FileUtils;
import com.louis.springbootinit.utils.InciseStrUtils;
import io.reactivex.Flowable;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void streamCall() throws NoApiKeyException, InputRequiredException {
        ApplicationParam param = ApplicationParam.builder()
                .apiKey("sk-d3e9c210958d426faceda15da1e408f8")
                .appId("7e128837311143f7bee6d4c5b61124cd")
                .prompt("画一张美女图")
                .incrementalOutput(true)
                .build();

        Application application = new Application();
        Flowable<ApplicationResult> result = application.streamCall(param);
        result.blockingForEach(data -> {
            System.out.printf("requestId: %s, text: %s, finishReason: %s\n",
                    data.getRequestId(), data.getOutput().getText(), data.getOutput().getFinishReason());

        });
    }

    /**
     * 下载文件
     * @param imageUrl
     * @param destinationFile
     * @throws IOException
     */
    public static void downloadImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try (InputStream in = new BufferedInputStream(connection.getInputStream());
             FileOutputStream out = new FileOutputStream(destinationFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } finally {
            connection.disconnect();
        }
    }
    public static String extractUrl(String text) {
        String urlPattern = "!\\[.*?\\]\\((.*?)\\)";
        Pattern pattern = Pattern.compile(urlPattern);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
    public static String extractFilename(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        int questionMarkIndex = url.indexOf('?', lastSlashIndex);
        if (lastSlashIndex >= 0 && questionMarkIndex > lastSlashIndex) {
            return url.substring(lastSlashIndex + 1, questionMarkIndex);
        }
        return null;
    }

    public static void main(String[] args) {
//        try {
//            streamCall();
//        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
//            System.out.printf("Exception: %s", e.getMessage());
//        }
//        System.exit(0);
//        String imageUrl = "https://dashscope-result-sh.oss-cn-shanghai.aliyuncs.com/1d/8a/20240727/29c02cfd/6877b495-e8b9-4d24-8bf7-6a6923690b22-1.png?Expires=1722180537&OSSAccessKeyId=LTAI5tQZd8AEcZX6KZV4G8qL&Signature=qC2PUYXUfB3jp7THvWwjZHL1TB4%3D";
//        String destinationFile = "D:\\\\download\\\\image.png";
//
//        try {
//            downloadImage(imageUrl, destinationFile);
//            System.out.println("Image downloaded successfully.");
//        } catch (IOException e) {
//            System.err.println("Failed to download image: " + e.getMessage());
//        }
//        String resp = "这是我为您绘制的一位美女的形象：\n![](https://dashscope-result-bj.oss-cn-beijing.aliyuncs.com/1d/1b/20240728/c96a9636/599b2ed2-3f7c-4813-9927-53f69512a287-1.png?Expires=1722187403&OSSAccessKeyId=LTAI5tQZd8AEcZX6KZV4G8qL&Signature=eGCBz13Q8q1A3c%2FoPGkK9KFAHtc%3D)";
//        String imageUrl = extractUrl(resp);
//
//        if (imageUrl != null) {
//            System.out.println("Extracted URL: " + imageUrl);
//        } else {
//            System.out.println("No URL found.");
//        }
        Pattern pattern = Pattern.compile("!\\[.*?\\]\\((.*?)\\)");
        Matcher matcher = pattern.matcher("从之前的对话中可以看出，您似乎对最近生成的美男子图像感到不太满意，认为它看起来更像是日本风格而不是中国的。为了帮助您更好地表达您的需求，我们可以尝试描述一下如何展现出“苦笑”的表情，尤其是像日本人那样表现这一表情的方式。\n" +
                "\n" +
                "在日本文化中，“苦笑”通常用来表示一种微妙的情感状态，可能是尴尬、无奈或是某种幽默感。它可以通过以下几种方式来表现：\n" +
                "\n" +
                "1. **嘴角轻微上扬**：不是那种开心的大笑，而是嘴角微微向上翘起，但不露齿。\n" +
                "2. **眼神温和**：眼睛略微眯起，给人一种温柔的感觉，同时带有一点无奈。\n" +
                "3. **眉毛轻轻皱起**：眉头微微皱起，显示出一种轻微的困惑或是不安。\n" +
                "\n" +
                "根据这些描述，我们尝试绘制一个展示这种“苦笑”表情的形象。接下来，我将为您生成一个符合上述特征的人物形象。\n" +
                "这里根据您的要求，我生成了一个展现“苦笑”表情的中国美男子图像，采用了中国传统绘画风格。希望这张图像能够符合您的预期：\n" +
                "![](https://dashscope-result-sh.oss-cn-shanghai.aliyuncs.com/1d/1a/20240728/29c02cfd/8254597b-baf4-49aa-abeb-f9656f5ba1ff-1.png?Expires=1722249818&OSSAccessKeyId=LTAI5tQZd8AEcZX6KZV4G8qL&Signature=mPjS0iMZ%2BlhhQcW1JNtnDQdg%2Fmk%3D)");
        if (matcher.find()) {
            System.out.println(matcher.find());
            String picUrlWithExpired = InciseStrUtils.extractUrl("从之前的对话中可以看出，您似乎对最近生成的美男子图像感到不太满意，认为它看起来更像是日本风格而不是中国的。为了帮助您更好地表达您的需求，我们可以尝试描述一下如何展现出“苦笑”的表情，尤其是像日本人那样表现这一表情的方式。\n" +
                    "\n" +
                    "在日本文化中，“苦笑”通常用来表示一种微妙的情感状态，可能是尴尬、无奈或是某种幽默感。它可以通过以下几种方式来表现：\n" +
                    "\n" +
                    "1. **嘴角轻微上扬**：不是那种开心的大笑，而是嘴角微微向上翘起，但不露齿。\n" +
                    "2. **眼神温和**：眼睛略微眯起，给人一种温柔的感觉，同时带有一点无奈。\n" +
                    "3. **眉毛轻轻皱起**：眉头微微皱起，显示出一种轻微的困惑或是不安。\n" +
                    "\n" +
                    "根据这些描述，我们尝试绘制一个展示这种“苦笑”表情的形象。接下来，我将为您生成一个符合上述特征的人物形象。\n" +
                    "这里根据您的要求，我生成了一个展现“苦笑”表情的中国美男子图像，采用了中国传统绘画风格。希望这张图像能够符合您的预期：\n" +
                    "![](https://dashscope-result-sh.oss-cn-shanghai.aliyuncs.com/1d/1a/20240728/29c02cfd/8254597b-baf4-49aa-abeb-f9656f5ba1ff-1.png?Expires=1722249818&OSSAccessKeyId=LTAI5tQZd8AEcZX6KZV4G8qL&Signature=mPjS0iMZ%2BlhhQcW1JNtnDQdg%2Fmk%3D)");
            String picName = extractFilename(picUrlWithExpired);
            System.out.println(picName);
        }
    }
}