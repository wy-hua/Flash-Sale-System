package com.wyhua.flashsale.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 *  commodity information
 *  秒杀商品的具体信息
 */
@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor

public class ProductInfo {
    /**
     * sale commodity ID,商品库存ID
     */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long productId;

    /**
     * commodity name,商品名称
     */
    private  String name;

    /**
     * stock quantity,库存数量
     */
    private Integer amount;

    /**
     * when the flash sale will start,秒杀开始时间
     */
    private Date startTime;

    /**
     * when the flash sale will end,秒杀结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    private Date createTime;


}
