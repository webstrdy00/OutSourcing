package com.sparta.spring26;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class Spring26Application {

    public static void main(String[] args) {
        SpringApplication.run(Spring26Application.class, args);
    }

}
