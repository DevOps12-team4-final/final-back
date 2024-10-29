package com.bit.finalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class finalprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(finalprojectApplication.class, args);
    }

}
