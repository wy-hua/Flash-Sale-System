package com.wyhua.flashsale.service.impl;

import com.wyhua.flashsale.dto.OrderDto;
import com.wyhua.flashsale.entity.ProductInfo;
import com.wyhua.flashsale.enums.ResultState;
import com.wyhua.flashsale.exception.BaseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class FlashSaleServiceImplTest {
    @Autowired
    private FlashSaleServiceImpl flashSaleService;

    @Test
    void findAllInfo() {
        List<ProductInfo> productInfoList= flashSaleService.findAllInfo();
        assertEquals(productInfoList.size(),4);
    }

    @Test
    void getSaleUrl() {
        assertNotNull(flashSaleService.getSaleUrl(1003));
        try{
            flashSaleService.getSaleUrl(1002);
        }catch( BaseException exception){
            System.out.println(exception.getState().toString());
            assertEquals(exception.getState(), ResultState.SALE_OVER);
        }
    }

//    test existing products and products that don't exist
    @Test
    void getProductInfo() {
        List<ProductInfo> productInfoList= flashSaleService.findAllInfo();
        for (int i = 0; i < productInfoList.size(); i++) {
            ProductInfo productInfo=productInfoList.get(i);
            assertEquals(flashSaleService.getProductInfoFromCache(productInfo.getProductId()),productInfo);
        }
    }

    @Test
    void getProductAmount() {
        List<ProductInfo> productInfoList= flashSaleService.findAllInfo();
        for (int i = 0; i < productInfoList.size(); i++) {
            ProductInfo productInfo = productInfoList.get(i);
            assertEquals(flashSaleService.getProductAmountFromCache(productInfo.getProductId()),productInfo.getAmount());
        }

    }

    @Test
    void makePurchase() {
        try{
            flashSaleService.makePurchase(1002,"15012345678", flashSaleService.getMD5(1002));
        }catch( BaseException exception){
            System.out.println(exception.getState().toString());
            assertEquals(exception.getState(), ResultState.SALE_OVER);
        }
        int amount=flashSaleService.getProductAmountFromCache(1003);
        OrderDto orderDto=  flashSaleService.makePurchase(1003,"15012345678", flashSaleService.getMD5(1003));
        assertEquals(flashSaleService.getProductAmountFromCache(1003),amount-1);
        assertEquals(orderDto.getUserPhone(),"15012345678");
        System.out.println(orderDto.toString());
    }
}