package com.wyhua.flashsale.exception;

import com.wyhua.flashsale.entity.ProductInfo;

public class OutOfStockException extends  RuntimeException {
    public  OutOfStockException(ProductInfo productInfo){
        super("The product(Id: {%d}, Name: {%s}) you want to purchase is out of stock now"
                .formatted(productInfo.getProductId(),productInfo.getName()));
    }
}
