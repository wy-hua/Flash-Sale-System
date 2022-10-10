package com.wyhua.flashsale.service.impl;

import com.wyhua.flashsale.entity.ProductInfo;
import com.wyhua.flashsale.exception.SaleIsOverException;
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
        }catch( RuntimeException exception){
            assertTrue(exception instanceof SaleIsOverException);
        }
    }

//    test existing products and products that don't exist
    @Test
    void getProductInfo() {
        List<ProductInfo> productInfoList= flashSaleService.findAllInfo();
        for (ProductInfo productInfo : productInfoList) {
            assertEquals(flashSaleService.getProductInfo(productInfo.getProductId()),productInfo);
        }
    }

    @Test
    void getProductAmount() {
        List<ProductInfo> productInfoList= flashSaleService.findAllInfo();
        for (ProductInfo productInfo : productInfoList) {
            assertEquals(flashSaleService.getProductAmount(productInfo.getProductId()),productInfo.getAmount());
        }

    }

    @Test
    void makePurchase() {

    }

    @Test
    void TestMq(){

    }
}