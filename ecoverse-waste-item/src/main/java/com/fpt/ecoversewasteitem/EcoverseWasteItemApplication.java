package com.fpt.ecoversewasteitem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// NOTE: This main class is only for standalone testing.
// When used as a module inside ecoverse-api, it is NOT the entry point.
@SpringBootApplication
public class EcoverseWasteItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoverseWasteItemApplication.class, args);
    }
}
