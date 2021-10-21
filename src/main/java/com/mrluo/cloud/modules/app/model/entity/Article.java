package com.mrluo.cloud.modules.app.model.entity;

import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 文章表(Article)实体类
 *
 * @author makejava
 * @since 2021-10-21 10:10:39
 */
@Data
@TableName("article")
public class Article extends BaseEntity<Article> {
    private static final long serialVersionUID = 430092090390869855L;

    /**
     * 文章标题
     */
    @TableField("title")
    private String title;

    /**
     * 文章内容
     */
    @TableField("content")
    private String content;

    /**
     * 二级栏目id
     */
    @TableField("column_id")
    private Long columnId;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}