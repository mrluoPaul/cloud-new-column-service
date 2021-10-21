package com.mrluo.cloud.security;


public class AuthLoginHolder {
    private static final ThreadLocal<String> LOGIN_APP_CODE_HOLDER = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<LoginType> LOGIN_TYPE_HOLDER = ThreadLocal.withInitial(() -> null);

    public static void initialize(LoginType loginType) {
        LOGIN_TYPE_HOLDER.set(loginType);
    }

    public enum LoginType {
        USERNAME_PASSWORD
    }
}
