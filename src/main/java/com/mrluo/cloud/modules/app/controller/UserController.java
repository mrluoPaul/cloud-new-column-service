package com.mrluo.cloud.modules.app.controller;

import com.mrluo.cloud.common.ResponseData;
import com.mrluo.cloud.common.defs.NewsDefs;
import com.mrluo.cloud.modules.app.model.vo.UserVO;
import com.mrluo.cloud.modules.app.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LuoJu
 * @date 2021/10/21/0021 11:27
 * @desc
 * @modified
 */
@RestController
@Validated
@Api(value = "注册管理")
@RequestMapping(UserController.PREFIX_URI)
public class UserController {
    public static final String PREFIX_URI = NewsDefs.API_PREFIX_URI + "/user";
    @Autowired
    private IUserService userService;

    @PostMapping("register")
    @ApiOperation(value = "用户注册", httpMethod = "POST")
    public ResponseData register( @RequestBody @Validated UserVO vo, HttpServletRequest request) {
        return ResponseData.success(userService.register(vo, request));
    }

}
