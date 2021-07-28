package com.xat.simple.isolate;

import org.springframework.boot.SpringApplication;

/**
 * @author 淡漠
 */
public class ApplicationRun {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ApplicationRun.class);
        springApplication.run(args);
    }
}
