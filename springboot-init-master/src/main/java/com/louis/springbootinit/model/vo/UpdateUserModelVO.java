package com.louis.springbootinit.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author louis
 * @version 1.0
 * @date 2024/7/13 3:11
 */
@Data
public class UpdateUserModelVO {
    private String id;
    private String userRole;
    private List<String> authModelVos;
}
