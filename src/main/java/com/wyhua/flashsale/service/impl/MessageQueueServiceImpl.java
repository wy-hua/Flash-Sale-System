package com.wyhua.flashsale.service.impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueServiceImpl {
    @Autowired
    AmqpTemplate amqpTemplate;
}
