package com.mail.product;

import com.mail.product.entity.BrandEntity;
import com.mail.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MailProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Test
    void contextLoads() {
        List<BrandEntity> list = brandService.query().list();
        list.forEach(System.err::println);
    }

}
