package org.guoli.blog.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.guoli.blog.utlis.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)  //指定出现异常执行该方法
    @ResponseBody   //为了返回数据
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("遇到异常。。。");
    }
    //特定异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody //为了返回数据
    public R error(ArithmeticException e) {
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常处理..");
    }

    //自定义异常
    @ExceptionHandler(GuoliException.class)
    @ResponseBody //为了返回数据
    public R error(GuoliException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().status(e.getStatus()).message(e.getMsg());
    }
}
