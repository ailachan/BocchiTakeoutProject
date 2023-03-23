package com.bocchi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@Slf4j//用于记录日志,加了之后可以用log记录日志
@EnableTransactionManagement//开启事务功能
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        log.info("boot 程序已启动");
    }
}
