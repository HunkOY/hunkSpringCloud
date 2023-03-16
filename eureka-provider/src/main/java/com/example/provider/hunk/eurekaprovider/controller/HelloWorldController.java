package com.example.provider.hunk.eurekaprovider.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hunkyang
 */
@RestController
@RequestMapping(value = "/hunk")
public class HelloWorldController {

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String HelloWorld(){
        return "eureka-provider Hello World";
    }
}
