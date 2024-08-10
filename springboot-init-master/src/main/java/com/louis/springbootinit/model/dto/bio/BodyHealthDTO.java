package com.louis.springbootinit.model.dto.bio;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class BodyHealthDTO implements Serializable {

    /**
     * 出生日期
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy年MM月dd日")
    @DateTimeFormat(pattern="yyyy年MM月dd日")
    private Date birthDate;

    /**
     * 身高（米）
     */
    private BigDecimal height;

    /**
     * 体重（千克）
     */
    private BigDecimal weight;

    /**
     * 身体质量指数（BMI）
     */
    private BigDecimal BMI;

    /**
     * 体温（摄氏度）
     */
    private BigDecimal bodyTemperature;

    /**
     * 血压（高压/低压）
     */
    private String bloodPressure;

    /**
     * 心率（次/分钟）
     */
    private Integer heartRate;

    /**
     * 血糖水平（mmol/L）
     */
    private BigDecimal bloodSugarLevel;

    /**
     * 血脂（mmol/L）
     */
    private BigDecimal bloodLipid;

    /**
     * 血氧浓度（%）
     */
    private BigDecimal bloodOxygenSaturation;

    /**
     * 年龄
     */
    private Integer age;

    private static final long serialVersionUID = 1L;

}