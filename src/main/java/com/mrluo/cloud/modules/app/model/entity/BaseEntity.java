package com.mrluo.cloud.modules.app.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mrluo.cloud.common.utils.localmap.NewsSecurityUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LuoJu
 * @date 2021/10/21/0021 10:13
 * @desc
 * @modified
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public abstract class BaseEntity<T extends BaseEntity> extends Model<T> {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 创建人
     */
    @TableField(value = "created_user", fill = FieldFill.INSERT)
    private String createdUser;

    /**
     * 更新人
     */
    @TableField(value = "updated_user", fill = FieldFill.UPDATE)
    private String updatedUser;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private Date createdDate;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private Date updatedDate;

    /**
     * 逻辑删除
     * 0-正常，1-删除
     */
    @TableField("del_flag")
    @TableLogic(value = "0", delval = "1")
    private Boolean delFlag = Boolean.FALSE;

    public Date getCreatedTime() {
        return new Date();
    }

    public Date getUpdatedTime() {
        return new Date();
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
