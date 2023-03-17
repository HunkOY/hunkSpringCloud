package com.example.consumer.hunk.eureka_consumer.controller;


import com.example.consumer.hunk.eureka_consumer.service.IProviderService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author hunkyang
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerProviderController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IProviderService iProviderService;

    @HystrixCommand(fallbackMethod = "serviceFallback")
    @RequestMapping("/service")
    public String service(){
        System.out.println("consumer service....");
//        ResponseEntity<String> forEntity =
//                restTemplate.getForEntity("http://eureka-provider/provider/service", String.class);
//        return forEntity.getBody();
        return iProviderService.service();
    }



    /**
     * 回退方法
     * @return
     */
    public String serviceFallback(){
        System.out.println("consumer serviceFallback....");
        System.out.println("启动熔断机制....");
        return "快速返回信息";
    }
}
