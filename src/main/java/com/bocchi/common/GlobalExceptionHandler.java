package com.bocchi.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Controller全局异常异常通知类
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody//由aop代理返回数据,所以要和controller类一样加此注解返回json数据
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获发生此异常的方法,并执行此通知Advice,以后遇到此异常前端起码不会去执行catch中的代码
     *
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)//捕获sql违反约束错误
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException e) {
        log.info("异常Advice捕获"+e.getMessage());

        if (e.getMessage().contains("Duplicate entry")) {
            String duplicateUsername = e.getMessage().split(" ")[2];
            //substring包头不包尾
            return Result.error(duplicateUsername.substring(1, duplicateUsername.length() - 1) + "已存在");
        }

        return Result.error("其他未知违反约束错误");
    }

    /**
     * 捕获发生此异常的方法,并执行此通知Advice,以后遇到此异常前端起码不会去执行catch中的代码
     *
     * @return
     */
    @ExceptionHandler(RemoveException.class)//捕获自定义删除异常错误
    public Result<String> RemoveExceptionHandler(RemoveException e) {
        log.info("异常Advice捕获"+e.getMessage());

        return Result.error(e.getMessage());
    }

    @ExceptionHandler(OtherException.class)//捕获自定义其他异常错误
    public Result<String> OtherExceptionHandler(OtherException e) {
        log.info("异常Advice捕获"+e.getMessage());

        return Result.error(e.getMessage());
    }
}
