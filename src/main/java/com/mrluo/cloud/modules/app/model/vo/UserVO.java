package com.mrluo.cloud.modules.app.model.vo;

import io.swagger.annotations.ApiModel;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * @author LuoJu
 * @date 2021/10/21/0021 16:24
 * @desc
 * @modified
 */
@Data
@Validated
@ApiModel(value = "注册用户vo")
public class UserVO implements Serializable {
    private static final long serialVersionUID = 249068183526416923L;
    @NotEmpty(message = "username不能为空")
    @Pattern(regexp = "[A-Za-z0-9]{5,20}", message = "用户名只能使用字母和数字，长度在5~20之间")
    private String username;
    @NotEmpty(message = "password不能为空")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$", message = "密码长度必须在8~20之间,至少包含一个大写、一个小写、一个数字、一个特殊符号")
    private String password;
    @NotEmpty(message = "email不能为空")
    @Pattern(regexp = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}", message = "邮箱格式不正确,请输入正确的邮箱")
    private String email;
}
