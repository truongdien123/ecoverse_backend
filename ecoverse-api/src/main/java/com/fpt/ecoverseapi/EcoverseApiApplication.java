package com.fpt.ecoverseapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.fpt")
public class EcoverseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoverseApiApplication.class, args);
    }

}
