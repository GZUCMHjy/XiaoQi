package com.louis.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 医疗记录表
 * @TableName medical_records
 */
@TableName(value ="medical_records")
@Data
public class MedicalRecords implements Serializable {
    /**
     * 用户ID
     */
    @TableId
    private String userId;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        MedicalRecords other = (MedicalRecords) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getPastMedicalHistory() == null ? other.getPastMedicalHistory() == null : this.getPastMedicalHistory().equals(other.getPastMedicalHistory()))
            && (this.getFamilyMedicalHistory() == null ? other.getFamilyMedicalHistory() == null : this.getFamilyMedicalHistory().equals(other.getFamilyMedicalHistory()))
            && (this.getAllergyHistory() == null ? other.getAllergyHistory() == null : this.getAllergyHistory().equals(other.getAllergyHistory()))
            && (this.getSurgicalHistory() == null ? other.getSurgicalHistory() == null : this.getSurgicalHistory().equals(other.getSurgicalHistory()))
            && (this.getHospitalizationRecords() == null ? other.getHospitalizationRecords() == null : this.getHospitalizationRecords().equals(other.getHospitalizationRecords()))
            && (this.getVaccinationRecords() == null ? other.getVaccinationRecords() == null : this.getVaccinationRecords().equals(other.getVaccinationRecords()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getPastMedicalHistory() == null) ? 0 : getPastMedicalHistory().hashCode());
        result = prime * result + ((getFamilyMedicalHistory() == null) ? 0 : getFamilyMedicalHistory().hashCode());
        result = prime * result + ((getAllergyHistory() == null) ? 0 : getAllergyHistory().hashCode());
        result = prime * result + ((getSurgicalHistory() == null) ? 0 : getSurgicalHistory().hashCode());
        result = prime * result + ((getHospitalizationRecords() == null) ? 0 : getHospitalizationRecords().hashCode());
        result = prime * result + ((getVaccinationRecords() == null) ? 0 : getVaccinationRecords().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", pastMedicalHistory=").append(pastMedicalHistory);
        sb.append(", familyMedicalHistory=").append(familyMedicalHistory);
        sb.append(", allergyHistory=").append(allergyHistory);
        sb.append(", surgicalHistory=").append(surgicalHistory);
        sb.append(", hospitalizationRecords=").append(hospitalizationRecords);
        sb.append(", vaccinationRecords=").append(vaccinationRecords);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}