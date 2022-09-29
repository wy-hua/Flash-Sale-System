package com.wyhua.flashsale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class OrderDto {
    private long orderId;

    private long productId;

    private String userPhone;

    // the state code of the order
    private Date creatTime;

    private int state;

    // the state information of the order
    private String stateInfo;



}
