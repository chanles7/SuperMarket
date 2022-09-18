/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.mail.common.util;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mail.common.exception.ExceptionEnum;
import lombok.Data;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;


public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;


    public R() {
        put("code", 200);
        put("msg", "success");
    }


    public <T> T getData(TypeReference<T> typeReference) {
        Object data = this.get("data");
        return JSON.parseObject(JSON.toJSONString(data), typeReference);
    }


    public static R error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }


    public static R error(ExceptionEnum exceptionEnum) {
        R r = new R();
        r.put("code", exceptionEnum.getCode());
        r.put("msg", exceptionEnum.getMsg());
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public static R ok(Object data) {
        R r = new R();
        r.put("data", data);
        return r;
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public R put(String msg) {
        super.put("msg", msg);
        return this;
    }

    public Integer getCode() {
        return (Integer) this.get("code");
    }

}
