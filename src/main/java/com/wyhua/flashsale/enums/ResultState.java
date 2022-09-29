package com.wyhua.flashsale.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultState {
    SUCCESS(0, "The operation succeeded"),
    FAILED(-1, "The operation failed for unknowon reasons"),
    EXCEPTION(-1, "System error"),

    /**
     *  commodity and order module
     */
    SALE_NOT_OPEN(10001, "The sale is not open yet"),
    SALE_OVER(10002,"The sale is over"),
    OUT_OF_STOCK(10003,"The product you try to purchase is out of stock"),
    ORDER_OUT_OF_LIMIT(10004, "You have purchase more product than you are allowed to purchase"),
    SALE_URL_ERROR(10005,"The url for purchasing the product is wrong"),
    PRODUCT_NOT_EXIST(10006,"The product you request doesn't exist");
    private int code;
    private String message;
}
