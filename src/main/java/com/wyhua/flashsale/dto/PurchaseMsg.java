package com.wyhua.flashsale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PurchaseMsg {
    private  long productId;
    private String userPhone;
    private Date purchaseTime;
}
