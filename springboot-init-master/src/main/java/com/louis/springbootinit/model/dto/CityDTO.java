package com.louis.springbootinit.model.dto;

import com.louis.springbootinit.common.BaseResponse;
import com.louis.springbootinit.model.CityApiResult;
import lombok.Data;

/**
 * @author louis
 * @version 1.0
 * @date 2024/6/1 15:39
 */
@Data
public class CityDTO {
    private String status;
    private String message;
    private String request_id;
    private BaseResponse<CityApiResult> result;
}
