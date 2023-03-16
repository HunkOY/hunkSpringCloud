package com.example.provider.hunk.eurekaprovider.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hunkyang
 *
 */
@RestController
@RequestMapping("/provider")
public class ProviderController {

    @RequestMapping("service")
    public String service(){
        System.out.println("eureka_provider service...");
        return "hello,this info from the eureka_provider";
    }
}
