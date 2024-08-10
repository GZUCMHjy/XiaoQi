package com.louis.springbootinit.utils;

import cn.hutool.core.date.DateUtil;
import com.louis.springbootinit.common.ErrorCode;
import com.louis.springbootinit.exception.BusinessException;
import com.louis.springbootinit.model.bo.SendMessageCacheBO;
import com.louis.springbootinit.model.dto.request.SmsCodeValidReqDTO;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Namego
 * @date 2024/5/25 18:04
 */
@Component
public class EmailUtils {

    /**
     * 验证码缓存
     */
    public static final Map<String, SendMessageCacheBO> EMAIL_MESSAGE_CACHE_MAP = new HashMap<>();

    /**
     * 验证码长度
     */
    public static final int CODE_LENGTH = 6;

    /**
     * 验证码有效时间，五分钟
     */
    public static final int CODE_ACTIVE_TIME = 5;

    /**
     * 验证码间隔时间
     */
    public static final int EMAIL_INTERVAL_TIME = 1;

    /**
     * 发送邮箱
     */
    @Value("${email.sender.address}")
    private String senderAddress;

    /**
     * 邮箱授权码
     */
    @Value("${email.sender.password}")
    private String senderPassword;

    /**
     * 邮箱用户名
     */
    @Value("${email.sender.name}")
    private String senderName;

    /**
     * 邮箱标题
     */
    @Value("${email.sender.subject}")
    private String subject;

    public boolean sendCode(String toEmail){
        SendMessageCacheBO sendMessageCacheBO = EMAIL_MESSAGE_CACHE_MAP.getOrDefault(toEmail, null);
        if(Objects.nonNull(sendMessageCacheBO)){
            if (DateUtil.offsetMinute(sendMessageCacheBO.getCreateTime(), EMAIL_INTERVAL_TIME).after(new Date())) {
                throw new BusinessException(ErrorCode.FREQUENTLY_ERROR,EMAIL_INTERVAL_TIME + "分钟内请勿多次发送验证码，请稍后重试");
            }
        }

        String code = this.generateVerificationCode(CODE_LENGTH);
        HtmlEmail send = new HtmlEmail();
        try {
            send.setHostName("smtp.qq.com");
            send.setAuthentication(senderAddress, senderPassword);

            send.setFrom(senderAddress, senderName);
            send.setSmtpPort(465);
            send.setSSLOnConnect(true);
            send.setCharset("utf-8");
            send.addTo(toEmail);
            send.setMsg("<div style='font-size: 16px;'>【广东省中医药科学院发展研究中心】验证码: <font color='blue' style='text-decoration: underline;font-size: 16px'>" + code + "</font> 用于\"岐伯曰\"中医药大模型账号注册登录，" + CODE_ACTIVE_TIME + "分钟内有效，请勿泄露和转发。如非本人操作，请忽略此邮件。</div>");
            send.setSubject(subject);
            send.send();//发送
            EMAIL_MESSAGE_CACHE_MAP.put(toEmail,new SendMessageCacheBO(toEmail,code,new Date()));
        } catch (EmailException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"发送验证码失败");
        }
        return true;
    }

    /**
     *生成指定位数的数字验证码
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
    public boolean validCode(SmsCodeValidReqDTO validReqDTO){
        return this.validCode(validReqDTO.getPhoneNumbers(),validReqDTO.getCode());
    }

    /**
     * 校验验证码
     */
    public boolean validCode(String email,String code){
        SendMessageCacheBO sendMessageCacheBO = EMAIL_MESSAGE_CACHE_MAP.getOrDefault(email, null);
        if(Objects.isNull(sendMessageCacheBO)){
            return false;
        }
        Date createTime = sendMessageCacheBO.getCreateTime();
        if (DateUtil.offsetMinute(createTime, CODE_ACTIVE_TIME).before(new Date())) {
            EMAIL_MESSAGE_CACHE_MAP.remove(email);
            return false;
        }
        return sendMessageCacheBO.getCode().equals(code);
    }

    public static boolean isValidEmail(String email) {
        // 邮箱正则表达式验证
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}
