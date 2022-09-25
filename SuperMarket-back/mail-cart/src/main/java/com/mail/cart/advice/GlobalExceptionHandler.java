package com.mail.cart.advice;


import com.mail.common.exception.ExceptionEnum;
import com.mail.common.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleException(MethodArgumentNotValidException e) {
        log.error("数据校验错误");
        Map<String, String> map = new HashMap<>();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        allErrors.forEach(item -> {
            map.put(item.getObjectName(), item.getDefaultMessage());
        });
        log.error("错误信息为:{}", map);
        return R.error(ExceptionEnum.MethodArgumentNotValidException).put("data", map);
    }


    @ExceptionHandler(Throwable.class)
    public R handleException(Throwable e) {
        log.error("系统发生错误，错误信息为:{}", e.getClass());
        e.printStackTrace();
        return R.error(ExceptionEnum.NullPointerException);
    }

}
