package com.wyhua.flashsale.constant;

import java.util.UUID;

public class RedisPrefixConst {
    /**
     *     get detail information based on commodity ID
     */
    public final static String COMMODITY_INFO="commodityInfo:%d";
    public final static String COMMODITY_LOCK_KEY = "commodityLock:%d";
    public final static String COMMODITY_AMOUNT="commodityAmount:%d";
}
