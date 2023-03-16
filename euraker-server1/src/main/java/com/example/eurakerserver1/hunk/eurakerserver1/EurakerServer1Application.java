package com.example.eurakerserver1.hunk.eurakerserver1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class EurakerServer1Application {

    public static void main(String[] args) {
        SpringApplication.run(EurakerServer1Application.class, args);
    }

}
