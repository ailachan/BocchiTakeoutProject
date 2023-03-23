package com.bocchi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bocchi.mapper.DishMapper;
import com.bocchi.mapper.SetmealMapper;
import com.bocchi.pojo.Dish;
import com.bocchi.pojo.DishDto;
import com.bocchi.pojo.DishFlavor;
import com.bocchi.pojo.Setmeal;
import com.bocchi.service.DishFlavorService;
import com.bocchi.service.DishService;
import com.bocchi.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;


    /**
     * 添加菜品及对应口味信息
     * @param dish
     */
    @Transactional//涉及多表操作需开启事务,俩个表的service的save方法要么都成功要么都回退(比如第一个save方法执行成功,下面另一个save方法发生异常,开启事务后就会一起回退)
    public void insertDishAndFlavor(DishDto dish){
        //用当前类中的方法添加菜品信息,表中不包含的flavors会默认忽略
        this.save(dish);

        //获取添加后雪花算法生成的菜品id
        Long id = dish.getId();

        //获取当前菜品id下所有的口味信息
        List<DishFlavor> flavors = dish.getFlavors();

        //为每一口味对象添加菜品id
        flavors = flavors.stream().map(new Function<DishFlavor, DishFlavor>() {

            @Override
            public DishFlavor apply(DishFlavor dishFlavor) {
                dishFlavor.setDishId(id);
                return dishFlavor;
            }
        }).collect(Collectors.toList());

        //批量添加
        dishFlavorService.saveBatch(flavors);

    }
}
