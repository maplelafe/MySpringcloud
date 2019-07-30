package com.ffb.order_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ffb
 * 2018-01-21 21:57
 */
@RestController
@RequestMapping("/env")
public class EnvController {

//    @Value("${env}")
//    private String env;

//    @GetMapping("/print")
//    private String print() {
//        return env;
//    }
}
