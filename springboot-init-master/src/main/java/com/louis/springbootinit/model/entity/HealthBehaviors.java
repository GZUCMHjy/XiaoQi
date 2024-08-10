package com.louis.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 健康行为表
 * @TableName health_behaviors
 */
@TableName(value ="health_behaviors")
@Data
public class HealthBehaviors implements Serializable {
    /**
     * 用户ID
     */
    @TableId
    private String userId;

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
        HealthBehaviors other = (HealthBehaviors) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getEatingHabits() == null ? other.getEatingHabits() == null : this.getEatingHabits().equals(other.getEatingHabits()))
            && (this.getExerciseHabits() == null ? other.getExerciseHabits() == null : this.getExerciseHabits().equals(other.getExerciseHabits()))
            && (this.getSmokingAndDrinking() == null ? other.getSmokingAndDrinking() == null : this.getSmokingAndDrinking().equals(other.getSmokingAndDrinking()))
            && (this.getSleepPattern() == null ? other.getSleepPattern() == null : this.getSleepPattern().equals(other.getSleepPattern()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getEatingHabits() == null) ? 0 : getEatingHabits().hashCode());
        result = prime * result + ((getExerciseHabits() == null) ? 0 : getExerciseHabits().hashCode());
        result = prime * result + ((getSmokingAndDrinking() == null) ? 0 : getSmokingAndDrinking().hashCode());
        result = prime * result + ((getSleepPattern() == null) ? 0 : getSleepPattern().hashCode());
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
        sb.append(", eatingHabits=").append(eatingHabits);
        sb.append(", exerciseHabits=").append(exerciseHabits);
        sb.append(", smokingAndDrinking=").append(smokingAndDrinking);
        sb.append(", sleepPattern=").append(sleepPattern);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}