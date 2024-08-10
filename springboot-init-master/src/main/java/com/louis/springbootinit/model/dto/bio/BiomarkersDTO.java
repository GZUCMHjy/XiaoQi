package com.louis.springbootinit.model.dto.bio;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
public class BiomarkersDTO implements Serializable {


    /**
     * 基因检测
     */
    private String geneticTesting;

    /**
     * 荷尔蒙水平
     */
    private String hormoneLevels;

    private static final long serialVersionUID = 1L;

}