package com.mrluo.cloud.security;


import com.mrluo.cloud.common.utils.localmap.LMapCache;
import com.mrluo.cloud.common.utils.localmap.NewsSecurityUtil;
import org.redisson.api.RMapCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class NewsWebSessionService extends OncePerRequestFilter {
    public static final String TOKEN_HEADER_KEY = "AccessToken";
    private static final String SPRING_SECURITY_CONTEXT = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ServerProperties properties;

    @Autowired
    @Qualifier("tokenToAuthenticationStore")
    private LMapCache<String, UsernamePasswordAuthenticationToken> tokenToAuthenticationStore;

    @Autowired
    @Qualifier("userNameToTokenStore")
    private RMapCache<String, String> userNameToTokenStore;

    private Duration sessionTimeout;

    @PostConstruct
    void initialize() {
        sessionTimeout = properties.getServlet().getSession().getTimeout();
    }

    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (HttpMethod.resolve(request.getMethod()) == HttpMethod.OPTIONS) {
            String origin = request.getHeader("Origin");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Origin", origin);
            response.addHeader("Access-Control-Allow-Methods", "*");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type,X,AccessToken");
            response.getWriter().println("ok");
            return;
        }
        String accessToken = request.getHeader(TOKEN_HEADER_KEY);
        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        Authentication authentication = tokenToAuthenticationStore.get(accessToken);
        if (authentication != null) {
            attachAuthentication(authentication, request);

            NewsSecurityUtil.getCurrentUserName().ifPresent(username -> {
                String existedAccessToken = userNameToTokenStore.get(username);
                if (ObjectUtils.isEmpty(existedAccessToken) || !Objects.equals(existedAccessToken, accessToken)) {
                    userNameToTokenStore.fastPutAsync(username, accessToken);
                }
            });

        }
        filterChain.doFilter(request, response);
    }

    public String login(String authCode, HttpServletRequest request) {
        return tryLogin(UUID.randomUUID().toString(), authCode, request);
    }

    public String login(String username, String password, HttpServletRequest request) {
        return tryLogin(username, password, request);
    }

    private String tryLogin(String principal, String credentials, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationResult = (UsernamePasswordAuthenticationToken)
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(principal, credentials));

        String newSessionToken = UUID.randomUUID().toString();

        NewsSecurityUtil.getUserName(authenticationResult)
                .ifPresent(userName -> userNameToTokenStore.fastPut(principal, newSessionToken,
                        0L, null, sessionTimeout.toMillis(), TimeUnit.MILLISECONDS));

        tokenToAuthenticationStore.fastPut(newSessionToken, authenticationResult,
                0L, sessionTimeout.toMillis());

        attachAuthentication(authenticationResult, request);

        return newSessionToken;
    }

    public void logout(HttpServletRequest request) throws ServletException {
        String accessToken = request.getHeader(TOKEN_HEADER_KEY);
        request.logout();
        request.getSession().invalidate();
        if (StringUtils.hasText(accessToken)) {
            NewsSecurityUtil.getUserName(tokenToAuthenticationStore.remove(accessToken))
                    .ifPresent(userNameToTokenStore::fastRemove);
        }
    }


    private void attachAuthentication(Authentication authentication, HttpServletRequest request) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT, authentication);
    }
}
