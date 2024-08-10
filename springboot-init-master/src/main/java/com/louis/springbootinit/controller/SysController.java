package com.louis.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.louis.springbootinit.annotation.AuthCheck;
import com.louis.springbootinit.common.BaseResponse;
import com.louis.springbootinit.common.DeleteRequest;
import com.louis.springbootinit.common.ErrorCode;
import com.louis.springbootinit.common.ResultUtils;
import com.louis.springbootinit.constant.UserConstant;
import com.louis.springbootinit.exception.BusinessException;
import com.louis.springbootinit.exception.ThrowUtils;
import com.louis.springbootinit.model.dto.user.*;
import com.louis.springbootinit.model.entity.User;
import com.louis.springbootinit.model.enums.SmsTypeEnum;
import com.louis.springbootinit.model.enums.UserRoleEnum;
import com.louis.springbootinit.model.vo.*;
import com.louis.springbootinit.service.SysService;
import com.louis.springbootinit.service.UserService;
import com.louis.springbootinit.utils.EmailUtils;
import com.louis.springbootinit.utils.EnumUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 超级管理员接口
 */
@RestController
@RequestMapping("/api/sys")
@Api(value = "超级管理员模块", tags = "超级管理员模块")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@Validated
@Slf4j
public class SysController {

    private final SysService sysService;


    /**
     * 修改用户使用模型权限
     * @param vo
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改用户使用模型权限")
    @AuthCheck(mustRole = UserConstant.SYS_ROLE)
    public BaseResponse update(@RequestBody UpdateUserModelVO vo) {
        return sysService.updateAuth(vo.getId(), vo.getUserRole(),vo.getAuthModelVos());
    }


    /**
     * 查询用户数据（无脱敏版）
     * @return
     */
    @PostMapping("/search")
    @AuthCheck(mustRole =UserConstant.SYS_ROLE)
    @ApiOperation(value = "查询用户数据")
    public BaseResponse queryUsers(@RequestBody QueryUserVO vo) {
        PageUserVO pageUserVO = sysService.queryUsers(vo.getPageNum(), vo.getPageSize(), vo.getUserName(), vo.getUserAccount());
        return ResultUtils.success(pageUserVO);
    }

}
