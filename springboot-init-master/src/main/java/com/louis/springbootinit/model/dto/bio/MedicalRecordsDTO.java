package com.louis.springbootinit.model.dto.bio;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
public class MedicalRecordsDTO implements Serializable {


    /**
     * 既往病史
     */
    private String pastMedicalHistory;

    /**
     * 家族病史
     */
    private String familyMedicalHistory;

    /**
     * 过敏史
     */
    private String allergyHistory;

    /**
     * 手术史
     */
    private String surgicalHistory;

    /**
     * 住院记录
     */
    private String hospitalizationRecords;

    /**
     * 疫苗接种记录
     */
    private String vaccinationRecords;

    private static final long serialVersionUID = 1L;


}