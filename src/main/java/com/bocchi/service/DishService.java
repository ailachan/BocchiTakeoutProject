package com.bocchi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bocchi.pojo.Dish;
import com.bocchi.pojo.DishDto;
import com.bocchi.pojo.Setmeal;

public interface DishService extends IService<Dish> {
    void insertDishAndFlavor(DishDto dish);
}
