package com.mrluo.cloud.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mrluo.cloud.common.exception.BusinessException;
import com.mrluo.cloud.common.exception.FailCodeEnum;
import com.mrluo.cloud.common.utils.AccountEncryptUtil;
import com.mrluo.cloud.common.utils.localmap.NewsSecurityUtil;
import com.mrluo.cloud.common.utils.localmap.NewsUserSession;
import com.mrluo.cloud.modules.app.model.dto.LoginResult;
import com.mrluo.cloud.modules.app.model.entity.User;
import com.mrluo.cloud.modules.app.mapper.UserMapper;
import com.mrluo.cloud.modules.app.model.vo.UserVO;
import com.mrluo.cloud.modules.app.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mrluo.cloud.security.AuthLoginHolder;
import com.mrluo.cloud.security.NewsWebSessionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private NewsWebSessionService sessionService;

    @Override
    public boolean checkUsernamePassword(String userName, String password) {
        User user = baseMapper.queryUserByUserNameOrEmail(userName);
        if (Objects.isNull(user)) {
            throw new BusinessException(FailCodeEnum.USER_NOT_EXIST, FailCodeEnum.USER_NOT_EXIST.getMsg());
        }
        String password1 = user.getPassword();
        String decrypt = AccountEncryptUtil.decrypt(password1);
        if (decrypt.equals(password)) {
            return true;
        }
        return false;
    }

    @Override
    public User retrieveByUserName(String userName) {
        return baseMapper.queryUserByUserNameOrEmail(userName);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public LoginResult register(UserVO vo, HttpServletRequest request) {
        String userName = vo.getUsername();
        String email = vo.getEmail();
        String password = vo.getPassword();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userName);
        User one = this.getOne(wrapper);
        if (!Objects.isNull(one)) {
            throw new BusinessException(FailCodeEnum.USERNAME_REPEAT, FailCodeEnum.USERNAME_REPEAT.getMsg());
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User emailUser = this.getOne(queryWrapper);
        if (!Objects.isNull(emailUser)) {
            throw new BusinessException(FailCodeEnum.EMAIL_REPEAT, FailCodeEnum.EMAIL_REPEAT.getMsg());
        }
        User user = new User();
        BeanUtils.copyProperties(vo, user);
        user.setPassword(AccountEncryptUtil.encrypt(password));
        Date date = new Date();
        user.setUpdatedDate(date);
        user.setCreatedDate(date);
        boolean save = this.save(user);
        LoginResult result = new LoginResult();
        if (save) {
            AuthLoginHolder.initialize(AuthLoginHolder.LoginType.USERNAME_PASSWORD);
            result = buildResultWithAccessToken(sessionService.login(userName, password, request));
        }
        return result;
    }

    private LoginResult buildResultWithAccessToken(String accessToken) {
        NewsUserSession userDetailDTO = NewsSecurityUtil.getCurrentUser()
                .orElseThrow(() -> new AuthenticationServiceException("内部错误请联系管理员"));
        LoginResult result = LoginResult.with(userDetailDTO, accessToken);
        return result;
    }
}