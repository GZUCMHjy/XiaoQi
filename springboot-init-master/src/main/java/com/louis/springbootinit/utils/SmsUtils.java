package com.louis.springbootinit.utils;

import cn.hutool.core.date.DateUtil;
import com.louis.springbootinit.common.ErrorCode;
import com.louis.springbootinit.exception.BusinessException;
import com.louis.springbootinit.model.bo.SendMessageCacheBO;
import com.louis.springbootinit.model.enums.SmsTypeEnum;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Namego
 * @date 2024/5/25 18:04
 */
@Component
public class SmsUtils {

    /**
     * 手机号登录验证码缓存
     */
    public static final Map<String, SendMessageCacheBO> SMS_LOGIN_CACHE_BO_MAP = new HashMap<>();

    /**
     * 手机号注册验证码缓存
     */
    public static final Map<String, SendMessageCacheBO> SMS_REGISTER_CACHE_BO_MAP = new HashMap<>();

    /**
     * 手机号修改密码缓存
     */
    public static final Map<String,SendMessageCacheBO> SMS_UPDATE_PASSWORD_BO_MAP = new HashMap<>();

    /**
    * 手机号验证码创建时间缓存
     */
    public static final Map<String,Date> SMS_CREATE_TIME_CACHE_BO_MAP = new HashMap<>();

    /**
     * 手机验证码长度(4-6)
     */
    public static final int CODE_LENGTH = 6;

    /**
     * 验证码有效时间，五分钟
     */
    public static final int CODE_ACTIVE_TIME = 5;

    /**
     * 验证码间隔时间
     */
    public static final int INTERVAL_TIME = 1;

    /**
     * secretId
     */
    @Value("${sms.secretId}")
    private String secretId;

    /**
     * secretKey
     */
    @Value("${sms.secretKey}")
    private String secretKey;

    /**
     * 签名
     */
    @Value("${sms.signName}")
    private String signName;

    /**
     * sdkAppId
     */
    @Value("${sms.sdkAppId}")
    private String sdkAppId;

    public boolean sendCode(SmsTypeEnum smsTypeEnum, String phoneNumbers) {
        try {
            Date time = SMS_CREATE_TIME_CACHE_BO_MAP.getOrDefault(phoneNumbers, null);
            if(Objects.nonNull(time)){
                if (DateUtil.offsetMinute(time, INTERVAL_TIME).after(new Date())) {
                    throw new BusinessException(ErrorCode.FREQUENTLY_ERROR,INTERVAL_TIME + "分钟内请勿多次发送验证码，请稍后重试");
                }
            }

            Credential cred = new Credential(secretId, secretKey);

            // 实例化一个http选项，可选，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("POST");
            httpProfile.setConnTimeout(30);
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);

            SendSmsRequest req = new SendSmsRequest();

            req.setSmsSdkAppId(sdkAppId);
            req.setSignName(signName);

            String templateId = smsTypeEnum.getTemplateId();
            req.setTemplateId(templateId);
            String[] templateParamSet;
            String code = this.generateVerificationCode(CODE_LENGTH);
            if (smsTypeEnum.isLimitedTime()) {
                templateParamSet = new String[]{code, String.valueOf(smsTypeEnum.getLimitedTimeDuration())};
            } else {
                templateParamSet = new String[]{code};
            }
            req.setTemplateParamSet(templateParamSet);


            String[] phoneNumberSet = {phoneNumbers};
            req.setPhoneNumberSet(phoneNumberSet);

            SendSmsResponse res = client.SendSms(req);

            SendMessageCacheBO sendMessageCacheBO = new SendMessageCacheBO();
            sendMessageCacheBO.setCode(code);
            Date now = new Date();
            sendMessageCacheBO.setCreateTime(now);
            sendMessageCacheBO.setTo(phoneNumbers);
            if(smsTypeEnum.equals(SmsTypeEnum.LOGIN)){
                SMS_LOGIN_CACHE_BO_MAP.put(phoneNumbers,sendMessageCacheBO);
            }else if(smsTypeEnum.equals(SmsTypeEnum.REGISTER)){
                SMS_REGISTER_CACHE_BO_MAP.put(phoneNumbers,sendMessageCacheBO);
            }else if(smsTypeEnum.equals(SmsTypeEnum.UPDATE_PASSWORD)){
                SMS_UPDATE_PASSWORD_BO_MAP.put(phoneNumbers,sendMessageCacheBO);
            }
            SMS_CREATE_TIME_CACHE_BO_MAP.put(phoneNumbers, now);
            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(res));

        } catch (
                TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 生成指定位数的数字验证码
     */
    public String generateVerificationCode(int length) {
        // 生成随机数字串，转换为字符串
        return ThreadLocalRandom.current()
                .ints(length, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }


    /**
     * 校验验证码
     */
    public boolean validCode(SmsTypeEnum smsTypeEnum,String phoneNumbers,String code){
        Map<String,SendMessageCacheBO> map;

        if(smsTypeEnum.equals(SmsTypeEnum.LOGIN)){
            map = SMS_LOGIN_CACHE_BO_MAP;
        }else if(smsTypeEnum.equals(SmsTypeEnum.REGISTER)){
            map = SMS_REGISTER_CACHE_BO_MAP;
        }else if(smsTypeEnum.equals(SmsTypeEnum.UPDATE_PASSWORD)){
            map = SMS_UPDATE_PASSWORD_BO_MAP;
        }else{
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"没有该手机验证码类型");
        }
        SendMessageCacheBO sendMessageCacheBO = map.getOrDefault(phoneNumbers,null);
        if(Objects.isNull(sendMessageCacheBO)){
            return false;
        }
        Date createTime = sendMessageCacheBO.getCreateTime();
        if(smsTypeEnum.isLimitedTime()) {
            if (DateUtil.offsetMinute(createTime, smsTypeEnum.getLimitedTimeDuration()).before(new Date())) {
                return false;
            }
        }
        return sendMessageCacheBO.getCode().equals(code);
    }
}
