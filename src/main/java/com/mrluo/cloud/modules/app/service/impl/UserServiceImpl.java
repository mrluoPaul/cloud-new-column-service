package com.mrluo.cloud.modules.app.service.impl;

import com.mrluo.cloud.modules.app.model.entity.User;
import com.mrluo.cloud.modules.app.mapper.UserMapper;
import com.mrluo.cloud.modules.app.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
@Transactional(rollbackFor = {Exception.class})
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    public boolean checkUsernamePassword(String valueOf, String valueOf1) {
        return true;
    }

    @Override
    public String checkCenterAuthCode(String loginAppCode, String valueOf) {
        return null;
    }

    @Override
    public boolean isDisabled(String usingUserName) {
        return false;
    }

    @Override
    public Set<String> permissionByAppCode(String loginAppCode, String usingUserName) {
        return null;
    }

    @Override
    public User retrieveByUserName(String loginAppCode, String usingUserName) {
        User user = new User();
        user.setUsername("luoju");
        user.setPassword("123456");
        return user;
    }
}