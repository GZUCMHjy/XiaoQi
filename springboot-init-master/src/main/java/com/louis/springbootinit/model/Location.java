package com.louis.springbootinit.model;

import lombok.Data;

/**
 * @author louis
 * @version 1.0
 * @date 2024/6/1 15:42
 */
@Data
public class Location {
    private String id;
    private String name;
    private String country;
    private String path;
    private String timezone;
    private String timezone_offset;
}
