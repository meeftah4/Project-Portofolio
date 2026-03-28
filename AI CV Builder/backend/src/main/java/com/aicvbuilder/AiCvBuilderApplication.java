package com.aicvbuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AiCvBuilderApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCvBuilderApplication.class, args);
    }

}
