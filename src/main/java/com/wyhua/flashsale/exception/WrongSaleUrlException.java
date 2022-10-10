package com.wyhua.flashsale.exception;

import com.wyhua.flashsale.entity.ProductInfo;

public class WrongSaleUrlException extends RuntimeException{
    public WrongSaleUrlException(ProductInfo productInfo){
        super("The Url for the product(Id: {%d}, Name: {%s}) you want to purchase is wrong"
                .formatted(productInfo.getProductId(),productInfo.getName()));
    }
    public WrongSaleUrlException(long id){
        super("The Url for the product(Id: %d) you want to purchase is wrong".formatted(id));
    }
}
