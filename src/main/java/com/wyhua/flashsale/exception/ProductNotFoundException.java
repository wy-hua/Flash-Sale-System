package com.wyhua.flashsale.exception;


public class ProductNotFoundException extends  RuntimeException{
    public  ProductNotFoundException(long id){
        super("The product(Id: {%d}) you want to purchase is not found in the database"
                .formatted(id));

    }
}
