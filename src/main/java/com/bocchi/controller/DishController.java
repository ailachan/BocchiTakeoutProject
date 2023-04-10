package com.bocchi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bocchi.common.Result;
import com.bocchi.pojo.Category;
import com.bocchi.pojo.Dish;
import com.bocchi.pojo.DishDto;
import com.bocchi.pojo.DishFlavor;
import com.bocchi.service.CategoryService;
import com.bocchi.service.DishFlavorService;
import com.bocchi.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 添加菜品及对应口味信息
     *
     * @param dish
     * @return
     */
    @PostMapping
    public Result<String> insertDish(@RequestBody DishDto dish) {
        log.info(dish.toString());

        dishService.insertDishAndFlavor(dish);

        return Result.success("添加成功");
    }

    @GetMapping("/page")
    public Result<Page<DishDto>> pageQuery(int page, int pageSize, String name) {
        log.info("page={},pageSize={}", page, pageSize);

        Page<Dish> dishPageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        //支持模糊查询
        lqw.like(name != null, Dish::getName, name);
        lqw.orderByDesc(Dish::getUpdateTime);

        dishService.page(dishPageInfo, lqw);

        Page<DishDto> dishDtoPageInfo = new Page<>();

        //DishDto类是在Dish类上扩展属性没有mapper和对应的表无法进行分页查询
        //只拷贝分页信息
        BeanUtils.copyProperties(dishPageInfo, dishDtoPageInfo, "records");

        //获取原始dish的记录
        List<Dish> dishRecords = dishPageInfo.getRecords();

        //查到Name映射到扩展DishDto中
        List<DishDto> dishDtoRecords = dishRecords.stream().map(new Function<Dish, DishDto>() {
            @Override
            public DishDto apply(Dish dish) {
                DishDto dishDto = new DishDto();
                //获取每一个菜品的分类id
                Long categoryId = dish.getCategoryId();
                //根据分类id查询对应的分类
                Category category = categoryService.getById(categoryId);
                if (category != null) {
                    //获取name
                    String categoryName = category.getName();
                    //写入Name到扩展类DishDto
                    dishDto.setCategoryName(categoryName);
                }
                //拷贝原始类Dish信息到扩展类DishDto中
                BeanUtils.copyProperties(dish, dishDto);
                return dishDto;
            }
        }).collect(Collectors.toList());

        dishDtoPageInfo.setRecords(dishDtoRecords);

        return Result.success(dishDtoPageInfo);
    }

    /**
     * 根据id查询菜品及喜好信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Result<DishDto> selectDishAndFlavorById(@PathVariable Long id){
        DishDto dishDto = dishService.selectDishAndFlavorById(id);

        return Result.success(dishDto);
    }

    /**
     * 修改菜品及喜好信息
     * @param dish
     * @return
     */
    @PutMapping
    public Result<String> updateDish(@RequestBody DishDto dish) {
        log.info(dish.toString());

        dishService.updateDishAndFlavor(dish);

        return Result.success("更新成功");
    }

    /**
     * 查询菜品分类
     * @param dish
     * @return
     */
    @GetMapping("list")
    public Result<List<DishDto>> dishCategoryList(Dish dish){
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        //设置按分类id查询菜品集合
        lqw.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        lqw.eq(Dish::getStatus,1);
        lqw.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> dishList = dishService.list(lqw);

        //查到Name映射到扩展DishDto中
        List<DishDto> dishDtoList = dishList.stream().map(new Function<Dish, DishDto>() {
            @Override
            public DishDto apply(Dish dish) {
                DishDto dishDto = new DishDto();
                //获取每一个菜品的分类id
                Long categoryId = dish.getCategoryId();
                //根据分类id查询对应的分类
                Category category = categoryService.getById(categoryId);
                if (category != null) {
                    //获取name
                    String categoryName = category.getName();
                    //写入Name到扩展类DishDto
                    dishDto.setCategoryName(categoryName);
                }
                //拷贝原始类Dish信息到扩展类DishDto中

                LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
                lqw.eq(DishFlavor::getDishId,dish.getId());
                List<DishFlavor> flavors = dishFlavorService.list(lqw);

                dishDto.setFlavors(flavors);

                BeanUtils.copyProperties(dish, dishDto);
                return dishDto;
            }
        }).collect(Collectors.toList());

        return Result.success(dishDtoList);
    }
}
