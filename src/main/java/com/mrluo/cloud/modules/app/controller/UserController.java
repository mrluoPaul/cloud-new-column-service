package com.mrluo.cloud.modules.app.controller;

import com.mrluo.cloud.modules.app.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LuoJu
 * @date 2021/10/21/0021 11:27
 * @desc
 * @modified
 */
@RestController
@RequestMapping("column")
public class UserController {
    @Autowired
    private IUserService userService;
}
