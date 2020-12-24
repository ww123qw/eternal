package com.rocketmq.rocketmq.Controller;

import com.rocketmq.rocketmq.rocket.ConsumerConfig;
import com.rocketmq.rocketmq.service.RocketMqService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RocketController {

    @Resource
    private RocketMqService rocketMqService ;
    @Resource
    private ConsumerConfig consumerConfig ;
    @Resource
    DefaultMQPushConsumer pushConsumer;
    @RequestMapping("/sendMsg")
    public SendResult sendMsg (){
//        consumerConfig.getRocketMQConsumer();
        String msg = "OpenAccount Msg";
        SendResult sendResult = null;
        try {
            sendResult = rocketMqService.openAccountMsg(msg) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendResult ;
    }

}
