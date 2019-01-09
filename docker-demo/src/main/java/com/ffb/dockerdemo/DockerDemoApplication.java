package com.ffb.dockerdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Controller
public class DockerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerDemoApplication.class, args);
    }
    @GetMapping("/docker/test")
    @ResponseBody
    public Object get(){

        Map<String,Object> map =new HashMap<String,Object>();
        map.put("name","tom");
        map.put("age",123);
        return map;
    }
}
