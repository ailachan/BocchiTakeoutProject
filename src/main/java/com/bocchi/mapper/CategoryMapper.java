package com.bocchi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bocchi.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
