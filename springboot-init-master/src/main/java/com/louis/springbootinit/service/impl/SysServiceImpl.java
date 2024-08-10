package com.louis.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.louis.springbootinit.common.BaseResponse;
import com.louis.springbootinit.common.ErrorCode;
import com.louis.springbootinit.common.ResultUtils;
import com.louis.springbootinit.exception.BusinessException;
import com.louis.springbootinit.model.entity.User;
import com.louis.springbootinit.model.vo.AuthModelVO;
import com.louis.springbootinit.model.vo.PageUserVO;
import com.louis.springbootinit.model.vo.PermissionUserVO;
import com.louis.springbootinit.service.SysService;
import com.louis.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author louis
 * @version 1.0
 * @date 2024/7/10 21:32
 */
@Service
@Slf4j
public class SysServiceImpl implements SysService {
    @Resource
    private UserService userService;

    /**
     * 更新用户使用模型权限
     * @param userId
     * @param authModelVos
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateAuth(String userId, String userRole, List<String> authModelVos) {
        User user = userService.getOne(new QueryWrapper<User>().eq("id", userId));
        // 修改权限
        user.setAuthModelVos(authModelVos);
        // 修改用户角色
        if(StringUtils.isNotBlank(userRole)){
            user.setUserRole(userRole);
        }
        try{
            userService.saveOrUpdate(user);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"修改失败");
        }
        return ResultUtils.success("更新成功");
    }

    @Override
    public PageUserVO queryUsers(int pageNum, int pageSize, String userName, String userAccount) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(userName)){
            userQueryWrapper.like("userName",userName);
        }
        if(StringUtils.isNotBlank(userAccount)){
            userQueryWrapper.like("userAccount",userAccount);
        }
        Page<User> page = userService.page(new Page<>(pageNum, pageSize), userQueryWrapper);
        PageUserVO pageUserVO = new PageUserVO();

        List<PermissionUserVO> res = page.getRecords().stream().map(record -> {
            PermissionUserVO permissionUserVO = new PermissionUserVO();
            permissionUserVO.setUserAccount(record.getUserAccount());
            permissionUserVO.setUserName(record.getUserName());
            permissionUserVO.setId(record.getId().toString());
            permissionUserVO.setUserRole(record.getUserRole());
            permissionUserVO.setAuthModelVos(record.getAuthModelVos());
            return permissionUserVO;
        }).collect(Collectors.toList());
        pageUserVO.setList(res);
        pageUserVO.setTotal(Integer.parseInt(String.valueOf(page.getTotal())));
        return pageUserVO;
    }

    /**
     * 分页查询用户列表
     * @return
     */

}
