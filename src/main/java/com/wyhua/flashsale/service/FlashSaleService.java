package com.wyhua.flashsale.service;

import com.wyhua.flashsale.dto.OrderDto;
import com.wyhua.flashsale.entity.ProductInfo;
import com.wyhua.flashsale.exception.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FlashSaleService {

    public List<ProductInfo> findAllInfo();


    /**
     *  get a product's purchase URL by its ID
     * @param productId
     * @return   the MD5 encryption part of the purchase Url
     * @throws ProductNotFoundException
     * @throws SaleIsOverException
     * @throws SaleNotOpenException
     */
    public String getSaleUrl(long productId) throws ProductNotFoundException, SaleIsOverException, SaleNotOpenException;

    /**
     * check whether the sale url is valid
     * @param md5 the MD5 encryption part of the purchase Url
     * @return
     */
    public boolean isSaleUrlValid(long productId,String md5);

    /**
     * try to get product information from cache,if failed then get it from database and save it to cache
     * @param productId  product ID
     * @return return null if the product doesn't exist either in cache or database
     */
    public ProductInfo getProductInfo(long productId);

    /**
     * try to get a product's information by its ID from cache, if failed then get it from database and save it to cache
     * @param productId
     * @return return null if the product's amount doesn't exist either in cache or database
     */
    public Long getProductAmount(long productId) ;


    /**
     *  decrement the product's amount stored in cache in advance (by one)
     * @param productId  product ID
     * @return  the product's amount after being decremented. If it doesn't exist in cache, return null
     * @throws WrongSaleUrlException
     * @throws OutOfStockException
     * @throws ProductNotFoundException
     * @throws SaleIsOverException
     * @throws SaleNotOpenException
     */
    public Long decrementProductAmountInCache(long productId)
            throws OutOfStockException,ProductNotFoundException,SaleIsOverException,SaleNotOpenException;


    /**
     *  After decrementing the product's amount stored in cache, modify the data in database
     *  Insert a new order into the database and decrease the amount of product
     * @param productId
     * @param userPhone
     * @param purchaseTime
     * @return
     * @throws WrongSaleUrlException
     */
    public Long makePurchaseInDataBase(long productId, String userPhone, Date purchaseTime);

}
