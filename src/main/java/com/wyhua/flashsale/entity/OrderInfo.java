package com.wyhua.flashsale.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class OrderInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    /**
     * order ID
     */
    private  Long orderId;

    /**
     * sale commodity ID,秒杀商品ID
     */
    private Long productId;

    /**
     * user phone number
     */
    private String userPhone;

    /**
     * order state:-1:invalid  0:success, 成功 1:paid  2:in deliver,已发货
     */
    private Short  state;

    private Date createTime;

}
