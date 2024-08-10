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
public class BioAddRequestDTO {
    @NotNull
    @Valid
    private BiomarkersDTO biomarkers;
    @NotNull
    @Valid
    private BodyHealthDTO bodyHealth;
    @NotNull
    @Valid
    private HealthBehaviorsDTO healthBehaviors;
    @NotNull
    @Valid
    private MedicalRecordsDTO medicalRecords;
    @NotNull
    @Valid
    private MentalHealthDTO mentalHealth;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String userName;
}
