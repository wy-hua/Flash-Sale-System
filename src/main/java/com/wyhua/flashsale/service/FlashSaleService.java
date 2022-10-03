package com.wyhua.flashsale.service;

import com.wyhua.flashsale.dto.OrderDto;
import com.wyhua.flashsale.entity.ProductInfo;
import com.wyhua.flashsale.exception.BaseException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FlashSaleService {

    List<ProductInfo> findAllInfo();

    /**
     * get a product's purchase URL by its ID
     * @param productId  product ID
     * @return  MD5 string for the purchase URL
     */
    String getSaleUrl(long productId) throws BaseException;

    /**
     * check whether the sale url is valid
     * @param md5
     * @return
     */
    boolean isSaleUrlValid(long productId,String md5);

    /**
     * try to get product information from cache,if failed then get it from database and save it to cache
     * @param productId  product ID
     * @return return null if the product doesn't exist either in cache or database
     */
    ProductInfo getProductInfo(long productId);

    /**
     * try to get a product's information by its ID from cache, if failed then get it from database and save it to cache
     * @param productId
     * @return return null if the product's amount doesn't exist either in cache or database
     */
    Long getProductAmount(long productId) ;

    /**
     *  decrement the product's amount stored in cache in advance (by one)
     * @param productId product ID
     * @return  return the product's amount after being decremented. If it doesn't exist in cache, return null
     * @throws BaseException  throw BaseException if the sale is close or the product doesn't exist or the product's amount is zero
     */
    Long decrementProductAmountInCache(long productId, String md5) throws BaseException;

    /**
     *  After decrementing the product's amount stored in cache, modify the data in database
     *  Insert a new order into the database and decrease the amount of the product
     * @param productId  product ID
     * @param userPhone  user phone number
     * @param md5  sale url
     * @return    return the product's amount stored in database after the purchase. If it failed, return null
     */
    Long makePurchase(long productId, String userPhone, String md5, Date purchaseTime) throws BaseException;

}
