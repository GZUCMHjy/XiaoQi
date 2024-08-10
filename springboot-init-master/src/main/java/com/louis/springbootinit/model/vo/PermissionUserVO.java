package com.louis.springbootinit.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author louis
 * @version 1.0
 * @date 2024/7/13 0:47
 */
@Data
public class PermissionUserVO {
    private String id;
    private String userName;
    private String userAccount;
    private String userRole;
    private List<String> authModelVos;
}
