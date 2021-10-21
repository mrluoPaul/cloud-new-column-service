package com.mrluo.cloud.modules.app.service;

import com.mrluo.cloud.modules.app.model.dto.LoginResult;
import com.mrluo.cloud.modules.app.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mrluo.cloud.modules.app.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
public interface IUserService extends IService<User> {
    boolean checkUsernamePassword(String userName, String password);

    User retrieveByUserName(String userName);

    LoginResult register(UserVO vo, HttpServletRequest request);
}