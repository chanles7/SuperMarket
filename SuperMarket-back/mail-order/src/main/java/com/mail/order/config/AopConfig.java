package com.mail.order.config;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@Aspect
@Component
public class AopConfig {

    private Long start;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String className;
    private String methodName;


    @Pointcut("execution (* com.mail.order.controller.*.*(..))")
    public void test() {

    }


    @Before("test()")
    public void beforeAdvice(JoinPoint joinpoint) {
        this.start = System.currentTimeMillis();
        String className = joinpoint.getTarget().getClass().getName();
        this.className = className.substring(className.lastIndexOf(".") + 1);
        this.methodName = joinpoint.getSignature().getName();
        log.info("{}的{}方法调用开始，开始时间：{}", this.className, this.methodName, LocalDateTime.now().format(dateTimeFormatter));
    }


    @AfterReturning("test()")
    public void afterAdvice() {
        log.info("{}的{}方法调用结束，结束时间：{}", this.className, this.methodName, LocalDateTime.now().format(dateTimeFormatter));
        Long end = System.currentTimeMillis();
        log.info("{}的{}方法共执行了{}ms", this.className, this.methodName, end - start);
    }

}
