package com.wyhua.flashsale.dao;

import com.wyhua.flashsale.entity.ProductInfo;
import com.wyhua.flashsale.entity.OrderInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductInfoDaoTest {
    @Autowired
    private ProductInfoDao productInfoDao;

    @Autowired
    private OrderInfoDao orderInfoDao;


    @Test
    public void saveOrder(){
        List<OrderInfo> pre = orderInfoDao.findByProductIdAndUserPhone(1003L,15012345679L);
        orderInfoDao.save(new OrderInfo(null,1003L,"15012345679", (short) 0, new Date()));
        List<OrderInfo> tmp = orderInfoDao.findByProductIdAndUserPhone(1003L,15012345679L);
        assertEquals(tmp.size(),pre.size()+1);
    }

    @Test
    public  void queryAll(){
        List<ProductInfo> productInfos = productInfoDao.queryAll(0,3);
        assertEquals(productInfos.size(),3);
    }

    @Test
    void purchaseById() {
        ProductInfo tmp= productInfoDao.findById(1003L).get();

        Timestamp currentTime=null;
        try{
            currentTime=StringToTimestamp("2022-09-04 00:00:00");

        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        Integer affectedRow= productInfoDao.decreaseAmountByOne(1003L,currentTime);
        assertEquals(affectedRow,1);
        assertEquals(tmp.getAmount()-1, productInfoDao.findById(1003L).get().getAmount());
    }

    /**
     *  convert String("yyyy-MM-dd HH:mm:ss") to  java.sql.Timestamp
     * @param strDate
     * @return
     * @throws ParseException
     */
    private static java.sql.Timestamp StringToTimestamp(String strDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        java.util.Date parsedTimeStamp = dateFormat.parse(strDate);

        java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedTimeStamp.getTime());
        return timestamp;

    }

}