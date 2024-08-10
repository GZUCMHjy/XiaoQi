package com.louis.springbootinit.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Namego
 * @date 2024/5/25 18:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageCacheBO {
    private String to;

    private String code;

    private Date createTime;
}
