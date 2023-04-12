package com.bocchi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bocchi.pojo.OrderDetail;
import com.bocchi.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
