package com.example.consumer.hunk.eureka_consumer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author hunkyang
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerProvider {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/service")
    public String service(){
        System.out.println("consumer service....");
        ResponseEntity<String> forEntity =
                restTemplate.getForEntity("http://eureka-provider/provider/service", String.class);
        return forEntity.getBody();
    }
}
