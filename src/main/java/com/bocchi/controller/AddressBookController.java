package com.bocchi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bocchi.common.BaseContext;
import com.bocchi.common.Result;
import com.bocchi.pojo.AddressBook;
import com.bocchi.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 添加地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public Result<String> addAddress(@RequestBody AddressBook addressBook){
        log.info(addressBook.toString());

        //设置当前是哪个user登录并添加的地址
        addressBook.setUserId(BaseContext.getCurrentId());

        addressBookService.save(addressBook);

        return Result.success("添加成功");
    }


    /**
     * 查询当前登录用户下的地址簿
     * @return
     */
    @GetMapping("list")
    public Result<List<AddressBook>> addressBookList(){
        Long currentUserId = BaseContext.getCurrentId();

        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId,currentUserId);

        List<AddressBook> addressBookList = addressBookService.list(lqw);

        return Result.success(addressBookList);
    }


    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("default")
    //传递的参数是一个json格式的AddressBook对象{"id":"xxx"},不是{xxx}
    public Result<String> setDefaultAddress(@RequestBody AddressBook addressBook){
        LambdaUpdateWrapper<AddressBook> luw = new LambdaUpdateWrapper<>();
        luw.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        luw.set(AddressBook::getIsDefault,0);

        //将当前登录用户下所有地址重置为非默认地址
        addressBookService.update(luw);

        //设置当前传过来的地址id为默认地址
        LambdaUpdateWrapper<AddressBook> luw2 = new LambdaUpdateWrapper<>();
        luw2.eq(AddressBook::getId, addressBook.getId());
        luw2.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        luw2.set(AddressBook::getIsDefault,1);
        addressBookService.update(luw2);

        return Result.success("设置成功");
    }

    /**
     * 按id查询地址信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Result<AddressBook> queryAddressBookById(@PathVariable Long id){
        log.info(id.toString());

        AddressBook addressBook = addressBookService.getById(id);

        return Result.success(addressBook);
    }

    /**
     * 修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    public Result<String> modifyAddressBookById(@RequestBody AddressBook addressBook){
        log.info(addressBook.toString());

        addressBookService.updateById(addressBook);

        return Result.success("修改成功");
    }


    /**
     * 删除地址
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteAddressBook(@RequestParam Long ids){
        log.info(ids.toString());

        addressBookService.removeById(ids);

        return Result.success("删除成功");
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("default")
    public Result<AddressBook> queryDefaultAddressBook(){

        Long currentUserId = BaseContext.getCurrentId();

        //查询当前登录用户的默认地址
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId,currentUserId);
        lqw.eq(AddressBook::getIsDefault,1);

        AddressBook addressBook = addressBookService.getOne(lqw);

        if (addressBook != null){
            return Result.success(addressBook);
        }

        return Result.error("未查询到默认地址");
    }
}
