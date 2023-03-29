package com.bocchi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bocchi.common.RemoveException;
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


    /**
     * 删除套餐及对应的菜品
     * @param ids
     */
    @Override
    @Transactional
    public void deleteSetMealAndSelecedDishs(List<Long> ids) {
        //根据套餐id及套餐状态字段,查询ids中是否有处于售卖状态的套餐
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Setmeal::getStatus,1);
        lqw.in(Setmeal::getId,ids);

        int count = this.count(lqw);

        //有则抛出异常
        if (count > 0){
            throw new RemoveException("套餐处于在售状态,不可删除");
        }

        //没有则删除套餐信息
        this.removeByIds(ids);

        //删除套餐对应的菜品信息,注意ids里存的是setmeal表的主键Id,是按setMealDish的外键id(setMeal的主键Id)删,而不是按SetmealDish这个关联表的主键id删
        LambdaQueryWrapper<SetmealDish> lqw2 = new LambdaQueryWrapper<>();
        lqw2.in(SetmealDish::getSetmealId,ids);

        setmealDishService.remove(lqw2);
    }
}
