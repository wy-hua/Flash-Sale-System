package com.wyhua.flashsale.consumer;

import com.wyhua.flashsale.dto.PurchaseMsg;
import com.wyhua.flashsale.service.FlashSaleService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.wyhua.flashsale.constant.MessageQueueConst.PURCHASE_QUEUE;

@RabbitListener(queues = PURCHASE_QUEUE)

@Component
public class PurchaseConsumer {
    @Autowired
    private FlashSaleService flashSaleService;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @RabbitHandler
    public void processPurchaseMessage(PurchaseMsg msg){
        flashSaleService.makePurchaseInDataBase(msg.getProductId(), msg.getUserPhone(),msg.getPurchaseTime());
    }
}
