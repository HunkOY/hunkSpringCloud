package com.example.hunk.eurekaprovider1.eurekaprovider1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author hunkyang
 */
@SpringBootApplication
@EnableEurekaClient
public class EurekaProvider1Application {

    public static void main(String[] args) {
        SpringApplication.run(EurekaProvider1Application.class, args);
    }

}
