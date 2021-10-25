package com.mrluo.cloud.modules.app.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author LuoJu
 * @date 2021/10/21 22:31
 **/
@Data
@ApiModel("新增栏目或者文章vo")
public class ColumnVO implements Serializable {
    private static final long serialVersionUID = 8170618456894561026L;
    @NotEmpty(message = "栏目标题不能为空")
    @ApiModelProperty("栏目标题")
    private String name;
    @NotNull(message = "父级id不能为空,一级栏目默认是0")
    @ApiModelProperty("栏目父级id")
    private Long parentId;
    @ApiModelProperty("文章内容")
    private String content;
    @ApiModelProperty("主键id,修改用")
    private Long id;
    @ApiModelProperty("栏目类型 1 一级栏目  2 二级栏目 3 文章")
    @NotNull(message = "类型不能为空")
    private Integer type;
}
