package com.example.hunk.eurekaprovider1.eurekaprovider1.controller;

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
        return "eureka-provider1 Hello World";
    }
}
