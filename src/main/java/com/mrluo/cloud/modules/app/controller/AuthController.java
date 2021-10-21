package com.mrluo.cloud.modules.app.controller;


import com.mrluo.cloud.common.ResponseData;
import com.mrluo.cloud.common.defs.NewsDefs;
import com.mrluo.cloud.common.exception.BusinessException;
import com.mrluo.cloud.common.utils.localmap.NewsSecurityUtil;
import com.mrluo.cloud.common.utils.localmap.NewsUserSession;
import com.mrluo.cloud.modules.app.model.dto.LoginResult;
import com.mrluo.cloud.modules.app.model.vo.LoginRequest;
import com.mrluo.cloud.security.AuthLoginHolder;
import com.mrluo.cloud.security.NewsWebSessionService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户表(User)表控制层
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
@RestController
@RequestMapping(AuthController.PREFIX_URI)
@Slf4j
public class AuthController {
    public static final String PREFIX_URI = NewsDefs.API_PREFIX_URI + "/login";
    @Autowired
    private NewsWebSessionService sessionService;

    @PostMapping
    @ApiOperation(value = "登录系统", notes = "使用用户名密码登录系统，返回值用于后续携带access-token请求头", httpMethod = "POST")
    public ResponseData<LoginResult> login(@RequestBody @Validated LoginRequest auth, HttpServletRequest request) {
        try {
            AuthLoginHolder.initialize(AuthLoginHolder.LoginType.USERNAME_PASSWORD);
            return buildResultWithAccessToken(sessionService
                    .login(auth.getUsername(), auth.getPassword(), request));
        } catch (AuthenticationException exception) {
            log.warn("Failed to login with message {}", exception.getMessage(), exception);
            throw new BusinessException("登录失败");
        }
    }

    private ResponseData<LoginResult> buildResultWithAccessToken(String accessToken) {
        NewsUserSession userDetailDTO = NewsSecurityUtil.getCurrentUser()
                .orElseThrow(() -> new AuthenticationServiceException("内部错误请联系管理员"));
        LoginResult result = LoginResult.with(userDetailDTO, accessToken);
        return ResponseData.success(result);
    }

    @DeleteMapping
    @ApiOperation(value = "退出系统", httpMethod = "DELETE")
    public ResponseData logout(HttpServletRequest request) throws ServletException {
        sessionService.logout(request);
        return ResponseData.success();
    }


}