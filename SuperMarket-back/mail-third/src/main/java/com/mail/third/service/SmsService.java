package com.mail.third.service;


import com.mail.common.util.R;
import org.springframework.stereotype.Service;

@Service
public interface SmsService {

    R sendCode(String phone, String code);
}
