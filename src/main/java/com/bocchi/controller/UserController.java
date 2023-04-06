package com.bocchi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bocchi.common.Result;
import com.bocchi.pojo.User;
import com.bocchi.service.UserService;
import com.bocchi.utils.SMSUtils;
import com.bocchi.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @return
     */
    @PostMapping("/login")
    //json格式需要加RequestBody
    public Result<User> login(@RequestBody Map map, HttpServletRequest request) {
        log.info(map.toString());

        String inputPhone = (String) map.get("phone");
        String inputVerifyCode = (String) map.get("code");

        if (inputPhone != null && inputVerifyCode != null) {
            //获取存入session中的正确的验证码
            String correctVerifyCode = (String) request.getSession().getAttribute(inputPhone);

            //如果验证码一致则返回登录成功或者自动注册此账户
            if (inputVerifyCode.equals(correctVerifyCode)){
                LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
                lqw.eq(User::getPhone,inputPhone);
                User user = userService.getOne(lqw);

                //如果用户不存在则新注册此用户
                if (user == null){
                    user = new User();
                    user.setPhone(inputPhone);
                    user.setStatus(1);
                    userService.save(user);
                }

                //存入用户id到session,便于拦截器放行
                request.getSession().setAttribute("userId",user.getId());

                //如果存在直接返回查询到的用户信息
                return Result.success(user);
            }


        }

        return Result.error("登陆失败");
    }

    /**
     * 获取验证码
     *
     * @return
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpServletRequest request) {
        log.info(user.toString());

        if (user.getPhone() != null) {
            //生成验证码
            String verifyCode = ValidateCodeUtils.generateValidateCode4String(4);

            //向传过来的手机号发送生成的正确的验证码
            //SMSUtils.sendMessage("aila","SMS_275000751",user.getPhone(),verifyCode);

            //回显已发送的生成的正确的验证码到控制台
            log.info(verifyCode);

            //将已生成的正确的验证码存入后台,便于点击登录时判断
            request.getSession().setAttribute(user.getPhone(), verifyCode);

            return Result.success("验证码发送成功");
        }
        return Result.error("验证码发送失败");
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("loginout")
    public Result<String> logout() {
        return null;
    }
}
