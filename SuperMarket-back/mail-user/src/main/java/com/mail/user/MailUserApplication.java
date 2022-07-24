package com.mail.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MailUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailUserApplication.class, args);
    }

}
