package com.ant.servicebase.exceptionhandler;



import com.ant.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 *
 */

/* @ControllerAdvice学名为Controller增强器，作用是给Controller控制器添加统一的操作和处理
    @ControllerAdvice是在类上声明的注解，其用法主要有三点：
        1.结合方法型注解@ExceptionHandler,用于捕获Controller中抛出的指定类型的异常，从而达到不同类型的异常区别处理的目的
        2.结合方法型注解@InitBinder，用于request中自定义的参数解析方式进行注册，从而达到自定义指定格式参数的目的
        3.结合方法型注解@ModelAttribute，表示其注解的方法将在目标Controller方法执行之前执行

 * */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     * @param e
     * @return
     */
    // 指定出现什么异常执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody  // 为了返回数据
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理....");
    }

    /**
     * 特定异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("指定了ArithmeticException异常处理");
    }


    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e){
        log.error(e.getMessage());   // 将错误日志输出到错误日志文件中（因为是调用的error方法,调用info方法就会放到info日志文件中）
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
