package com.louis.springbootinit.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author louis
 * @version 1.0
 * @date 2024/7/13 2:19
 */
@Data
public class IdRequest {
    @JsonProperty("id")
    private String id;
}
