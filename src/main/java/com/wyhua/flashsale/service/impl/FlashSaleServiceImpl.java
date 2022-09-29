package com.wyhua.flashsale.service.impl;

import com.wyhua.flashsale.dao.ProductInfoDao;
import com.wyhua.flashsale.dao.OrderInfoDao;
import com.wyhua.flashsale.dao.cache.RedisDao;
import com.wyhua.flashsale.dto.OrderDto;
import com.wyhua.flashsale.entity.OrderInfo;
import com.wyhua.flashsale.entity.ProductInfo;
import com.wyhua.flashsale.enums.OrderState;
import com.wyhua.flashsale.enums.ResultState;
import com.wyhua.flashsale.exception.BaseException;
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
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * check whether the sale is open and return
     * @param id
     * @return  the product information, can't be null
     * @throws BaseException
     */
    private ProductInfo checkIfSaleOpenAndGetProductInfo(long id) throws BaseException{
        ProductInfo productInfo= getProductInfoFromCache(id);
        if(productInfo==null)
            throw new BaseException(ResultState.PRODUCT_NOT_EXIST);
        Date now=new Date();
        if(now.compareTo(productInfo.getStartTime())<0)
            throw new BaseException(ResultState.SALE_NOT_OPEN);
        if(now.compareTo(productInfo.getEndTime())>0)
            throw new BaseException(ResultState.SALE_OVER);
        return productInfo;
    }


    @Override
    public String getSaleUrl(long id) throws BaseException {

//        check whether the product is in sale
        if(checkIfSaleOpenAndGetProductInfo(id)!=null)
            return getMD5(id);
        else
            return null;
    }

    @Override
    public ProductInfo getProductInfoFromCache(long id) {
        ProductInfo res=redisDao.getProductInfo(id);
        if(res==null){
            res= productInfoDao.findById(id).orElse(null);
            if(res==null)
                return null;
            redisDao.saveProductInfo(res);
            redisDao.saveProductAmount(res.getProductId(), res.getAmount());

        }
        return res;
    }


    @Override
    public Integer getProductAmountFromCache(long id)  {
        Integer result=redisDao.getProductAmount(id);
//       if the amount is not cached,get it from the database and cache it
        if(result==null){
            ProductInfo productInfo= getProductInfoFromCache(id);
            if(productInfo!=null){
                result=productInfo.getAmount();
                redisDao.saveProductAmount(id,result);
            }
        }
        return  result;
    }

    @Transactional
    @Override
    public OrderDto makePurchase(long productId, String userPhone, String md5) throws BaseException {
//        check whether the md5 is correct
        if(!getMD5(productId).equals(md5))
            throw new BaseException(ResultState.SALE_URL_ERROR);

        ProductInfo productInfo=checkIfSaleOpenAndGetProductInfo(productId);

//        save the order
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setProductId(productId);
        orderInfo.setUserPhone(userPhone);
        orderInfo.setState((short)1);
        orderInfo.setCreateTime(new Date());
        orderInfo= orderInfoDao.save(orderInfo);

//       decrement the existing amount of the product by one
        int amount= getProductAmountFromCache(productId);
        if(amount==0)
            throw new BaseException(ResultState.OUT_OF_STOCK);
        else{
            redisDao.decrementProduct(productId,false);
        }

        return new OrderDto(orderInfo.getOrderId(), productId,userPhone,orderInfo.getCreateTime(),
                OrderState.SUCCESS.getState(),OrderState.SUCCESS.getInfo());
    }
}
