package com.louis.springbootinit.model;

import lombok.Data;

/**
 * @author louis
 * @version 1.0
 * @date 2024/6/1 16:45
 */
@Data
public class WeatherResults {
    private String code;
    private String msg;
    private WeatherData data;
}
