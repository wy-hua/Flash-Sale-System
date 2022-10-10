package com.wyhua.flashsale.exception;

import com.wyhua.flashsale.entity.ProductInfo;

public class SaleIsOverException extends  RuntimeException{
    public SaleIsOverException(ProductInfo productInfo){
        super("The sale for the product(Id: {%d}, Name: {%s}) that you want to purchase is over, please come back next time"
                .formatted(productInfo.getProductId(),productInfo.getName()));
    }
}
