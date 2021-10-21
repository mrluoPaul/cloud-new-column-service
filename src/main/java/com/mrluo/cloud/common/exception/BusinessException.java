package com.mrluo.cloud.common.exception;

import com.baomidou.mybatisplus.core.enums.IEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LuoJu
 * @date 2021/10/21/0021 10:56
 * @desc
 * @modified
 */
@Getter
@Setter
public class BusinessException extends RuntimeException{
    private static final long serialVersionUID = -3537109711029547957L;

    /**
     * 错误代码.
     */
    private IEnum<String> errorCode;


    public BusinessException() {
        super();
    }


    public BusinessException(String message) {
        super(message);
    }


    public BusinessException(IEnum<String> errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String message, Throwable cause, IEnum<String> errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
