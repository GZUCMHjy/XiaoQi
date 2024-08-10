package com.louis.springbootinit.constant;

/**
 * 用户常量
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    /**
     * 用户登录token
     */
    String USER_LOGIN_TOKEN = "user_token";
    //  region 权限

    String SYS_ROLE = "sys";
    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";

    // endregion

    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "123456";

    /**
     * 密码最少长度
     */
    Integer PASSWORD_LENGTH_LIMIT = 6;
}
