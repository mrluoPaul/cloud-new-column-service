package com.mrluo.cloud.modules.app.model.dto;

import com.mrluo.cloud.common.utils.localmap.NewsUserSession;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author LuoJu
 * @date 2021/10/21/0021 11:07
 * @desc
 * @modified
 */
@Data
public class LoginResult implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("访问码")
    private String accessToken;
    @ApiModelProperty("用户id")
    private Long id;
    @ApiModelProperty("用户账号")
    private String userName;

    public static LoginResult with(NewsUserSession userDTO, String accessToken) {
        LoginResult loginResult = new LoginResult();
        loginResult.setId(userDTO.getId());
        loginResult.setUserName(userDTO.getUsername());
        loginResult.setAccessToken(accessToken);
        return loginResult;
    }
}
