package com.louis.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 身体健康信息表
 * @TableName body_health
 */
@TableName(value ="body_health")
@Data
public class BodyHealth implements Serializable {
    /**
     * 用户ID
     */
    @TableId
    private String userId;

    /**
     * 出生日期
     */
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

    /**
     * dataTime
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
        BodyHealth other = (BodyHealth) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getBirthDate() == null ? other.getBirthDate() == null : this.getBirthDate().equals(other.getBirthDate()))
            && (this.getHeight() == null ? other.getHeight() == null : this.getHeight().equals(other.getHeight()))
            && (this.getWeight() == null ? other.getWeight() == null : this.getWeight().equals(other.getWeight()))
            && (this.getBMI() == null ? other.getBMI() == null : this.getBMI().equals(other.getBMI()))
            && (this.getBodyTemperature() == null ? other.getBodyTemperature() == null : this.getBodyTemperature().equals(other.getBodyTemperature()))
            && (this.getBloodPressure() == null ? other.getBloodPressure() == null : this.getBloodPressure().equals(other.getBloodPressure()))
            && (this.getHeartRate() == null ? other.getHeartRate() == null : this.getHeartRate().equals(other.getHeartRate()))
            && (this.getBloodSugarLevel() == null ? other.getBloodSugarLevel() == null : this.getBloodSugarLevel().equals(other.getBloodSugarLevel()))
            && (this.getBloodLipid() == null ? other.getBloodLipid() == null : this.getBloodLipid().equals(other.getBloodLipid()))
            && (this.getBloodOxygenSaturation() == null ? other.getBloodOxygenSaturation() == null : this.getBloodOxygenSaturation().equals(other.getBloodOxygenSaturation()))
            && (this.getAge() == null ? other.getAge() == null : this.getAge().equals(other.getAge()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getBirthDate() == null) ? 0 : getBirthDate().hashCode());
        result = prime * result + ((getHeight() == null) ? 0 : getHeight().hashCode());
        result = prime * result + ((getWeight() == null) ? 0 : getWeight().hashCode());
        result = prime * result + ((getBMI() == null) ? 0 : getBMI().hashCode());
        result = prime * result + ((getBodyTemperature() == null) ? 0 : getBodyTemperature().hashCode());
        result = prime * result + ((getBloodPressure() == null) ? 0 : getBloodPressure().hashCode());
        result = prime * result + ((getHeartRate() == null) ? 0 : getHeartRate().hashCode());
        result = prime * result + ((getBloodSugarLevel() == null) ? 0 : getBloodSugarLevel().hashCode());
        result = prime * result + ((getBloodLipid() == null) ? 0 : getBloodLipid().hashCode());
        result = prime * result + ((getBloodOxygenSaturation() == null) ? 0 : getBloodOxygenSaturation().hashCode());
        result = prime * result + ((getAge() == null) ? 0 : getAge().hashCode());
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
        sb.append(", birthDate=").append(birthDate);
        sb.append(", height=").append(height);
        sb.append(", weight=").append(weight);
        sb.append(", BMI=").append(BMI);
        sb.append(", bodyTemperature=").append(bodyTemperature);
        sb.append(", bloodPressure=").append(bloodPressure);
        sb.append(", heartRate=").append(heartRate);
        sb.append(", bloodSugarLevel=").append(bloodSugarLevel);
        sb.append(", bloodLipid=").append(bloodLipid);
        sb.append(", bloodOxygenSaturation=").append(bloodOxygenSaturation);
        sb.append(", age=").append(age);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}