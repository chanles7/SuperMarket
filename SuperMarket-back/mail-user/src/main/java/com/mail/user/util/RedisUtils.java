package com.mail.user.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {


    @Resource
    private StringRedisTemplate stringRedisTemplate;


    //=================== String ======================

    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, Long time, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, time, timeUnit);
    }


    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }


    public void delete(String... keys) {
        for (String key : keys) {
            stringRedisTemplate.delete(key);
        }
    }
}
