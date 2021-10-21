package com.mrluo.cloud.security;


import com.mrluo.cloud.common.utils.localmap.NewsUserDetailDTO;
import com.mrluo.cloud.common.utils.localmap.NewsUserSession;
import com.mrluo.cloud.modules.app.model.entity.User;
import com.mrluo.cloud.modules.app.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    protected IUserService userService;

    protected void additionalAuthenticationChecks(
            UserDetails userDetails, UsernamePasswordAuthenticationToken token) throws AuthenticationException {
        if (!userService.checkUsernamePassword(String.valueOf(token.getPrincipal()), String.valueOf(token.getCredentials()))) {
            throw new AccountExpiredException("用户名密码不匹配");
        }
    }

    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken token) throws AuthenticationException {
        User userDTO = Optional.ofNullable(userService.retrieveByUserName(username))
                .orElseThrow(() -> new AccountExpiredException("用户 " + username + " 无权登录系统"));
        NewsUserSession userSession = new NewsUserSession();
        userSession.setId(userDTO.getId());
        userSession.setUsername(userDTO.getUsername());
        return new NewsUserDetailDTO(userSession);
    }
}
