package com.wyhua.flashsale.service;

import com.wyhua.flashsale.entity.OrderInfo;

import java.util.List;

public interface OrderService {
    /**
     * get all the orders of certain user
     * @param userPhone
     * @return
     */
    public List<OrderInfo> getAllOrders(String userPhone);

}
