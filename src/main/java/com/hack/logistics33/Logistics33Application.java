package com.hack.logistics33;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class Logistics33Application extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Logistics33Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Logistics33Application.class, args);
    }


}
//@SpringBootApplication
//public class Logistics33Application {
//    public static void main(String[] args) {
//        SpringApplication.run(Logistics33Application.class);
//    }
//}