package com.wyhua.flashsale.enums;

import lombok.Data;

public enum OrderState {
    PAID(2,"paid"),
    IN_DELIEVERY(3,"In delivery"),
    SUCCESS(1,"Success"),
    END(0,"Fail"),
    REPEAT_ORDER(-1,"The order is repeated"),
    INNER_ERROR(-2,"System error"),
    DATE_REWRITE(-3,"Data tampering");

    private int state;
    private String info;
    OrderState(int state, String info) {
        this.state = state;
        this.info = info;
    }

    public int getState() {
        return state;
    }


    public String getInfo() {
        return info;
    }

    public static OrderState stateof(int index){
        for (var i : values()){
            if(i.getState()==index)
                return i;
        }
        return null;
    }
}
