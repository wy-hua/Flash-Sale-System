package com.wyhua.flashsale.exception;

import com.wyhua.flashsale.entity.ProductInfo;

public class SaleNotOpenException extends  RuntimeException{

    public SaleNotOpenException(ProductInfo productInfo) {
        super("The sale for the product(Id: {%d}, Name: {%s}) that you want to purchase is not open now, please wait"
                .formatted(productInfo.getProductId(),productInfo.getName()));
    }
}
