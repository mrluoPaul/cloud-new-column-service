package com.mrluo.cloud.config;

import com.mrluo.cloud.common.defs.NewsDefs;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = NewsDefs.PRODUCT_NAME)
public class AuthProperties {
    private boolean usingAlternatePassword = false;
    private String usernamePattern = "^[A-Za-z0-9]+$";
}
