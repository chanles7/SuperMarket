package com.mail.cart.interceptor;


import com.mail.cart.util.RedisUtils;
import com.mail.common.vo.LoginUserVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.mail.common.constant.UserConstant.USER_LOGIN_CACHE_PREFIX;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private RedisUtils redisUtils;

    public static ThreadLocal<LoginUserVO> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            return false;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        String s = redisUtils.get(USER_LOGIN_CACHE_PREFIX + token);
        if (StringUtils.isNotBlank(s)) {
            loginUserVO.setUserId(s.split("_")[0]);
        } else {
            loginUserVO.setUserKey(token);
        }
        threadLocal.set(loginUserVO);
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        threadLocal.remove();
    }


    public static LoginUserVO getUser() {
        return threadLocal.get();
    }
}
