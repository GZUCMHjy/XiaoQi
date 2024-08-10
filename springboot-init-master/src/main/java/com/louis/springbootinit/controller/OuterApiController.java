package com.louis.springbootinit.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.louis.springbootinit.annotation.AuthCheck;
import com.louis.springbootinit.common.BaseResponse;
import com.louis.springbootinit.common.ErrorCode;
import com.louis.springbootinit.common.ResultUtils;
import com.louis.springbootinit.constant.UserConstant;
import com.louis.springbootinit.exception.BusinessException;
import com.louis.springbootinit.model.*;
import com.louis.springbootinit.model.entity.User;
import com.louis.springbootinit.model.enums.ModelEnums;
import com.louis.springbootinit.utils.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.louis.springbootinit.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author louis
 * @version 1.0
 * @date 2024/6/1 15:27
 * 该接口层停用
 */
@RestController
@RequestMapping("/api/outer")
@Slf4j
@Api(value = "第三方接口模块",tags = "第三方接口模块")
@Deprecated
public class OuterApiController {



    @Value("${roll_api.app_id}")
    private String appId;
    @Value("${roll_api.app_secret}")
    private String appPK;


    private final static String REGIONPREFIX = "https://www.mxnzp.com/api/ip/aim_ip?ip=";
    private final static String NEWSPREFIX ="https://www.mxnzp.com/api/news/list/v2?typeId=";
    private final static String WEATHERPREFIX = "https://www.mxnzp.com/api/weather/current/";
    private static final Map<String, Integer> modelIdMap = new HashMap<>();
    // 允许重试次数
    private final static int MAXRETRIES = 3;

    // 静态代码块保证只初始化一次
    static {
        modelIdMap.put(ModelEnums.MODEL2.getModelId(), 532);
        modelIdMap.put(ModelEnums.MODEL5.getModelId(), 533);
        modelIdMap.put(ModelEnums.MODEL4.getModelId(), 534);
        modelIdMap.put(ModelEnums.MODEL6.getModelId(), 535);
        modelIdMap.put(ModelEnums.MODEL3.getModelId(), 536);
        modelIdMap.put(ModelEnums.MODEL1.getModelId(), 537);
    }


    /**
     * 获取天气信息
     * @param req
     * @return
     */
    @PostMapping("/weather")
    @ApiOperation(value = "获取天气信息")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<WeatherData> queryWeather(HttpServletRequest req) {
        // 获取客户端请求的ip地址
        String ipAddr = IpUtils.getIpAddr(req);
        log.info("该请求的地址是：{}", ipAddr);
        // 根据IP获取地区信息
        String regionJson = getRegion(ipAddr);
        JSONUtil.toBean(regionJson,CityApiResult.class);
        WeatherData weather = getWeather(regionJson);
        log.info("==================1"+ weather);
        return ResultUtils.success(weather);
    }

    /**
     * 获取每日新闻列表
     * @param typeId
     * @return
     */
    @GetMapping("/new")
    @ApiOperation(value = "获取每日新闻列表")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<?> queryNews(int typeId) {
        typeId = getMappedTypeId(typeId);
        String url = NEWSPREFIX+typeId+"&page=1&app_id=" + appId + "&app_secret=" + appPK;
        List<NewsData> news = getNews(url);
        return ResultUtils.success(news);
    }
    public String getRegion(String ipAddr){
        String url = REGIONPREFIX + ipAddr + "&app_id=" + appId + "&app_secret=" + appPK;
        String regionJson = null;
        // 重试机制
        int retryCount = 0;
        while((regionJson == null || regionJson.isEmpty()) && retryCount < MAXRETRIES){
                try{
                    regionJson = HttpUtil.get(url);
                    retryCount++;
                    if(regionJson.contains("fail")){
                        throw new BusinessException(ErrorCode.OPERATION_ERROR,"获取失败,请重试");
                    }
                }catch (Exception e){
                    log.error("Failed to get region data, attempt " + retryCount, e);
                }
                if (regionJson == null || regionJson.isEmpty()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        log.error("Thread interrupted during sleep", e);
                        Thread.currentThread().interrupt();
                    }
                }
        }
        if (regionJson == null || regionJson.isEmpty()) {
            log.error("Failed to get region data after " + MAXRETRIES + " attempts");
            return null;
        }
        return regionJson;
    }

    public List<NewsData> getNews(String url){
        String newJsonStr = HttpUtil.get(url);
        int retryCount = 0;
        // 重试机制
        while((newJsonStr == null || newJsonStr.isEmpty()) && retryCount < MAXRETRIES){
            try{
                newJsonStr = HttpUtil.get(url);
                retryCount++;
            }catch (Exception e){
                log.error("Failed to get weather data, attempt " + retryCount, e);
            }
            if (newJsonStr == null || newJsonStr.isEmpty()) {
                try {
                    Thread.sleep(1000); // wait before retrying
                } catch (InterruptedException e) {
                    log.error("Thread interrupted during sleep", e);
                    Thread.currentThread().interrupt(); // reset interrupt status
                }
            }
        }

        if (newJsonStr == null || newJsonStr.isEmpty()) {
            log.error("Failed to get news data after " + MAXRETRIES + " attempts");
            return null;
        }
        NewsResults bean = JSONUtil.toBean(newJsonStr, NewsResults.class);
        List<NewsData> data = bean.getData();
        return data;
    }
    /**
     * 获取天气信息
     * @param regionJson
     * @return
     */
    public WeatherData getWeather(String regionJson){
        CityResults bean = JSONUtil.toBean(regionJson, CityResults.class);
        CityData data = bean.getData();
        String district = data.getCity();
        String weatherJson = null;
        int retryCount = 0;
        while ((weatherJson == null || weatherJson.isEmpty()) && retryCount < MAXRETRIES) {
            try {
                String url = WEATHERPREFIX + district + "?app_id=" + appId + "&app_secret=" + appPK;
                log.info("URL地址："+url);
                weatherJson = HttpUtil.get(url);
                retryCount++;
            } catch (Exception e) {
                log.error("Failed to get weather data, attempt " + retryCount, e);
            }
            if (weatherJson == null || weatherJson.isEmpty()) {
                try {
                    log.info("我要休息，休息1秒");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("Thread interrupted during sleep", e);
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (weatherJson == null || weatherJson.isEmpty()) {
            log.error("Failed to get weather data after " + MAXRETRIES + " attempts");
            return null; // or handle it in a way appropriate to your application
        }
        WeatherResults weatherResults;
        try {
            weatherResults = JSONUtil.toBean(weatherJson, WeatherResults.class);
            log.info("======================4" + weatherResults.getData());
        } catch (Exception e) {
            log.error("Failed to parse weather data", e);
            return null; // or handle it in a way appropriate to your application
        }

        return weatherResults.getData();
    }

    /**
     * 获取新闻类型id
     * @param typeId
     * @return
     * @throws BusinessException
     */
    public static int getMappedTypeId(int typeId) throws BusinessException {
        Integer mappedTypeId = modelIdMap.get(String.valueOf(typeId));
        if (mappedTypeId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return mappedTypeId;
    }

    /**
     * 获取新闻标签列表
     * @return
     */
    @GetMapping("/newTags")
    @Deprecated
    @ApiOperation(value = "获取新闻标签(类型)列表",hidden = true)
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<?> queryNewsTag() {
        // 标签列表
        String newJsonStr = HttpUtil.get("https://www.mxnzp.com/api/news/types/v2?app_id=" + appId + "&app_secret=" + appPK);
        NewsTagResults bean = JSONUtil.toBean(newJsonStr, NewsTagResults.class);
        List<NewsTagData> data = bean.getData();
        return ResultUtils.success(data);
    }

    /**
     * 获取天气信息（测试版）
     * @param ip
     * @return
     */
    @GetMapping("/weather-test")
    @ApiOperation(value = "获取天气信息（测试版）")
    @Deprecated
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<WeatherData> queryWeatherByIP(String ip) {
        // 获取ip对应的地区信息
        String regionJson = HttpUtil.get(REGIONPREFIX + ip + "&app_id=" + appId + "&app_secret=" + appPK);
        // 添加重试机制(直到成功返回)
        while(regionJson == null || "".equals(regionJson) || regionJson.length() == 0){
            regionJson = HttpUtil.get(REGIONPREFIX + ip + "&app_id=" + appId + "&app_secret=" + appPK);
        }
        if(regionJson.contains("fail")){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"获取失败,请重试");
        }
        WeatherData weather = getWeather(regionJson);
        log.info("================1" + weather);
        while(weather == null){
            weather = getWeather(regionJson);
        }
        log.info("================2" + weather);
        return ResultUtils.success(weather);
    }


}
