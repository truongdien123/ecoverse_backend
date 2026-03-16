package com.fpt.ecoverseauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.fpt.ecoverseauth", "com.fpt.ecoversecommon", "com.fpt.ecoverseuser"})
@EntityScan(basePackages = {"com.fpt.ecoverseuser.entities", "com.fpt.ecoversecommon.entity"})
@EnableJpaRepositories(basePackages = {"com.fpt.ecoverseauth.repositories", "com.fpt.ecoverseuser.repositories"})
public class EcoverseAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoverseAuthApplication.class, args);
    }

}
