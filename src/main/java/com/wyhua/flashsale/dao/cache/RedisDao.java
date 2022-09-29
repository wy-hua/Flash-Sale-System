package com.wyhua.flashsale.dao.cache;

import com.wyhua.flashsale.entity.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

import static com.wyhua.flashsale.constant.RedisPrefixConst.*;
import static java.util.Collections.singletonList;

@Component
public class RedisDao {
    private final RedisTemplate<String, Object> template;


    /**
     * unit:  milliseconds
     */
    private final int LOCK_EXPIRE_TIME=10;

    /**
     *      sleep for a while if we don't get the lock
     */
    private final int WAIT_AFTER_FAIL=5;

    /**
     * remove the key only if it exists and the value stored at the key is exactly the one I expect to be
     */
    private final RedisScript<Long> releaseLockScript;

    @Autowired
    public RedisDao(RedisTemplate<String, Object> template) {
        this.releaseLockScript = RedisScript.of("if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end",Long.class);
        this.template = template;
    }


    /**
     *  try to get distributed lock
     * @param lockkey
     * @param lockRequestId
     * @param expireTime   unit: milliseconds
     * @return
     */
    private boolean tryGetDistributedLock(String lockkey,String lockRequestId,int expireTime){
        return template.opsForValue().setIfAbsent(lockkey,lockRequestId, Duration.ofMillis(expireTime));
    }

    /**
     * release the distributed lock of a product
     * @param lockKey
     * @param requestId
     * @return   1: success  0: failed
     */
    private   Long releaseDistributedLock(String lockKey, String requestId){
       return template.execute(releaseLockScript, singletonList(lockKey),requestId);
    }

    public String productAmountKey(Long id){
        return String.format(COMMODITY_AMOUNT,id);
    }

    public String productInfoKey(Long id){
        return String.format(COMMODITY_INFO,id);
    }

    public String productLockKey(Long id){
        return String.format(COMMODITY_LOCK_KEY,id);
    }

    public  void saveProductAmount(Long id,int amount){
        String key=productAmountKey(id);
        template.opsForValue().set(key,amount);
    }

    public Integer  getProductAmount(Long id){
        String key=productAmountKey(id);
//        if the key doesn't exist
        return (Integer) template.opsForValue().get(key);
    }

    public void saveProductInfo(ProductInfo productInfo){
        String key=productInfoKey(productInfo.getProductId());
        template.opsForValue().set(key, productInfo);
    }


    public ProductInfo getProductInfo(Long id){
        String key=productInfoKey(id);
        return (ProductInfo) template.opsForValue().get(key);


    }



    /**
     *  try to get the lock and decrement the amount of a product by one
     * @param id   product ID
     * @param isDistributedLock    whether there are distributed locks to every product amount
     * @return
     */
    public Long decrementProduct(Long id,boolean isDistributedLock){
        String key=productAmountKey(id);
        Long res=-1L;
        if(!isDistributedLock){
            res=template.opsForValue().decrement(key);
            return res;
        }
        String lockKey=productLockKey(id);
        String lockRequestId = UUID.randomUUID().toString();
        try {
            while (true) {
                boolean isGetLock = tryGetDistributedLock(lockKey, lockRequestId, LOCK_EXPIRE_TIME);
                System.out.println("try to get lock at:"+ new Date());

                if (isGetLock) {
                    System.out.println("get lock at:"+ new Date());
                    res=template.opsForValue().decrement(key);
                    break;
                }
                // sleep for a while if we don't get the lock
                try {
                    Thread.sleep(WAIT_AFTER_FAIL);
                } catch (InterruptedException ignored) {
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            Long tmp= releaseDistributedLock(lockKey,lockRequestId);
            if( tmp!=0)
                System.out.println("release lock at:"+ new Date());


        }
        return res;
    }
}
