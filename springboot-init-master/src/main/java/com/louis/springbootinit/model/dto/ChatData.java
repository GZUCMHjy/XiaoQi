package com.louis.springbootinit.model.dto;

import lombok.Data;

/**
 * @author louis
 * @version 1.0
 * @date 2024/5/31 13:12
 */
@Data
public class ChatData {
    private String  id;
    private String  text;
    private String  role;
    private String  status;
    private String  dateTime;
    private String  requestId;
}
