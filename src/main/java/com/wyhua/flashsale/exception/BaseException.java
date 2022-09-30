package com.wyhua.flashsale.exception;

import com.wyhua.flashsale.enums.ResultState;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private ResultState resultState;

    public BaseException(ResultState resultState){
        super(resultState.getMessage());
        this.resultState =resultState;
    }
}
