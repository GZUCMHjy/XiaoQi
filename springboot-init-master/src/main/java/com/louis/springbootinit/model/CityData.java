package com.louis.springbootinit.model;

import cn.hutool.core.date.DateTime;
import lombok.Data;

/**
 * @author louis
 * @version 1.0
 * @date 2024/6/1 23:00
 */
@Data
public class CityData {
    private String  ip;
    private String  province;
    private String  provinceId;
    private String  city;
    private String  cityId;
    private String  isp;
    private String  desc;
}
