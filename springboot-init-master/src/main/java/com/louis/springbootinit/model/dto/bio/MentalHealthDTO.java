package com.louis.springbootinit.model.dto.bio;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
public class MentalHealthDTO implements Serializable {

    /**
     * 心理评估记录
     */
    private String psychologicalAssessment;

    /**
     * 心理治疗记录
     */
    private String psychotherapyRecords;

    private static final long serialVersionUID = 1L;

}