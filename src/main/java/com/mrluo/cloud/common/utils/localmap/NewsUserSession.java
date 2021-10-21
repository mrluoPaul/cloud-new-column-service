package com.mrluo.cloud.common.utils.localmap;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class NewsUserSession implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;
}
