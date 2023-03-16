package com.bocchi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bocchi.common.Result;
import com.bocchi.pojo.Employee;
import com.bocchi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String username = employee.getUsername();
        String password = employee.getPassword();

        //对接收到的密码进行md5加密,方便比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //查询用户
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,username);
        Employee emp = employeeService.getOne(lqw);

        //用户不存在
        if (emp == null){
            return Result.error("用户不存在");
        }

        //密码错误,用从数据库查到的调equals,防止NPE
        if (!emp.getPassword().equals(password)){
            return Result.error("密码错误");
        }

        //状态为0表示禁用
        if (emp.getStatus()==0){
            return Result.error("用户已禁用");
        }

        //存入session,返回success结果集,内部code为1,并data=emp
        request.getSession().setAttribute("employeeId",emp.getId());
        return Result.success(emp);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        //清空session,并返回状态
        request.getSession().removeAttribute("employeeId");
        return Result.success("退出成功");
    }
}
