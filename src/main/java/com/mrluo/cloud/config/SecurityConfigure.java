package com.mrluo.cloud.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

public interface SecurityConfigure {
    /**
     * @param web
     * @return if next configure should skipped
     */
    default boolean configure(WebSecurity web) {
        return false;
    }

    /**
     * @param http
     * @return if next configure should skipped
     */
    default boolean configure(HttpSecurity http) {
        return false;
    }
}
