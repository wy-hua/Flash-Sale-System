package com.wyhua.flashsale.controller;

import com.wyhua.flashsale.dto.OrderDto;
import com.wyhua.flashsale.dto.Result;
import com.wyhua.flashsale.enums.ResultState;
import com.wyhua.flashsale.exception.BaseException;
import com.wyhua.flashsale.service.FlashSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            return new Result<String>(baseException.getState(),null);
        }
        return new Result<String>(ResultState.SUCCESS,url);

    }

//    @PostMapping()
//    public Result<OrderDto> purchase()




}
