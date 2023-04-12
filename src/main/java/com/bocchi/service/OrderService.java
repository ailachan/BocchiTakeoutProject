package com.bocchi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bocchi.pojo.Orders;

public interface OrderService extends IService<Orders> {

    /**
     * 提交订单
     * @param orders
     */
    void submitOrder(Orders orders);
}
