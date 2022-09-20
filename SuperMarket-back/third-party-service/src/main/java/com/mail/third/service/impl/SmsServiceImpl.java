package com.mail.third.service.impl;

import com.mail.common.util.R;
import com.mail.third.service.SmsService;
import com.mail.third.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class SmsServiceImpl implements SmsService {


    @Value("${third.sms.host}")
    private String host;
    @Value("${third.sms.path}")
    private String path;
    @Value("${third.sms.appCode}")
    private String appCode;


    @Override
    public R sendCode(String phone, String code) {
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appCode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        querys.put("param", "**code**:" + code + ",**minute**:5");
        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, String> bodys = new HashMap<String, String>();
        try {
            HttpResponse response = HttpUtils.doPost(host, path, "POST", headers, querys, bodys);
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok();

    }
}
