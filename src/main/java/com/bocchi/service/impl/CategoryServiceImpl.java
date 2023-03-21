package com.bocchi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bocchi.common.RemoveException;
import com.bocchi.mapper.CategoryMapper;
import com.bocchi.pojo.Category;
import com.bocchi.pojo.Dish;
import com.bocchi.pojo.Setmeal;
import com.bocchi.service.CategoryService;
import com.bocchi.service.DishService;
import com.bocchi.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 删除不存在关联的分类
     * @param id
     * @return
     */
    @Override
    public boolean removeCategory(Long id){
        LambdaQueryWrapper<Dish> dishLqw = new LambdaQueryWrapper<>();

        dishLqw.eq(Dish::getCategoryId,id);

        //当前分类id下dish表中存在关联数据
        if (dishService.count(dishLqw)>0){
            throw new RemoveException("删除异常,dish表中存在关联数据");
        }

        LambdaQueryWrapper<Setmeal> mealLqw = new LambdaQueryWrapper<>();

        mealLqw.eq(Setmeal::getCategoryId,id);

        if (setmealService.count(mealLqw)>0){
            throw new RemoveException("删除异常,setMeal表中存在关联数据");
        }

        return super.removeById(id);
    }
}
