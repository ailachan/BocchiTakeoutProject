package com.bocchi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bocchi.pojo.Dish;
import com.bocchi.pojo.Setmeal;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
