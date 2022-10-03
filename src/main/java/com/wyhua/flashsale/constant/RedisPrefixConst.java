package com.wyhua.flashsale.constant;

import java.util.UUID;

public class RedisPrefixConst {
    /**
     *     get detail information based on commodity ID
     */
    public final static String COMMODITY_INFO="productInfo:%d";
    public final static String COMMODITY_LOCK_KEY = "productLock:%d";
    public final static String COMMODITY_AMOUNT="productAmount:%d";
}
