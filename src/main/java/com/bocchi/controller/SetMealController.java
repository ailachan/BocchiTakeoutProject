package com.bocchi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bocchi.common.Result;
import com.bocchi.pojo.Category;
import com.bocchi.pojo.Setmeal;
import com.bocchi.pojo.SetmealDto;
import com.bocchi.service.CategoryService;
import com.bocchi.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐及包含的菜品信息
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result<String> insertSetmealAndSelectedDishs(@RequestBody SetmealDto setmealDto){
        log.info(String.valueOf(setmealDto));

        setmealService.insertSetmealAndSelectedDishs(setmealDto);

        return Result.success("添加成功");
    }

    /**
     * 分页查询套餐
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public Result<Page<SetmealDto>> pageQuery(int page,int pageSize,String name){
        Page<Setmeal> setmealPageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPageInfo = new Page<>();

        Page<Setmeal> setmealPageResult = setmealService.page(setmealPageInfo);

        //拷贝除records以外的信息
        BeanUtils.copyProperties(setmealPageInfo,setmealDtoPageInfo,"records");

        //根据原records信息中的分类id查询分类name,重新构造Dto对象的records集合
        List<SetmealDto> setmealDtoRecords = setmealPageResult.getRecords().stream().map(new Function<Setmeal, SetmealDto>() {
            @Override
            public SetmealDto apply(Setmeal setmeal) {
                SetmealDto setmealDto = new SetmealDto();

                Long categoryId = setmeal.getCategoryId();

                Category category = categoryService.getById(categoryId);

                //查到的Name添加到dto
                if (category != null) {
                    setmealDto.setCategoryName(category.getName());
                }

                //拷贝原records信息到dto
                BeanUtils.copyProperties(setmeal, setmealDto);

                return setmealDto;
            }
        }).collect(Collectors.toList());

        setmealDtoPageInfo.setRecords(setmealDtoRecords);

        return Result.success(setmealDtoPageInfo);
    }


    /**
     * 删除套餐及关联表的菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    //多个逗号分割的参数会自动转为一个List集合接收,@RequestParam表示作为作为集合中的参数add而不是List这个对象中的属性来接收
    public Result<String> deleteSetMealAndSelectedDishs(@RequestParam List<Long> ids){
        log.info(ids.toString());

        setmealService.deleteSetMealAndSelecedDishs(ids);

        return Result.success("删除成功");
    }


    /**
     * 查询套餐
     * @param setmeal
     * @return
     */
    @GetMapping("list")
    public Result<List<Setmeal>> setMealList(Setmeal setmeal){
        Long categoryId = setmeal.getCategoryId();
        Integer status = setmeal.getStatus();

        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(categoryId != null,Setmeal::getCategoryId,categoryId);
        lqw.eq(status != null,Setmeal::getStatus,status);
        List<Setmeal> setmealList = setmealService.list(lqw);

        return Result.success(setmealList);
    }
}
