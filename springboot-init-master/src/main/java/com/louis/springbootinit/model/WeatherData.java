package com.louis.springbootinit.model;

import lombok.Data;

/**
 * @author louis
 * @version 1.0
 * @date 2024/6/1 16:19
 */
@Data
public class WeatherData {
    private String address;
    private String cityCode;
    private String weather;
    private String windDirection;
    private String windPower;
    private String humidity;
    private String reportTime;
}
