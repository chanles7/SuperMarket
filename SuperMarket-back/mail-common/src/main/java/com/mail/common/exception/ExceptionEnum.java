package com.mail.common.exception;


import lombok.Getter;

@Getter
public enum ExceptionEnum {

    MethodArgumentNotValidException("CM0001", "数据校验错误"),
    NullPointerException("CM0000", "系统错误"),

    PRODUCT_UP_EXCEPTION("PR0000", "商品上架异常");

    private final String code;
    private final String msg;


    ExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
