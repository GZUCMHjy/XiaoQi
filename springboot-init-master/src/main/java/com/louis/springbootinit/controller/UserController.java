package com.louis.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.louis.springbootinit.annotation.AuthCheck;
import com.louis.springbootinit.common.*;
import com.louis.springbootinit.constant.UserConstant;
import com.louis.springbootinit.exception.BusinessException;
import com.louis.springbootinit.exception.ThrowUtils;
import com.louis.springbootinit.model.dto.user.*;
import com.louis.springbootinit.model.entity.User;
import com.louis.springbootinit.model.enums.SmsTypeEnum;
import com.louis.springbootinit.model.vo.LoginUserVO;
import com.louis.springbootinit.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.louis.springbootinit.utils.EmailUtils;
import com.louis.springbootinit.utils.EnumUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.louis.springbootinit.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/api/user")
@Api(value = "用户模块", tags = "用户模块")
@Validated
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.prefix}")
    private String imagePrefix;

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    @ApiOperation(value = "更新个人信息",hidden = true)
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
                                              HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 用户手机号登录
     *
     * @param reqDTO
     * @param request
     * @return
     */
    @PostMapping("/no/secret/login")
    @ApiOperation(value = "免密登录")
    public BaseResponse<LoginUserVO> noPasswordLogin(@RequestBody @Valid UserNoSecretLoginReqDTO reqDTO, HttpServletRequest request) {
        LoginUserVO loginUserVO = userService.userNoSecretLogin(reqDTO, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 发送验证码
     * @param reqDTO
     * @return
     */
    @PostMapping("/code/send")
    @ApiOperation(value = "发送验证码")
    public BaseResponse getEmailCode(@RequestBody @Valid UserCodeSendReqDTO reqDTO) {
        if (EmailUtils.isValidEmail(reqDTO.getUserAccount())) {
            userService.sendEmailCode(reqDTO.getUserAccount());
        }else{
            if (reqDTO.getUserAccount().length() != 11) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户格式错误");
            }
            SmsTypeEnum smsTypeEnum = EnumUtils.getById(SmsTypeEnum.class,reqDTO.getOperate());
            userService.sendPhoneCode(smsTypeEnum,reqDTO.getUserAccount());
        }
        return ResultUtils.success(true);
    }

    /**
     * 修改密码
     * @param reqDTO
     * @return
     */
    @PostMapping("/password/update")
    @ApiOperation(value = "修改密码")
    public BaseResponse updatePassword(@RequestBody @Valid UserUpdatePasswordReqDTO reqDTO) {
        userService.updatePassword(reqDTO);
        return ResultUtils.success(true);
    }

    /**
     * 免密修改密码
     * @param reqDTO
     * @return
     */
    @PostMapping("/no/secret/password/update")
    @ApiOperation(value = "免密修改密码")
    public BaseResponse noSecretPasswordUpdate(@RequestBody @Valid UserNoSecretUpdatePasswordReqDTO reqDTO) {
        userService.noSecretPasswordUpdate(reqDTO);
        return ResultUtils.success(true);
    }

    /**
     * 免密修改密码
     * @param req
     * @param modelId
     * @return
     */
    @GetMapping("/isAuth")
    @ApiOperation(value = "免密修改密码")
    public BaseResponse isAuth(HttpServletRequest req,String modelId) {
        User user = (User)req.getSession().getAttribute(USER_LOGIN_STATE);
        return ResultUtils.success(userService.isAuthByModel(user.getId().toString(),modelId));
    }

    /**
     * 用户上传头像照片
     * @param file
     * @return
     */
    @PostMapping("/uploadAvatar")
    public BaseResponse<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        try {
            // 保存文件到指定目录

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "原始文件名为空");
            }
            String fileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String[] filenameParts = originalFilename.split("\\.");
            if (filenameParts.length < 2) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件名格式不正确");
            }
            String fileNameSuffix = "." + filenameParts[1];
            String fileName = fileNamePrefix + fileNameSuffix;
            Path path = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), path);
            // 生成文件的访问URL
            String fileUrl = imagePrefix +"/images/" + fileName;
            return ResultUtils.success(fileUrl,"ok");
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
        }
    }


}
