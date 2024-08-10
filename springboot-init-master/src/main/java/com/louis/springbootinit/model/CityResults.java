package com.louis.springbootinit.model;

import lombok.Data;

import java.util.List;

/**
 * @author louis
 * @version 1.0
 * @date 2024/6/1 22:59
 */
@Data
public class CityResults {
    private String code;
    private String msg;
    private CityData data;
}
