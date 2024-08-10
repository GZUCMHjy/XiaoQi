package com.louis.springbootinit.model.vo;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.List;

/**
 * @author louis
 * @version 1.0
 * @date 2024/7/13 0:46
 */
@Data
public class PageUserVO {
    private int total;
    private List<PermissionUserVO> list;
}
