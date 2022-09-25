package com.mail.order.config;

import com.mail.order.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * description 功能描述
 *
 * @author ch
 * create 2022/9/23 5:47
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {


    @Bean
    public AuthInterceptor authInterceptor(){
        return new AuthInterceptor();
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor()).addPathPatterns("/**");
    }
}

