package com.mrluo.cloud.modules.app.model.vo;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
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
public class LoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotEmpty
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotEmpty
    @ApiModelProperty(value = "密码")
    private String password;
}
