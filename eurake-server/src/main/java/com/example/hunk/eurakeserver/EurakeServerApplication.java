package com.example.hunk.eurakeserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author hunkyang
 */
@SpringBootApplication
@EnableEurekaServer
public class EurakeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurakeServerApplication.class, args);
    }

}
