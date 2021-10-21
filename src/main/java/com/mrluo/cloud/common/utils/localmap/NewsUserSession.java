package com.mrluo.cloud.common.utils.localmap;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@ToString
public class NewsUserSession implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("用户中心（用户ID）")
    private String ucUserId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("姓名")
    private String nickName;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("电话号码")
    private String phoneNum;

    @ApiModelProperty("权限集")
    private Set<String> permissions;
}
