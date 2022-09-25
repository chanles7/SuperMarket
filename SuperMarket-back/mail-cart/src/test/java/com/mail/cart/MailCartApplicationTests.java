package com.mail.cart;

import com.mail.cart.util.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;



@RunWith(SpringRunner.class)
@SpringBootTest
public class MailCartApplicationTests {

	@Resource
	private StringRedisTemplate stringRedisTemplate;
	@Resource
	private RedisUtils redisUtils;

	@Test
	public void contextLoads() {
		System.out.println(redisUtils);
	}

}
