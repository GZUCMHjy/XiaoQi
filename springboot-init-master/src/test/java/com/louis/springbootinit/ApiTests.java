package com.louis.springbootinit;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author louis
 * @version 1.0
 * @date 2024/6/1 15:04
 */
@SpringBootTest
public class ApiTests {

    @Value("${weather.privateKey}")
    private String weatherPK;
    @Test
    public void queryWeatherByCity(){
        HttpUtil.get("https://api.seniverse.com/v3/weather/now.json?key="+ weatherPK + "&location=guangzhou&language=zh-Hans&unit=c");
    }

    @Test
    public void queryCityByIp(){

    }
}
