package com.louis.springbootinit;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import io.reactivex.Flowable;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;


//导入可选配置类
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;


// 导入对应SMS模块的client
import com.tencentcloudapi.sms.v20210111.SmsClient;


// 导入要请求接口对应的request response类
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;


import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 主类测试
 */
@SpringBootTest
class MainApplicationTests {
    @Value("${email.sender.address}")
    private String senderAddress;

    @Value("${email.sender.password}")
    private String senderPassword;

    @Value("${email.sender.name}")
    private String senderName;

    // 已删测试
    @Test
    public void sendEmail() {
        String toEmail = "947441054@qq.com";
        String verificationCode = this.generateVerificationCode(6);

        HtmlEmail send = new HtmlEmail();//创建一个HtmlEmail实例对象
        try {
            send.setHostName("smtp.qq.com");
            send.setAuthentication(senderAddress, senderPassword); //第一个参数是发送者的QQEamil邮箱   第二个参数是刚刚获取的授权码

            send.setFrom(senderAddress, senderName);
            send.setSmtpPort(465);    //端口号 可以不开
            send.setSSLOnConnect(true); //开启SSL加密
            send.setCharset("utf-8");
            send.addTo(toEmail);  //设置收件人    email为你要发送给谁的邮箱账户
            send.setMsg("<div style='font-size: 16px;'>【广东省中医院】\"验证码\" <font color='blue' style='text-decoration: underline;font-size: 16px'>" + verificationCode + "</font> 用于省中医大模型账号注册，5分钟内有效，请勿泄露和转发。如非本人操作，请忽略此邮箱。</div>");
            send.setSubject("广东省中医院"); //邮箱标题
            send.send();  //发送
        } catch (EmailException e) {
            e.printStackTrace();
        }
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

    // 已删测试
//    @Test
//    public void callAgentApp()
//            throws ApiException, NoApiKeyException, InputRequiredException {
//        //private  String baiLianApiKey;
//        ApplicationParam param = ApplicationParam.builder()
//                .apiKey("sk-d3e9c210958d426faceda15da1e408f8")
//                .appId("1f2642f8b00149e9960e6a0433575c62")
//                .prompt("如何做炒西红柿鸡蛋？")
//                .topP(0.2)
//                .build();
//
//        Application application = new Application();
////        ApplicationResult result = application.call(param);
////
////        System.out.printf("requestId: %s, text: %s, finishReason: %s\n",
////                result.getRequestId(), result.getOutput().getText(), result.getOutput().getFinishReason());
//        Flowable<ApplicationResult> result = application.streamCall(param);
//        result.blockingForEach(data -> {
//            System.out.printf("requestId: %s, text: %s, finishReason: %s\n",
//                    data.getRequestId(), data.getOutput().getText(), data.getOutput().getFinishReason());
//
//        });
//    }

    @Test
    public void sendSms(){
        try {
            /* 必要步骤：
             * 实例化一个认证对象，入参需要传入腾讯云账户密钥对secretId，secretKey。
             * 这里采用的是从环境变量读取的方式，需要在环境变量中先设置这两个值。
             * 您也可以直接在代码中写死密钥对，但是小心不要将代码复制、上传或者分享给他人，
             * 以免泄露密钥对危及您的财产安全。
             * SecretId、SecretKey 查询: https://console.cloud.tencent.com/cam/capi */
            Credential cred = new Credential("AKIDRgiguNlFdvNoCkMvcGqV7eCiDd8iZPQX", "P1U3fPJC1ythQ6hCMTYj0EWmG508D7tm");


            // 实例化一个http选项，可选，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            // 设置代理（无需要直接忽略）
            // httpProfile.setProxyHost("真实代理ip");
            // httpProfile.setProxyPort(真实代理端口);
            /* SDK默认使用POST方法。
             * 如果您一定要使用GET方法，可以在这里设置。GET方法无法处理一些较大的请求 */
            httpProfile.setReqMethod("POST");
            /* SDK有默认的超时时间，非必要请不要进行调整
             * 如有需要请在代码中查阅以获取最新的默认值 */
            httpProfile.setConnTimeout(30);
            /* 指定接入地域域名，默认就近地域接入域名为 sms.tencentcloudapi.com ，也支持指定地域域名访问，例如广州地域的域名为 sms.ap-guangzhou.tencentcloudapi.com */
            httpProfile.setEndpoint("sms.tencentcloudapi.com");


            /* 非必要步骤:
             * 实例化一个客户端配置对象，可以指定超时时间等配置 */
            ClientProfile clientProfile = new ClientProfile();
            /* SDK默认用TC3-HMAC-SHA256进行签名
             * 非必要请不要修改这个字段 */
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            /* 实例化要请求产品(以sms为例)的client对象
             * 第二个参数是地域信息，可以直接填写字符串ap-guangzhou，支持的地域列表参考 https://cloud.tencent.com/document/api/382/52071#.E5.9C.B0.E5.9F.9F.E5.88.97.E8.A1.A8 */
            SmsClient client = new SmsClient(cred, "ap-guangzhou",clientProfile);
            /* 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
             * 您可以直接查询SDK源码确定接口有哪些属性可以设置
             * 属性可能是基本类型，也可能引用了另一个数据结构
             * 推荐使用IDE进行开发，可以方便的跳转查阅各个接口和数据结构的文档说明 */
            SendSmsRequest req = new SendSmsRequest();


            String sdkAppId = "1400913668";
            req.setSmsSdkAppId(sdkAppId);


            /* 短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名 */
            // 签名信息可前往 [国内短信](https://console.cloud.tencent.com/smsv2/csms-sign) 或 [国际/港澳台短信](https://console.cloud.tencent.com/smsv2/isms-sign) 的签名管理查看
            String signName = "腾云悦智";
            req.setSignName(signName);


            /* 模板 ID: 必须填写已审核通过的模板 ID */
            // 模板 ID 可前往 [国内短信](https://console.cloud.tencent.com/smsv2/csms-template) 或 [国际/港澳台短信](https://console.cloud.tencent.com/smsv2/isms-template) 的正文模板管理查看
            String templateId = "2167857";
            req.setTemplateId(templateId);


            /* 模板参数: 模板参数的个数需要与 TemplateId 对应模板的变量个数保持一致，若无模板参数，则设置为空 */
            String[] templateParamSet = {"1234","5"};
            req.setTemplateParamSet(templateParamSet);


            String[] phoneNumberSet = {"+8618813874735"};
            req.setPhoneNumberSet(phoneNumberSet);

            SendSmsResponse res = client.SendSms(req);


            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(res));


            // 也可以取出单个值，您可以通过官网接口文档或跳转到response对象的定义处查看返回字段的定义
            // System.out.println(res.getRequestId());


            /* 当出现以下错误码时，快速解决方案参考
             * [FailedOperation.SignatureIncorrectOrUnapproved](https://cloud.tencent.com/document/product/382/9558#.E7.9F.AD.E4.BF.A1.E5.8F.91.E9.80.81.E6.8F.90.E7.A4.BA.EF.BC.9Afailedoperation.signatureincorrectorunapproved-.E5.A6.82.E4.BD.95.E5.A4.84.E7.90.86.EF.BC.9F)
             * [FailedOperation.TemplateIncorrectOrUnapproved](https://cloud.tencent.com/document/product/382/9558#.E7.9F.AD.E4.BF.A1.E5.8F.91.E9.80.81.E6.8F.90.E7.A4.BA.EF.BC.9Afailedoperation.templateincorrectorunapproved-.E5.A6.82.E4.BD.95.E5.A4.84.E7.90.86.EF.BC.9F)
             * [UnauthorizedOperation.SmsSdkAppIdVerifyFail](https://cloud.tencent.com/document/product/382/9558#.E7.9F.AD.E4.BF.A1.E5.8F.91.E9.80.81.E6.8F.90.E7.A4.BA.EF.BC.9Aunauthorizedoperation.smssdkappidverifyfail-.E5.A6.82.E4.BD.95.E5.A4.84.E7.90.86.EF.BC.9F)
             * [UnsupportedOperation.ContainDomesticAndInternationalPhoneNumber](https://cloud.tencent.com/document/product/382/9558#.E7.9F.AD.E4.BF.A1.E5.8F.91.E9.80.81.E6.8F.90.E7.A4.BA.EF.BC.9Aunsupportedoperation.containdomesticandinternationalphonenumber-.E5.A6.82.E4.BD.95.E5.A4.84.E7.90.86.EF.BC.9F)
             * 更多错误，可咨询[腾讯云助手](https://tccc.qcloud.com/web/im/index.html#/chat?webAppId=8fa15978f85cb41f7e2ea36920cb3ae1&title=Sms)
             */


        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }

    }
}
