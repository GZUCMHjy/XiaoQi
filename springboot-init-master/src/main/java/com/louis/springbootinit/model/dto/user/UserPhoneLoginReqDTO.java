package com.louis.springbootinit.model.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Namego
 * @date 2024/5/25 20:13
 */
@Data
public class UserPhoneLoginReqDTO {
    @ApiModelProperty(value = "手机号",required = true)
    @NotNull(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号必须为11位")
    @Pattern(regexp = "^\\d{11}$", message = "手机号必须为11位数字")
    private String userAccount;

    @ApiModelProperty(value = "验证码",required = true)
    @NotNull(message = "验证码不能为空")
    private String code;
}
