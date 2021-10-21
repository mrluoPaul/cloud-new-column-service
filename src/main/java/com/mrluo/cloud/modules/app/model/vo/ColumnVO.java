package com.mrluo.cloud.modules.app.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author LuoJu
 * @date 2021/10/21 22:31
 **/
@Data
@ApiModel("新增栏目vo")
public class ColumnVO implements Serializable {
    private static final long serialVersionUID = 8170618456894561026L;
    @NotEmpty(message = "栏目标题不能为空")
    @ApiModelProperty("栏目标题")
    private String columnName;
    @ApiModelProperty("栏目父级id")
    private Long parentId;
}
