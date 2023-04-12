package com.bocchi.controller;

import com.bocchi.common.Result;
import com.bocchi.pojo.Orders;
import com.bocchi.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("submit")
    public Result<String> submitOrders(@RequestBody Orders orders){
        log.info(orders.toString());

        orderService.submitOrder(orders);

        return Result.success("订单提交成功");
    }
}
