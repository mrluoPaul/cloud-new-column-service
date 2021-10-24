package com.mrluo.cloud.modules.app.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author LuoJu
 * @date 2021/10/24 21:43
 **/
@Data
@ApiModel(value = "树形结构vo")
public class TreeVO implements Serializable {
    private static final long serialVersionUID = 8956335837176454586L;
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("栏目或者文章名称")
    private String name;
    @ApiModelProperty("父级id")
    private Long parentId;
    @ApiModelProperty("父级名称")
    private String parentName;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("更新时间")
    private Date updatedTime;
    @ApiModelProperty("类型  1 一级栏目  2 二级栏目 3 文章")
    private Integer type;
    @ApiModelProperty("子级")
    private List<TreeVO> children = new ArrayList<>();
}
