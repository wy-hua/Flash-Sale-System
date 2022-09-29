package com.wyhua.flashsale.dao;

import com.wyhua.flashsale.entity.OrderInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderInfoDao extends CrudRepository<OrderInfo, Long> {
//    /**
//     * insert a valid order and avoid duplicates
//     * @param productId
//     * @param userPhone
//     * @return number of inserted rows  插入的行数
//     */
//    int insertValidOrder(@Param("productId") long productId, @Param("userPhone") long userPhone);
//
//
//    /**
//     * 根据秒杀商品的id查询明细SuccessKilled对象(该对象携带了Seckill秒杀产品对象)
//     * @param productId
//     * @return
//     */
//    OrderInfo queryByIdWithCommodityInfo(@Param("productId") long productId,@Param("userPhone") long userPhone);
    List<OrderInfo> findByProductIdAndUserPhone(Long productId, Long userPhone);

}
