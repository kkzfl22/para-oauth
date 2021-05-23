package com.paraview.oauth.exception;

import com.paraview.oauth.bean.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionInvoker {

    @ExceptionHandler
    public R exception(Exception e){
        e.printStackTrace();
        return new R("500",e.getMessage());
    }

}
