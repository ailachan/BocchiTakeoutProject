package com.bocchi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bocchi.mapper.OrderDetailMapper;
import com.bocchi.mapper.OrderMapper;
import com.bocchi.pojo.OrderDetail;
import com.bocchi.pojo.Orders;
import com.bocchi.service.OrderDetailService;
import com.bocchi.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
