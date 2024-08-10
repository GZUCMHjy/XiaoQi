package com.louis.springbootinit.model;

import lombok.Data;

/**
 * @author louis
 * @version 1.0
 * @date 2024/6/1 15:41
 */
@Data
public class CityApiResult {

    private String status;
    private String country;
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String zip;
    private String lat;
    private String lon;
    private String timezone;
    private String isp;
    private String org;
    private String as;
    private String query;// 发起请求的ip地址

}
