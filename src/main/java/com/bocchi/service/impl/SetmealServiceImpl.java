package com.bocchi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bocchi.mapper.SetmealMapper;
import com.bocchi.pojo.Setmeal;
import com.bocchi.pojo.SetmealDish;
import com.bocchi.pojo.SetmealDto;
import com.bocchi.service.SetmealDishService;
import com.bocchi.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐及套餐中包含的菜品
     * @param setmealDto
     */
    @Override
    @Transactional
    public void insertSetmealAndSelectedDishs(SetmealDto setmealDto) {
        //新增菜品，雪花算法生成setmealId
        this.save(setmealDto);

        //新增套餐包含的菜品，setmealDishes中不含setmealId,需设置
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes().stream().map(new Function<SetmealDish, SetmealDish>() {
            @Override
            public SetmealDish apply(SetmealDish setmealDish) {
                setmealDish.setSetmealId(setmealDto.getId());
                return setmealDish;
            }
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }
}
