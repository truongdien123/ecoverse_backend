package com.fpt.ecoverseapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.fpt")
@EntityScan(basePackages = "com.fpt")
@EnableJpaRepositories(basePackages = "com.fpt")
public class EcoverseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoverseApiApplication.class, args);
    }

}
