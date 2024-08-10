package com.louis.springbootinit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.louis.springbootinit.common.BaseResponse;
import com.louis.springbootinit.model.entity.User;
import com.louis.springbootinit.model.vo.AuthModelVO;
import com.louis.springbootinit.model.vo.PageUserVO;

import java.util.List;

/**
 * @author louis
 * @version 1.0
 * @date 2024/7/10 20:30
 */
public interface SysService {

    /**
     * 更改某用户使用模型权限
     * @param userId
     * @param models
     * @return
     */
    BaseResponse updateAuth(String userId,String userRole,List<String> models);


    /**
     * 分页查询用户列表
     * @param pageNum
     * @param pageSize
     * @param userName
     * @param userAccount
     * @return
     */
    PageUserVO queryUsers(int pageNum, int pageSize, String userName, String userAccount);


}
