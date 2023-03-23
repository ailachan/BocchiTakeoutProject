package com.bocchi.controller;

import com.bocchi.common.Result;
import com.bocchi.pojo.DishDto;
import com.bocchi.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 添加菜品及对应口味信息
     * @param dish
     * @return
     */
    @PostMapping
    public Result<String> insertDish(@RequestBody DishDto dish){
        log.info(dish.toString());

        dishService.insertDishAndFlavor(dish);

        return Result.success("添加成功");
    }
}
