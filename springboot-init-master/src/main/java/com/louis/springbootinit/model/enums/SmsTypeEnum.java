package com.louis.springbootinit.model.enums;


/**
 * @author namego
 * 短信类型
 */
public enum SmsTypeEnum {
    LOGIN(1, "登录验证码", "2167857", true, 5),
    REGISTER(2, "注册验证码", "2167853", false, -1),
    UPDATE_PASSWORD(3,"修改密码验证码","2167857",true,5);

    private int id;
    private String type;
    private String templateId;
    private boolean isLimitedTime;
    private Integer limitedTimeDuration;

    SmsTypeEnum(int id, String type, String templateId, boolean isLimitedTime, Integer limitedTimeDuration) {
        this.id = id;
        this.type = type;
        this.templateId = templateId;
        this.isLimitedTime = isLimitedTime;
        this.limitedTimeDuration = limitedTimeDuration;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTemplateId() {
        return templateId;
    }

    public boolean isLimitedTime() {
        return isLimitedTime;
    }

    public Integer getLimitedTimeDuration() {
        return limitedTimeDuration;
    }

}
