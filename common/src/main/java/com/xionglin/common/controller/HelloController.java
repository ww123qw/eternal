package com.xionglin.common.controller;

import com.xionglin.common.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "日志注解",description = "日志注解")
public class HelloController {

    @PostMapping("/hello")
    @ApiOperation(value = "日志注解")
    @Log(title = "日志注解")
    public String hello(String oneParam,String twoParam){

        return oneParam+twoParam;
    }
}
