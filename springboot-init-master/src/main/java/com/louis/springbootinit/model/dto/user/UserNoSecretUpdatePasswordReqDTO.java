package com.louis.springbootinit.model.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Namego
 * @date 2024/5/27 12:40
 */
@Data
public class UserNoSecretUpdatePasswordReqDTO {
    @ApiModelProperty(value = "账号", required = true)
    @NotNull(message = "账号不能为空")
    private String userAccount;

    @ApiModelProperty(value = "验证码",required = true)
    @NotNull(message = "验证码不能为空")
    private String code;

    @ApiModelProperty(value = "密码",required = true)
    @NotNull(message = "密码不能为空")
    @Size(min = 6, message = "密码不能过短")
    private String password;
}
