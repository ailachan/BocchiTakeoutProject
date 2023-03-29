package com.bocchi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bocchi.pojo.Setmeal;
import com.bocchi.pojo.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void insertSetmealAndSelectedDishs(SetmealDto setmealDto);

    public void deleteSetMealAndSelecedDishs(List<Long> ids);
}
