package com.mrluo.cloud.common.exception;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @author LuoJu
 * @date 2021/7/1/0001 14:58
 * @desc
 * @modified
 */
public enum FailCodeEnum implements IEnum<String> {
    USER_NOT_EXIST("2001", "用户不存在"),
    USERNAME_REPEAT("2002", "用户名已被注册,请重新输入"),
    EMAIL_REPEAT("2003", "邮箱已被注册,请重新输入");
    private String value;

    private String msg;

    FailCodeEnum(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
