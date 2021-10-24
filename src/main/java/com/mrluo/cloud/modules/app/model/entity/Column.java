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
@TableName("column_article")
public class Column extends BaseEntity<Column> {
    private static final long serialVersionUID = -97436067191105055L;

    /**
     * 栏目名
     */
    @TableField("name")
    private String name;

    /**
     * 父级栏目id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 栏目类型 1 一级栏目  2 二级栏目 3 文章
     */
    @TableField("type")
    private Integer type;
    /**
     * 文章内容
     */
    @TableField("content")
    private String content;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}