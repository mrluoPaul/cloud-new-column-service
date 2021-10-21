package com.mrluo.cloud.common.utils.localmap;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NewsSecurityUtil {
    public static Optional<NewsUserSession> getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(NewsUserDetailDTO.class::isInstance)
                .map(NewsUserDetailDTO.class::cast).map(NewsUserDetailDTO::getUser);
    }

    public static Optional<String> getCurrentUserName() {
        return getCurrentUser().map(NewsUserSession::getUsername);
    }

    public static Optional<String> getUserName(Authentication authentication) {
        return Optional.ofNullable(authentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(NewsUserDetailDTO.class::isInstance)
                .map(NewsUserDetailDTO.class::cast).map(NewsUserDetailDTO::getUser)
                .map(NewsUserSession::getUsername);
    }
}
