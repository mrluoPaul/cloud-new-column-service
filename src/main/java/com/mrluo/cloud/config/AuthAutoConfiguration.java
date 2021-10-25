package com.mrluo.cloud.config;

import com.mrluo.cloud.common.utils.localmap.Sha256PasswordEncoder;
import com.mrluo.cloud.modules.app.controller.AuthController;
import com.mrluo.cloud.modules.app.controller.ColumnController;
import com.mrluo.cloud.modules.app.controller.UserController;
import com.mrluo.cloud.security.AuthAuthenticationProvider;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author LuoJu
 * @date 2021/10/21/0021 10:45
 * @desc
 * @modified
 */
@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthAutoConfiguration implements SecurityConfigure {
    @Autowired
    private AuthAuthenticationProvider authAuthenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Sha256PasswordEncoder();
    }

    @Autowired
    @SneakyThrows
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authAuthenticationProvider);
    }

    @SneakyThrows
    public boolean configure(HttpSecurity http) {
        http.authorizeRequests().antMatchers(
                        AuthController.PREFIX_URI + "/**",
                        UserController.PREFIX_URI + "/**",
                        ColumnController.PREFIX_URI + "/tree",
                        ColumnController.PREFIX_URI + "/detail",
                        "/webjars/**",
                        "/v2/**",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/api/**")
                .permitAll();
        return false;
    }
}
