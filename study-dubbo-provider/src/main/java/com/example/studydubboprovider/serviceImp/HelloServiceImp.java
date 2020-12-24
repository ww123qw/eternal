package com.example.studydubboprovider.serviceImp;

import com.alibaba.dubbo.config.annotation.Service;
import com.example.studydubbopai.service.HelloService;

@Service(version = "1.1.0")
public class HelloServiceImp implements HelloService {
    @Override
    public String getHello() {
        return "你好，大坏蛋";
    }
}
