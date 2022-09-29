package com.wyhua.flashsale.dto;

import lombok.Data;

@Data
public class SaleUrlDto {
    private boolean isSaleOpen;

    private String md5;

    private long productId;

    // system current time:milliseconds
    private long now;

    // sale start time:milliseconds
    private long start;

    // sale end time: milliseconds
    private long end;
}
