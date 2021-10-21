package com.mrluo.cloud.common.utils.localmap;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

public class Sha256PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return encodePassword(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Objects.equals(encodePassword(rawPassword), encodedPassword);
    }

    protected String encodePassword(CharSequence rawPassword) {
        return DigestUtils.sha256Hex(rawPassword.toString());
    }
}
