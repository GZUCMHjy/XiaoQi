package com.louis.springbootinit.model.dto.user;

import com.louis.springbootinit.constant.UserConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Namego
 * @date 2024/5/26 22:22
 */
@Data
public class UserUpdatePasswordReqDTO {

    @ApiModelProperty(value = "账号",required = true)
    @NotNull(message = "账号不能为空")
    private String userAccount;

    @ApiModelProperty(value = "原密码",required = true)
    @NotNull(message = "原密码不能为空")
    private String originalPassword;

    @ApiModelProperty(value = "新密码",required = true)
    @NotNull(message = "新密码不能为空")
    @Size(min = 6, message = "密码不能过短")
    private String newPassword;
}
