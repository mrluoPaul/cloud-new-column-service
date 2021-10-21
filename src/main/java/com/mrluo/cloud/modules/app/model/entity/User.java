package com.mrluo.cloud.modules.app.model.entity;


import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 用户表(User)实体类
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
@Data
@TableName("user")
public class User extends BaseEntity<User> {
    private static final long serialVersionUID = 333277205760577934L;

    /**
     * 用户账号
     */
    @TableField("username")
    private String username;

    /**
     * 用户密码
     */
    @TableField("password")
    private String password;

    /**
     * 用户邮箱
     */
    @TableField("email")
    private String email;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}