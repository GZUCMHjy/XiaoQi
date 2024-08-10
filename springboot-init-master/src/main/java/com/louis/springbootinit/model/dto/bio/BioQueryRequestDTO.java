package com.louis.springbootinit.model.dto.bio;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author louis
 * @version 1.0
 * @date 2024/6/7 20:53
 */
@Data
public class BioQueryRequestDTO {
    private String userName;
    private BiomarkersDTO biomarkers;

    private BodyHealthDTO bodyHealth;

    private HealthBehaviorsDTO healthBehaviors;

    private MedicalRecordsDTO medicalRecords;

    private MentalHealthDTO mentalHealth;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy年MM月dd日HH:mm:ss")
    @DateTimeFormat(pattern="yyyy年MM月dd日HH:mm:ss")
    private Date updateTime;
}
