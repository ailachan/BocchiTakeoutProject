package com.bocchi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bocchi.mapper.DishMapper;
import com.bocchi.mapper.SetmealMapper;
import com.bocchi.pojo.Dish;
import com.bocchi.pojo.Setmeal;
import com.bocchi.service.DishService;
import com.bocchi.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
