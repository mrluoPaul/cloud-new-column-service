package com.mrluo.cloud.security;


public class AuthLoginHolder {
    private static final ThreadLocal<String> LOGIN_APP_CODE_HOLDER = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<LoginType> LOGIN_TYPE_HOLDER = ThreadLocal.withInitial(() -> null);

    public static void initialize(LoginType loginType) {
        LOGIN_TYPE_HOLDER.set(loginType);
    }

    static LoginType getLoginType() {
        return LOGIN_TYPE_HOLDER.get();
    }

    static String getLoginAppCode() {
        return LOGIN_APP_CODE_HOLDER.get();
    }

    public static boolean isUserCenterSsoAuthCode() {
        return getLoginType() == LoginType.USER_CENTER_SSO_AUTH_CODE;
    }

    public enum LoginType {
        USERNAME_PASSWORD,

        USER_CENTER_SSO_AUTH_CODE
    }
}
