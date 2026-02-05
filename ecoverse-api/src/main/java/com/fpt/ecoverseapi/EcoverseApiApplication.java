package com.fpt.ecoverseapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.fpt")
public class EcoverseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoverseApiApplication.class, args);
    }

}
