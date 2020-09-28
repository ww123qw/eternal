package com.rocketmq.rocketmq.service;


import org.apache.rocketmq.client.producer.SendResult;

public interface RocketMqService {
    SendResult openAccountMsg(String msgInfo) ;
}
