package com.wyhua.flashsale.service.impl;

import com.wyhua.flashsale.dao.ProductInfoDao;
import com.wyhua.flashsale.dao.OrderInfoDao;
import com.wyhua.flashsale.dao.cache.RedisDao;
import com.wyhua.flashsale.entity.OrderInfo;
import com.wyhua.flashsale.entity.ProductInfo;
import com.wyhua.flashsale.enums.ResultState;
import com.wyhua.flashsale.exception.*;
import com.wyhua.flashsale.service.FlashSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class FlashSaleServiceImpl implements FlashSaleService {

    final RedisDao redisDao;
    final ProductInfoDao productInfoDao;
    final OrderInfoDao orderInfoDao;
    final String salt = "shsdssljdd'l.23*";

    @Autowired
    public FlashSaleServiceImpl(RedisDao redisDao, ProductInfoDao productInfoDao, OrderInfoDao orderInfoDao) {
        this.redisDao = redisDao;
        this.productInfoDao = productInfoDao;
        this.orderInfoDao = orderInfoDao;
    }


    @Override
    public List<ProductInfo> findAllInfo() {
        List<ProductInfo> list=new ArrayList<>();
        Iterator<ProductInfo> iterator= productInfoDao.findAll().iterator();
        iterator.forEachRemaining(list::add);
        return list;
    }

    public String getMD5(long productId) {
        String base = productId + "/" + salt;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    /**
     * check whether the sale is open and return
     * @param id
     * @return  the product information, can't be null
     * @throws
     */
    private ProductInfo checkIfSaleOpenAndGetProductInfo(long id) throws ProductNotFoundException,SaleIsOverException,SaleNotOpenException{
        ProductInfo productInfo= getProductInfo(id);
        if(productInfo==null)
            throw new ProductNotFoundException(id);
        Date now=new Date();
        if(now.compareTo(productInfo.getStartTime())<0)
            throw new SaleNotOpenException(productInfo);
        if(now.compareTo(productInfo.getEndTime())>0)
            throw new SaleIsOverException(productInfo);
        return productInfo;
    }


    @Override
    public String getSaleUrl(long productId) throws ProductNotFoundException,SaleIsOverException,SaleNotOpenException {

//        check whether the product is in sale
        if(checkIfSaleOpenAndGetProductInfo(productId)!=null)
            return getMD5(productId);
        else
            return null;
    }

    @Override
    public boolean isSaleUrlValid(long productId, String md5) {
        return md5.equals(getMD5(productId));
    }

    @Override
    public ProductInfo getProductInfo(long productId) {
        ProductInfo res=redisDao.getProductInfo(productId);
        if(res==null){
            res= productInfoDao.findById(productId).orElse(null);
            if(res==null)
                return null;
            redisDao.saveProductInfo(res);
            redisDao.saveProductAmount(res.getProductId(), res.getAmount());
        }
        return res;
    }


    @Override
    public Long getProductAmount(long productId)  {
        Long result=redisDao.getProductAmount(productId);
//       if the amount is not cached,get it from the database and cache it
        if(result==null){
            ProductInfo productInfo= getProductInfo(productId);
            if(productInfo!=null){
                result=productInfo.getAmount();
                redisDao.saveProductAmount(productId,result);
            }
        }
        return  result;
    }


    @Override
    public Long decrementProductAmountInCache(long productId)
            throws OutOfStockException,ProductNotFoundException,SaleIsOverException,SaleNotOpenException {
        //      decrement the product's amount stored in cache in advance (by one)
        ProductInfo productInfo= checkIfSaleOpenAndGetProductInfo(productId);
        long amount= getProductAmount(productId);
        if(amount==0)
            throw new OutOfStockException(productInfo);
        else{
            return redisDao.decrementProduct(productId,false);
        }
    }

    @Transactional
    @Override
    public Long makePurchaseInDataBase(long productId, String userPhone, Date purchaseTime) throws WrongSaleUrlException {
//      save the order
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setProductId(productId);
        orderInfo.setUserPhone(userPhone);
        orderInfo.setState((short)1);
        orderInfo.setCreateTime(purchaseTime);
        orderInfoDao.save(orderInfo);
//       decrement the product's amount stored in database
        long affectedRows= productInfoDao.decreaseAmountByOne(productId,purchaseTime);

        return affectedRows;

    }
}
