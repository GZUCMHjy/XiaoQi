package com.louis.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName model
 */
@TableName(value ="model")
@Data
public class Model implements Serializable {
    /**
     * 主键ID，用于唯一标识每条记录
     */
    @TableId
    private String id;

    /**
     * 模型ID，可能用于其他系统中的唯一标识
     */
    private String modelid;

    /**
     * 模型名称，用于描述模型的名称或标识
     */
    private String modelname;

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
        Model other = (Model) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getModelid() == null ? other.getModelid() == null : this.getModelid().equals(other.getModelid()))
            && (this.getModelname() == null ? other.getModelname() == null : this.getModelname().equals(other.getModelname()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getModelid() == null) ? 0 : getModelid().hashCode());
        result = prime * result + ((getModelname() == null) ? 0 : getModelname().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", modelid=").append(modelid);
        sb.append(", modelname=").append(modelname);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}