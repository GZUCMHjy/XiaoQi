package com.louis.springbootinit.model.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Namego
 * @date 2024/5/28 23:09
 */
@Data
public class UserCodeSendReqDTO {
    @ApiModelProperty(value = "邮箱或手机号",required = true)
    @NotNull(message = "账户不能为空")
    private String userAccount;

    @ApiModelProperty(value = "操作 登录传1，注册传2",required = true)
    private Integer operate;
}
