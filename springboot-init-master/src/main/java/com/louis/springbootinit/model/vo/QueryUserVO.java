package com.louis.springbootinit.model.vo;

import lombok.Data;

/**
 * @author louis
 * @version 1.0
 * @date 2024/7/13 2:33
 */
@Data
public class QueryUserVO {
    private int pageNum;
    private int pageSize;
    private String userName;
    private String userAccount;
}
