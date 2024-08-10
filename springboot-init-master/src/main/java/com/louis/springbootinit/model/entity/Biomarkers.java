package com.louis.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 生物标志物表
 * @TableName biomarkers
 */
@TableName(value ="biomarkers")
@Data
public class Biomarkers implements Serializable {
    /**
     * 用户ID
     */
    @TableId
    private String userId;

    /**
     * 基因检测
     */
    private String geneticTesting;

    /**
     * 荷尔蒙水平
     */
    private String hormoneLevels;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
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
        Biomarkers other = (Biomarkers) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getGeneticTesting() == null ? other.getGeneticTesting() == null : this.getGeneticTesting().equals(other.getGeneticTesting()))
            && (this.getHormoneLevels() == null ? other.getHormoneLevels() == null : this.getHormoneLevels().equals(other.getHormoneLevels()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getGeneticTesting() == null) ? 0 : getGeneticTesting().hashCode());
        result = prime * result + ((getHormoneLevels() == null) ? 0 : getHormoneLevels().hashCode());
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
        sb.append(", geneticTesting=").append(geneticTesting);
        sb.append(", hormoneLevels=").append(hormoneLevels);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}