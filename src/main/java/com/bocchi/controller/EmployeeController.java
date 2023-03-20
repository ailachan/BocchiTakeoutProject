package com.bocchi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bocchi.common.Result;
import com.bocchi.pojo.Employee;
import com.bocchi.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
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

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        //清空session,并返回状态
        request.getSession().removeAttribute("employeeId");
        return Result.success("退出成功");
    }

    /**
     * 添加员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public Result<String> insertEmployee(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工,员工信息：{}",employee.toString());
        log.info(String.valueOf(Thread.currentThread().getId()));

        //设定初始密码并md5加密存储
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //1.8新时间类生成当前系统时间作为创建日期和更新日期
/*        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());*/

        //设置创建和更新人
/*        Long currentLoginAccount = (Long) request.getSession().getAttribute("employeeId");
        employee.setCreateUser(currentLoginAccount);
        employee.setUpdateUser(currentLoginAccount);*/

        employeeService.save(employee);

        //异常后此return不会返回
        return Result.success("添加成功");
    }


    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public Result<Page<Employee>> pageQuery(int page,int pageSize,String name){//接收get ?xxx=xxx&xxx=xxx格式数据
        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);

        //分页参数配置
        Page<Employee> pageInfo = new Page<>(page,pageSize);

        //是否需要分页模糊查询
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotEmpty(name),Employee::getName,name);//为空就不添加like条件name like value

        //结果排序
        lqw.orderByDesc(Employee::getUpdateTime);//order by updateTime desc

        //执行查询
        Page<Employee> pageResult = employeeService.page(pageInfo,lqw);

        return Result.success(pageResult);
    }


    /**
     * 根据ID更新员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public Result<String> updateEmployee(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

/*        employee.setUpdateUser((Long) request.getSession().getAttribute("employeeId"));
        employee.setUpdateTime(LocalDateTime.now());*/
        employeeService.updateById(employee);

        return Result.success("更新成功");
    }

    /**
     * 根据id查询员工信息,员工信息有些前端并没有在列表中一行显示出来,不能用vue的获取当前行数据来回显,要回显没有显示的数据得查一遍数据库
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Result<Employee> queryById(@PathVariable Long id){
        log.info(String.valueOf(employeeService.getById(id)));

        Employee employee = employeeService.getById(id);

        if (employee != null){
            return Result.success(employee);
        }
        return Result.error("未查询到员工");
    }
}
