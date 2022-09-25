package com.mail.user.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mail.common.util.CommonUtils;
import com.mail.common.util.R;
import com.mail.user.entity.MemberEntity;
import com.mail.user.feign.ThirdFeignService;
import com.mail.user.service.MemberService;
import com.mail.user.service.UserService;
import com.mail.user.util.RedisUtils;
import com.mail.user.vo.req.UserReqVO;
import com.mail.user.vo.resp.UserRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.util.concurrent.TimeUnit;

import static com.mail.user.constant.UserConstant.SMS_CODE_CACHE_PREFIX;
import static com.mail.user.constant.UserConstant.USER_LOGIN_CACHE_PREFIX;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private ThirdFeignService thirdFeignService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MemberService memberService;
    @Resource
    private RedisUtils redisUtils;

    @Override
    public R sendCode(String phone) {
        String phoneKey = SMS_CODE_CACHE_PREFIX + phone;
        String value = redisUtils.get(phoneKey);
        if (StringUtils.isNotBlank(value)) {
            long ago = Long.parseLong(value.split("_")[1]);
            long now = System.currentTimeMillis();
            if (now - ago < 60 * 1000) {
                return R.error("发送请求次数频繁，请一分钟后再试");
            }
        }
        String code = CommonUtils.generateRandomNumber(6);
        log.info("验证码为:{},10分钟内有效", code);
        String param = code + "_" + System.currentTimeMillis();
        redisUtils.set(SMS_CODE_CACHE_PREFIX + phone, param, 10L, TimeUnit.MINUTES);
//        String code = "";
//        R r = thirdFeignService.sendCode(phone, code);
        return R.ok();
    }


    @Override
    public R register(UserReqVO userVO) {
        log.info("注册用户信息为:{}", userVO);
        String s = stringRedisTemplate.opsForValue().get(SMS_CODE_CACHE_PREFIX + userVO.getPhone());
        if (StringUtils.isBlank(s)) {
            return R.error("验证码已失效");
        }
        String code = s.split("_")[0];
        if (!code.equals(userVO.getCode())) {
            return R.error("验证码不匹配");
        }
        if (StringUtils.isBlank(userVO.getPassword()) || StringUtils.isBlank(userVO.getConfPassword())) {
            return R.error("密码不能为空");
        }
        if (!userVO.getPassword().equals(userVO.getConfPassword())) {
            return R.error("两次密码不一致");
        }
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("临时用户" + UUID.randomUUID().toString().substring(0, 8));
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(userVO.getPassword());
        memberEntity.setPassword(password);
        memberEntity.setMobile(userVO.getPhone());
        memberService.save(memberEntity);
        redisUtils.delete(SMS_CODE_CACHE_PREFIX + userVO.getPhone());
        return R.ok();
    }


    @Override
    public R login(UserReqVO userVO) {
        log.info("登录用户信息为:{}", userVO);
        MemberEntity member = memberService.getMemberByPhone(userVO.getPhone());
        if (member == null) {
            return R.error("请先注册");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(userVO.getPassword(), member.getPassword())) {
            return R.error("密码错误");
        }
        String uuid = UUID.randomUUID().toString();
        redisUtils.set(USER_LOGIN_CACHE_PREFIX + uuid, member.getId().toString(), 30L, TimeUnit.DAYS);
        return R.ok().put("data", uuid);
    }


    @Override
    public R getUserIndo(ServletRequest req) {
        String token = (String) req.getAttribute("token");
        if (token == null) {
            R.error();
        }
        UserRespVO userRespVO = new UserRespVO();
        userRespVO.setNickname("游客1997");
        userRespVO.setActor("");
        return R.ok(userRespVO);
    }


    @Override
    public R logout(ServletRequest req) {
        String token = (String) req.getAttribute("token");
        if ("*".equals(token)) {
            return R.error();
        }
        redisUtils.delete(USER_LOGIN_CACHE_PREFIX + token);
        return R.ok();
    }
}
