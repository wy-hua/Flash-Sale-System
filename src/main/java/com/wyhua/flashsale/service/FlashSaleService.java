package com.wyhua.flashsale.service;

import com.wyhua.flashsale.dto.OrderDto;
import com.wyhua.flashsale.entity.ProductInfo;
import com.wyhua.flashsale.exception.BaseException;

import java.util.List;
import java.util.Optional;

public interface FlashSaleService {

    List<ProductInfo> findAllInfo();

    /**
     * get a product's purchase URL by its ID
     * @param id
     * @return
     */
    String getSaleUrl(long id) throws BaseException;


    /**
     * get product information from redis,if not exit then get it from database and save it to redis
     * @param id
     * @return
     */
    ProductInfo getProductInfoFromCache(long id);

    /**
     * get a product's information by its ID
     * @param id
     * @return
     */
    Integer getProductAmountFromCache(long id) ;

    /**
     *
     * @param productId
     * @param userPhone
     * @param md5
     * @return
     */
    OrderDto makePurchase(long productId,String userPhone,String md5) throws BaseException;


}
