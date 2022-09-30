package com.wyhua.flashsale.controller;

import com.wyhua.flashsale.dto.OrderDto;
import com.wyhua.flashsale.dto.Result;
import com.wyhua.flashsale.entity.ProductInfo;
import com.wyhua.flashsale.enums.ResultState;
import com.wyhua.flashsale.exception.BaseException;
import com.wyhua.flashsale.service.FlashSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FlashSaleController {
    @Autowired
    FlashSaleService flashSaleService;

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
    public Result<Integer> getProductAmount(@PathVariable("productId") long id){
        return new Result<>(ResultState.SUCCESS,flashSaleService.getProductAmountFromCache(id));
    }

    @PostMapping("/flashSale/order")
    public Result<OrderDto> purchase(@RequestParam(value = "userPhone") String  userPhone,
                                    @RequestParam("productId") long productId,@RequestParam("saleUrl") String md5){
        OrderDto orderDto;
        try {
            orderDto= flashSaleService.makePurchase(productId,userPhone,md5);
        }
        catch (BaseException e){
            return new Result<>(e.getResultState(),null);
        }
        return new Result<>(ResultState.SUCCESS,orderDto);
    }
//    @PostMapping()
//    public Result<OrderDto> purchase()




}
