package com.wyhua.flashsale.dao;

import com.wyhua.flashsale.entity.ProductInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductInfoDao extends CrudRepository<ProductInfo,Long> {

    /**
     * get a list of product information with limit and offset
     * @param offset
     * @param limit
     * @return
     */
    @Query(value = "SELECT * FROM product_info ORDER BY create_time DESC LIMIT ?1 ,?2 ;",nativeQuery = true)
    List<ProductInfo> queryAll(Integer offset, Integer limit);



    /**
     * reduce the amount of a product by one
     * @param productId
     * @param currentTime   time when the reduction happens
     * @return  number of affected rows
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE product_info " +
            "SET amount=amount-1 " +
            "WHERE product_id=?1 " +
            "AND start_time <= ?2 " +
            "AND end_time >= ?2 " +
            "AND amount > 0;",nativeQuery = true)
    Integer decreaseAmountByOne(Long productId, Date currentTime);



}
