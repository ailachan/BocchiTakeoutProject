package com.bocchi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bocchi.pojo.Dish;
import com.bocchi.pojo.DishDto;
import com.bocchi.pojo.Setmeal;

import java.util.List;

public interface DishService extends IService<Dish> {
    void insertDishAndFlavor(DishDto dish);

    DishDto selectDishAndFlavorById(Long id);

    void updateDishAndFlavor(DishDto dish);
}
