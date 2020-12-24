package com.example.studydubboconsumer.cotroller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.studydubbopai.service.HelloService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloCotroller {
   @Reference(version = "1.1.0")
    private HelloService helloService ;
    @RequestMapping("/getUser")
    public String  getUserList() {
        return helloService.getHello();
    }
}
