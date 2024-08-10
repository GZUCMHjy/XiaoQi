package com.louis.springbootinit.model;

import cn.hutool.core.date.DateTime;
import lombok.Data;

/**
 * @author louis
 * @version 1.0
 * @date 2024/6/1 23:00
 */
@Data
public class NewsData {
    private String  title;
    private String  imgList;
    private String  source;
    private String  newsId;
    private String digest;
    private DateTime postTime;
    private String videoList;
}
