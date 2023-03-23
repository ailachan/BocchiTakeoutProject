package com.bocchi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bocchi.mapper.DishFlavorMapper;
import com.bocchi.pojo.DishFlavor;
import com.bocchi.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
