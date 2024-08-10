package com.louis.springbootinit.model.dto.bio;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
public class HealthBehaviorsDTO implements Serializable {


    /**
     * 饮食习惯
     */
    private String eatingHabits;

    /**
     * 运动习惯
     */
    private String exerciseHabits;

    /**
     * 吸烟饮酒
     */
    private String smokingAndDrinking;

    /**
     * 睡眠模式
     */
    private String sleepPattern;


    private static final long serialVersionUID = 1L;


}