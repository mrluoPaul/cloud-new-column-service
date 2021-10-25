package com.mrluo.cloud.modules.app.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * @author LuoJu
 * @date 2021/10/21/0021 11:15
 * @desc
 * @modified
 */
@Data
@Validated
@ApiModel(value = "登录参数")
public class LoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotEmpty
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotEmpty
    @ApiModelProperty(value = "密码")
    private String password;
}
