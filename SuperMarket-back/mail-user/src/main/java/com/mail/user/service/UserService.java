package com.mail.user.service;

import com.mail.common.util.R;
import com.mail.user.vo.req.UserReqVO;

import javax.servlet.ServletRequest;

public interface UserService {

    R sendCode(String sendCode);


    R register(UserReqVO userVO);


    R login(UserReqVO userVO);


    R getUserIndo(ServletRequest req);


    R logout(ServletRequest req);
}
