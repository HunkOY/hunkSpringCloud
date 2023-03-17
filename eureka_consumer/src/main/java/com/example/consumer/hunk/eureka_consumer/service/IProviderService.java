package com.example.consumer.hunk.eureka_consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hunkyang
 */
@FeignClient(value = "eureka-provider")
public interface IProviderService {

    @RequestMapping("/provider/service")
    public String service();
}
