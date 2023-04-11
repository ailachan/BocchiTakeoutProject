package com.bocchi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bocchi.mapper.ShoppingCartMapper;
import com.bocchi.pojo.ShoppingCart;
import com.bocchi.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
