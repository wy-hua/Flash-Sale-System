package com.wyhua.flashsale.handler;

import com.wyhua.flashsale.dto.Result;
import com.wyhua.flashsale.exception.*;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FlashSaleExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<String> outOfStockHandler(OutOfStockException e){
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public  ResponseEntity<String> productNotFoundHandler(ProductNotFoundException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SaleIsOverException.class)
    public ResponseEntity<String> saleIsOverHandler(SaleIsOverException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SaleNotOpenException.class)
    public ResponseEntity<String> saleNotOpenHandler(SaleNotOpenException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(WrongSaleUrlException.class)
    public ResponseEntity<String> wrongSaleUrlException(WrongSaleUrlException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
