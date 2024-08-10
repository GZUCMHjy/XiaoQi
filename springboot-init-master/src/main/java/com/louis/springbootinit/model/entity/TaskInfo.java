package com.louis.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务信息表
 * @TableName task_info
 */
@TableName(value ="task_info")
@Data
public class TaskInfo implements Serializable {
    /**
     * 任务ID
     */
    @TableId(type = IdType.AUTO)
    private Long taskId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 唯一标识符
     */
    private String uuid;

    /**
     * 任务提示信息
     */
    private String prompt;

    /**
     * 图片URL（服务器的链接）
     */
    private String picUrl;
    /**
     * 原图片链接
     */
    private String rawPicUrl;

    /**
     * 任务状态（0：待执行 1：已执行）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}