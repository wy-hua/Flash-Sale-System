package com.wyhua.flashsale.controller;

import com.wyhua.flashsale.dto.PurchaseMsg;
import com.wyhua.flashsale.entity.ProductInfo;
import com.wyhua.flashsale.exception.WrongSaleUrlException;
import com.wyhua.flashsale.service.FlashSaleService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.wyhua.flashsale.constant.MessageQueueConst.PURCHASE_EXCHANGE;
import static com.wyhua.flashsale.constant.MessageQueueConst.PURCHASE_QUEUE;

@RestController
public class FlashSaleController {
    @Autowired
    FlashSaleService flashSaleService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/flashSale/products/{productId}/saleUrl")
    public ResponseEntity<String> getSaleUrl(@PathVariable("productId") long id){
        String url;
        url=flashSaleService.getSaleUrl(id);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/flashSale/products/info")
    public  ResponseEntity<List<ProductInfo>> getAllProductInfo(){
        return ResponseEntity.ok(flashSaleService.findAllInfo());
    }

    @GetMapping("/flashSale/products/{productId}/amount")
    public ResponseEntity<Long> getProductAmount(@PathVariable("productId") long id){
        return ResponseEntity.ok(flashSaleService.getProductAmount(id));
    }

    @PostMapping("/flashSale/order")
    public ResponseEntity<Object> purchase(@RequestParam(value = "userPhone") String  userPhone,
                                    @RequestParam("productId") String productIdStr,@RequestParam("saleUrl") String md5){

        long productId=Long.valueOf(productIdStr);
        if(!flashSaleService.isSaleUrlValid(productId,md5))
            throw new WrongSaleUrlException(productId);
        flashSaleService.decrementProductAmountInCache(productId);
        PurchaseMsg purchaseMsg=new PurchaseMsg(productId,userPhone,new Date());
        rabbitTemplate.convertAndSend(PURCHASE_EXCHANGE,PURCHASE_QUEUE,purchaseMsg);
        return  ResponseEntity.ok(null);
    }
}
