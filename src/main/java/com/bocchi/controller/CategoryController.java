package com.bocchi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bocchi.common.Result;
import com.bocchi.pojo.Category;
import com.bocchi.pojo.DishFlavor;
import com.bocchi.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> insertCategory(@RequestBody Category category){
        log.info("category:{}",category);

        categoryService.save(category);

        return Result.success("添加成功");
    }

    /**
     * 分类信息分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public Result<Page<Category>> queryByPage(int page,int pageSize){
        //配置分页信息
        Page<Category> pageInfo = new Page<>(page,pageSize);

        //设置排序字段
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Category::getSort);

        //返回分页结果
        Page<Category> pageResult = categoryService.page(pageInfo, lqw);

        return Result.success(pageResult);
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteById(Long ids){
        categoryService.removeCategory(ids);

        return Result.success("删除成功");
    }


    /**
     * 更新分类
     * @param category
     * @return
     */
    @PutMapping
    public Result<String> updateById(@RequestBody Category category){

        categoryService.updateById(category);

        return Result.success("更新成功");
    }


    /**
     * 按type获取分类下拉列表
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> CategoryListQuery(Category category){
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(category.getType()!=null,Category::getType,category.getType());//按类型查询是套餐还是菜品
        lqw.orderByDesc(Category::getSort,Category::getUpdateTime);

        List<Category> list = categoryService.list(lqw);

        return Result.success(list);
    }

}
