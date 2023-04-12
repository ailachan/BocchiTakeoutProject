package com.bocchi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bocchi.common.BaseContext;
import com.bocchi.common.OtherException;
import com.bocchi.mapper.OrderMapper;
import com.bocchi.pojo.*;
import com.bocchi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 提交订单
     *
     * @param orders
     */
    @Override
    @Transactional
    public void submitOrder(Orders orders) {
        //获取当前登录的用户id
        Long userId = BaseContext.getCurrentId();

        //查询当前用户购物车的数据
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(lqw);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new OtherException("购物车为空");
        }

        //查询当前订单的地址信息
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null) {
            throw new OtherException("地址信息有误");
        }

        //查询当前登录的用户信息
        User user = userService.getById(userId);

        //mp的id生成工具生成订单id
        long orderId = IdWorker.getId();

        //线程安全的Integer类型
        AtomicInteger amount = new AtomicInteger(0);

        //通过购物车信息构造购物车订单中每个商品的详细信息
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();

            //addAndGet用于将参数值累加,累加当前遍历到的item的单价*数量,计算所有item的总金额
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());

            //购物车中的每个item的金额set到订单详情中
            orderDetail.setAmount(item.getAmount().multiply(new BigDecimal(item.getNumber())));

            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setOrderId(orderId);
            orderDetail.setDishId(item.getDishId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            return orderDetail;
        }).collect(Collectors.toList());

        //构造订单合计信息
        orders.setId(orderId);
        orders.setNumber(String.valueOf(orderId));
        orders.setStatus(2);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setUserName(user.getName());//用户名
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());//收货人

        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail())
        );
        //购物车所有套餐菜品的总金额
        orders.setAmount(new BigDecimal(amount.get()));

        //订单详情，购物车中每个商品的详细信息
        orderDetailService.saveBatch(orderDetailList);
        //合计信息
        this.save(orders);

        //清空当前用户的购物车数据
        shoppingCartService.remove(lqw);

    }
}
