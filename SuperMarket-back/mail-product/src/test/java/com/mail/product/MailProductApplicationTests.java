package com.mail.product;

import com.mail.product.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.DecimalFormat;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class MailProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void contextLoads() {
        int aa = (int) (Math.random() * 100000000);
        String num = String.format("%08d", aa);


        DecimalFormat decimalFormat = new DecimalFormat("00000000");

        String numFormat= decimalFormat .format(Math.random() * 100000000);
//        Integer num =

        log.info("{}",num);
        log.info("{}",numFormat);

        String a = stringRedisTemplate.opsForValue().get("a");
        log.info("a:{}", a);
    }


}
