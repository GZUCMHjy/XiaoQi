package com.louis.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 心理健康表
 * @TableName mental_health
 */
@TableName(value ="mental_health")
@Data
public class MentalHealth implements Serializable {
    /**
     * 用户ID
     */
    @TableId
    private String userId;

    /**
     * 心理评估记录
     */
    private String psychologicalAssessment;

    /**
     * 心理治疗记录
     */
    private String psychotherapyRecords;

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
        MentalHealth other = (MentalHealth) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getPsychologicalAssessment() == null ? other.getPsychologicalAssessment() == null : this.getPsychologicalAssessment().equals(other.getPsychologicalAssessment()))
            && (this.getPsychotherapyRecords() == null ? other.getPsychotherapyRecords() == null : this.getPsychotherapyRecords().equals(other.getPsychotherapyRecords()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getPsychologicalAssessment() == null) ? 0 : getPsychologicalAssessment().hashCode());
        result = prime * result + ((getPsychotherapyRecords() == null) ? 0 : getPsychotherapyRecords().hashCode());
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
        sb.append(", psychologicalAssessment=").append(psychologicalAssessment);
        sb.append(", psychotherapyRecords=").append(psychotherapyRecords);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}