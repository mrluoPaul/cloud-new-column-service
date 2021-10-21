package com.mrluo.cloud.modules.app.service;

import com.mrluo.cloud.modules.app.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
public interface IUserService extends IService<User> {
    boolean checkUsernamePassword(String valueOf, String valueOf1);

    String checkCenterAuthCode(String loginAppCode, String valueOf);

    boolean isDisabled(String usingUserName);

    Set<String> permissionByAppCode(String loginAppCode, String usingUserName);

    User retrieveByUserName(String loginAppCode, String usingUserName);
}