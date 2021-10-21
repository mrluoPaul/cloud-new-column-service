package com.mrluo.cloud.common.exception;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @author LuoJu
 * @date 2021/10/21/0021 11:04
 * @desc
 * @modified
 */
public enum CodeConstants implements IEnum<String> {
    SUCCESS("0000"),

    /**
     * 通用失败.
     */
    FAIL("0001"),

    /**
     * 数据库异常
     */
    DB_EXCEPTION("0002"),

    /**
     * 无效的参数.
     */
    INVALID_ARGUMENTS("0003"),

    /**
     * 字段不能为空.
     */
    FIELD_CAN_NOT_NULL("0004"),

    /**
     * 对象不存在.
     */
    OBJECT_NOT_EXIST("0005"),

    /**
     * 字段不能为null.
     */
    FIELD_NOT_NULL("0006"),

    /**
     * 字段类型不正确.
     */
    FIELD_TYPE_ERROR("0007"),

    /**
     * 字段超长.
     */
    FIELD_OVER_LONG("0008"),

    /**
     * 参数长度不相等
     */
    LENGTH_NOT_EQ("0009"),

    /**
     * 令牌过期
     */
    TOEKN_EXPIRE("0010"),

    /**
     * 令牌错误
     */
    TOKEN_ERROR("0011"),

    /**
     * 项目不存在.
     */
    PROJECT_NOT_EXIST("0012"),

    /**
     * 查询对象已存在.
     */
    OBJECT_EXIST("0013"),

    /**
     * 错误的返回多个结果
     */
    DB_MORE_RESULT("0014"),

    /**
     * 表单验证失败
     */
    FORM_VALIDATION_FAIL("0015"),

    /**
     * 验证码错误
     */
    VERIFICATION_CODE("0016"),


    ;

    /**
     * 错误编码
     */
    private String code;

    CodeConstants(String code) {
        this.code = code;
    }


    @Override
    public String getValue() {
        return this.code;
    }
}
