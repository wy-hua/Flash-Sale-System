package com.wyhua.flashsale.dao.cache;

import com.wyhua.flashsale.entity.ProductInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RedisDaoTest {
    @Autowired
    private RedisDao redisDao;

    @Autowired
    private  RedisTemplate<String, Object> template;
    @Test
    void saveAndGetProductInfo() {
        ProductInfo productInfo =new ProductInfo(1003L,"iphone14",1000,new Date(),new Date(),null);
        redisDao.saveProductInfo(productInfo);
        assertEquals(redisDao.getProductInfo(1003L).getAmount(), productInfo.getAmount());

    }

    @Test
    void getProductAmount(){
       System.out.println( redisDao.getProductAmount(1002l));
    }

    @Test
    void decrementProduct() {
        class DecreaseThread implements  Runnable{
            public  void run(){
                redisDao.decrementProduct(1003L,false);
            }
        }
        int amount=10000;
        redisDao.saveProductAmount(1003L,amount);
        List<Integer> testList= List.of(1,10,100,100);
        for (int i = 0; i < testList.size(); i++) {
            amount-=testList.get(i);
        }
        testList.forEach(v ->{
            long startTime= System.nanoTime();
            for(int i=0;i<v;i++){
                new DecreaseThread().run();
            }
            long endTime= System.nanoTime();
            System.out.println(String.format("decrement for %d times, the elapsed time is %d milliseconds",v,(endTime-startTime)/1000000L));
        });

        assertEquals(redisDao.getProductAmount(1003L),amount);
        template.delete(redisDao.productAmountKey(1003L));

    }

    @Test
    void saveProductAmount() {
        redisDao.saveProductAmount(1003L,1000);
        assertEquals(redisDao.getProductAmount(1003L),1000);
        template.delete(redisDao.productAmountKey(1003L));


    }


}