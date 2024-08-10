package com.louis.springbootinit.model.dto;

import lombok.Data;

/**
 * @author louis
 * @version 1.0
 * @date 2024/5/26 17:51
 */
@Data
public class BaiLianResult {
    private String message;
    private int status_code;
    public void fail(String message,int status_code){
        this.message = message;
        this.status_code = status_code;
    }
    public void success(String message,int status_code){
        this.message = message;
        this.status_code = status_code;
    }

}
