package com.mrluo.cloud.modules.app.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author LuoJu
 * @date 2021/10/21 22:42
 **/
@Data
@ApiModel(value = "文章详情VO")
public class ArticleVO implements Serializable {
    private static final long serialVersionUID = 8827343731262085857L;
    @ApiModelProperty("文章id")
    private Long id;
    @ApiModelProperty("文章名称")
    private String name;
    @ApiModelProperty("文章内容")
    private String content;
    @ApiModelProperty("文章所属栏目名称")
    private String columnName;
}
