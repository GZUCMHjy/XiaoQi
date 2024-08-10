package com.louis.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 聊天记录表
 * @TableName msg_log
 */
@TableName(value ="msg_log")
@Data
public class MsgLog implements Serializable {
    /**
     * 每条聊天记录的唯一标识
     */
    @TableId
    private String uuid;

    /**
     * 客户端发来的prompt指令或标题
     */
    private String title;

    /**
     * 与消息相关的图标信息（例如URL或文件名）
     */
    private String icon;

    /**
     * 系统相关信息（如系统消息、状态等）
     */
    private String systeminfo;

    /**
     * 调用的大模型ID
     */
    private String modelid;

    /**
     * 用户id
     */
    private String userId;

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
        MsgLog other = (MsgLog) that;
        return (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getIcon() == null ? other.getIcon() == null : this.getIcon().equals(other.getIcon()))
            && (this.getSysteminfo() == null ? other.getSysteminfo() == null : this.getSysteminfo().equals(other.getSysteminfo()))
            && (this.getModelid() == null ? other.getModelid() == null : this.getModelid().equals(other.getModelid()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getIcon() == null) ? 0 : getIcon().hashCode());
        result = prime * result + ((getSysteminfo() == null) ? 0 : getSysteminfo().hashCode());
        result = prime * result + ((getModelid() == null) ? 0 : getModelid().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", uuid=").append(uuid);
        sb.append(", title=").append(title);
        sb.append(", icon=").append(icon);
        sb.append(", systeminfo=").append(systeminfo);
        sb.append(", modelid=").append(modelid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append(", userId=").append(userId);
        sb.append("]");
        return sb.toString();
    }
}