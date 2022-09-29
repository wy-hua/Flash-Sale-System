package com.wyhua.flashsale.dto;


import com.wyhua.flashsale.enums.ResultState;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Result <T>{
    private boolean isSuccess;

    private int code;

    private String message;

    private T data;

    public Result(ResultState resultState,T data){
        this.code=resultState.getCode();
        this.message=resultState.getMessage();
        this.data=data;
        if(resultState!=ResultState.SUCCESS){
            this.isSuccess=false;
        }
        else{
            this.isSuccess=true;
        }
    }

}
