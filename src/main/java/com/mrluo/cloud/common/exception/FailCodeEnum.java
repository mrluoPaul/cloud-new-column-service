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
    EMAIL_REPEAT("2003", "邮箱已被注册,请重新输入"),
    COLUMN_NAME_REPEAT("2004", "栏目标题重复,请重新输入栏目标题"),
    ID_IS_NULL("2005", "修改时主键id不能为空"),
    COLUMN_OR_ARTICLE_IS_NULL("2006", "栏目或者文章不存在"),
    PARENT_IS_NULL("2007", "父级id不能为空"),
    COLUMN_NAME_IS_ERR("2008", "二级栏目标题必须与一级栏目标题一致");
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
