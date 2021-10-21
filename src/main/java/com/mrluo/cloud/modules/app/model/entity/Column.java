package com.mrluo.cloud.modules.app.model.entity;


import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 新闻栏目表(Column)实体类
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
@Data
@TableName("column")
public class Column extends BaseEntity<Column> {
    private static final long serialVersionUID = -97436067191105055L;

    /**
     * 栏目名
     */
    @TableField("column_name")
    private String columnName;

    /**
     * 父级栏目id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 栏目类型 1 一级栏目  2 二级栏目
     */
    @TableField("type")
    private Integer type;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}