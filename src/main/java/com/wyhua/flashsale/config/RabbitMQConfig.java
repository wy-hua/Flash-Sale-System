package com.wyhua.flashsale.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.wyhua.flashsale.constant.MessageQueueConst.PURCHASE_EXCHANGE;
import static com.wyhua.flashsale.constant.MessageQueueConst.PURCHASE_QUEUE;

@Configuration
public class RabbitMQConfig {

    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public Queue purchaseQueue(){return new Queue(PURCHASE_QUEUE,true);}

    @Bean
    public DirectExchange purchaseExchange(){return new DirectExchange(PURCHASE_EXCHANGE,true,false);}

    @Bean
    public  Binding bindingPurchaseDirect(){return BindingBuilder.bind(purchaseQueue()).
            to(purchaseExchange()).with(PURCHASE_QUEUE);
    }
}
