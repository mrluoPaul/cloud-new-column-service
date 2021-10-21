package com.mrluo.cloud.common.exception;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @author LuoJu
 * @date 2021/7/1/0001 14:58
 * @desc
 * @modified
 */
public enum FailCodeEnum implements IEnum<String> {
    //    CODE_NAME_REPEAT("2001", "编码或者名称重复"),
//    DIC_NOT_EXIST("2002", "字典不存在"),
    ROLE_NAME_REPEAT("2003", "角色名称重复"),
    ROLE_NOT_EXIST("2004", "角色不存在"),
    USER_REPEAT("2005", "用户重复"),
    USER_NOT_EXIST("2006", "用户不存在"),
    RESOURCE_NAME_OR_CODE_REPEAT("2007", "资源名称或者code重复"),
    RESOURCE_NOT_EXIST("2008", "资源不存在"),
    RESOURCE_ID_NOT_NULL("2009", "资源id不能为空"),
    RESOURCE_HAVE_CHILDREN("2010", "该资源下有子级资源,不允许删除"),
    APP_CODE_NOT_NULL("2011", "系统code不能为空"),
    ROLE_HAVE_USER("2012", "该角色下有人员关联,无法删除"),
    ROLE_IS_NOT_DISABLE("2013", "该角色是启用状态,无法删除"),
    USER_IS_ENABLE("2014", "该用户已被禁用,无法登录"),
    ORG_TREE_IS_NULL("2015", "获取组织架构为空"),
    LINE_IS_NULL("2016", "条线数据为空");
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
