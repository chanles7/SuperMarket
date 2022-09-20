package com.mail.user.vo.req;


import lombok.Data;

@Data
public class UserReqVO {

    private String username;

    private String password;

    private String confPassword;

    private String phone;

    private String code;

    private String openid;
}
