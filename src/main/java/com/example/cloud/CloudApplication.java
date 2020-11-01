package com.example.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ServletComponentScan
public class CloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class, args);
    }

}
