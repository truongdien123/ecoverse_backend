package com.fpt.ecoverseuser;

import com.fpt.ecoversecommon.config.SecurityConfig;
import com.fpt.ecoversecommon.util.UploadFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SecurityConfig.class, UploadFile.class})
public class EcoverseUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoverseUserApplication.class, args);
    }

}
