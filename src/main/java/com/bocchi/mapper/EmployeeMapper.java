package com.bocchi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bocchi.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
