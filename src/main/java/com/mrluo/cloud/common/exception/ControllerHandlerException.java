package com.mrluo.cloud.common.exception;

import com.mrluo.cloud.common.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author LuoJu
 * @date 2021/10/21 21:27
 **/
@ControllerAdvice
@Slf4j
public class ControllerHandlerException {
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData handleException(BusinessException exception) {
        return ResponseData.fail(exception.getMessage(), exception.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData handleException(MethodArgumentNotValidException exception) {
        return ResponseData.fail(exception.getMessage());
    }
}
