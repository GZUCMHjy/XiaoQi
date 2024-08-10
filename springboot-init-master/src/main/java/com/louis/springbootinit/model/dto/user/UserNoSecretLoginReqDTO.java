package com.louis.springbootinit.model.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Namego
 * @date 2024/5/25 20:13
 */
@Data
public class UserNoSecretLoginReqDTO {
    @ApiModelProperty(value = "账号", required = true)
    @NotNull(message = "邮号不能为空")
    private String userAccount;


    @ApiModelProperty(value = "验证码",required = true)
    @NotNull(message = "验证码不能为空")
    private String code;
}
