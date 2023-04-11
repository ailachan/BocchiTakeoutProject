package com.bocchi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bocchi.common.BaseContext;
import com.bocchi.common.Result;
import com.bocchi.pojo.ShoppingCart;
import com.bocchi.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("add")
    public Result<ShoppingCart> addToShoppingCart(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());

        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,userId);


        //当前添加到购物车中的是菜品还是套餐
        if (shoppingCart.getDishId() != null){
            lqw.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }
        else {
            lqw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        //查询当前用户下的当前菜品或套餐是否存在
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(lqw);

        if (shoppingCart1 == null){
            shoppingCart.setNumber(1);
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);

            return Result.success(shoppingCart);
        }
        else
        {
            Integer number = shoppingCart1.getNumber();
            shoppingCart1.setNumber(number+1);
            shoppingCartService.updateById(shoppingCart1);

            return Result.success(shoppingCart1);
        }

    }

    /**
     * 查询购物车清单
     * @return
     */
    @GetMapping("list")
    public Result<List<ShoppingCart>> queryShoppingCart(){
        Long userID = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,userID);
        lqw.orderByDesc(ShoppingCart::getCreateTime);


        List<ShoppingCart> shoppingCartList = shoppingCartService.list(lqw);

        return Result.success(shoppingCartList);
    }


    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("clean")
    public Result<String> cleanShoppingCart(){
        Long userId = BaseContext.getCurrentId();

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,userId);

        shoppingCartService.remove(lqw);

        return Result.success("清空成功");
    }


    /**
     * 减少菜品或套餐的数量
     * @param shoppingCart
     * @return
     */
    @PostMapping("sub")
    public Result<ShoppingCart> subtractShoppingCart(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());

        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,userId);

        //跟据菜品或套餐查询到已预定但需要减少数量的菜品或套餐
        Long dishId = shoppingCart.getDishId();
        lqw.eq(dishId != null,ShoppingCart::getDishId,dishId);
        Long setmealId = shoppingCart.getSetmealId();
        lqw.eq(setmealId != null,ShoppingCart::getSetmealId,setmealId);
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(lqw);

        //减少菜品或套餐的数量
        Integer number = shoppingCartServiceOne.getNumber();
        shoppingCartServiceOne.setNumber(number-1);
        shoppingCartService.updateById(shoppingCartServiceOne);

        return Result.success(shoppingCartServiceOne);
    }
}
