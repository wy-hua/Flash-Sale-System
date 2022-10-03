package com.wyhua.flashsale.controller;

import com.wyhua.flashsale.dto.OrderDto;
import com.wyhua.flashsale.dto.PurchaseMsg;
import com.wyhua.flashsale.dto.Result;
import com.wyhua.flashsale.entity.ProductInfo;
import com.wyhua.flashsale.enums.ResultState;
import com.wyhua.flashsale.exception.BaseException;
import com.wyhua.flashsale.service.FlashSaleService;
import org.aspectj.weaver.ast.Or;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Result<String> getSaleUrl(@PathVariable("productId") long id){
        String url;
        try{
            url=flashSaleService.getSaleUrl(id);

        }
        catch (BaseException baseException){
            return new Result<>(baseException.getResultState(),null);
        }
        return new Result<>(ResultState.SUCCESS,url);

    }

    @GetMapping("/flashSale/products/info")
    public  Result<List<ProductInfo>> getAllProductInfo(){
        return new Result<>(ResultState.SUCCESS,flashSaleService.findAllInfo());
    }

    @GetMapping("/flashSale/products/{productId}/amount")
    public Result<Long> getProductAmount(@PathVariable("productId") long id){
        return new Result<>(ResultState.SUCCESS,flashSaleService.getProductAmount(id));
    }

    @PostMapping("/flashSale/order")
    public Result<String> purchase(@RequestParam(value = "userPhone") String  userPhone,
                                    @RequestParam("productId") String productIdStr,@RequestParam("saleUrl") String md5){

        long productId=Long.valueOf(productIdStr);
        Result<String> result;
        try{
            flashSaleService.decrementProductAmountInCache(productId,md5);
            PurchaseMsg purchaseMsg=new PurchaseMsg(productId,userPhone,md5,new Date());
            rabbitTemplate.convertAndSend(PURCHASE_EXCHANGE,PURCHASE_QUEUE,purchaseMsg);


            result= new Result<>(ResultState.SUCCESS,null);
        }catch (BaseException e){
            result=new Result<>(e.getResultState(),null);
        }
        return  result;


//        try {
//            flashSaleService.makePurchase(productId,userPhone,md5);
//        }
//        catch (BaseException e){
//            return new Result<>(e.getResultState(),null);
//        }
//        return new Result<>(ResultState.SUCCESS,orderDto);
    }
//    @PostMapping()
//    public Result<OrderDto> purchase()




}
