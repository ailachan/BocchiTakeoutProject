package com.bocchi.filter;

import com.alibaba.fastjson.JSON;
import com.bocchi.common.BaseContext;
import com.bocchi.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 登录过滤器
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //支持**通配符匹配
    private static final AntPathMatcher PATH_MATHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info(String.valueOf(Thread.currentThread().getId()));

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取本次请求的uri
        String requestURI = request.getRequestURI();

        //创建白名单
        String[] writeUris = new String[]{
                "/employee/login",
                "/employee/logout",
                //request.js,此js在引入并页面加载时生效
                "/backend/**",
                //此处是直接放行所有页面,因为每个页面还有部分ajax请求发给controller没有加入此白名单,可以根据这个来过滤这个页面的数据并跳转到login
                //加入白名单,也能够让页面中引入的前端拦截器js生效,如果让页面过滤则拦截器不生效,过滤响应到的code0没人接收调转到login
                "/front/**",
                "/file/upload",//TODO 过滤器放行文件上传请求
                "/file/download",//TODO 过滤器放行文件下载请求
                //放行前台用户登录
                "/user/sendMsg",
                "/user/login"
        };

        log.info("本次请求的uri：{}", request.getRequestURI());

        //匹配白名单放行
        for (String writeUri : writeUris) {
            boolean match = PATH_MATHER.match(writeUri, requestURI);
            if (match){
                log.info("{}为白名单请求被放行",request.getRequestURI());
                filterChain.doFilter(request,response);
                return;//过滤器链还会回来的,必须return
            }
        }

        //判断employee是否登录
        if (request.getSession().getAttribute("employeeId")!=null){
            log.info("id为{}的employee已登录",request.getSession().getAttribute("employeeId"));
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("employeeId"));//存入employee到本次请求的ThreadLocal中,便于元数据自动填充
            filterChain.doFilter(request,response);
            return;//过滤器链还会回来的,必须return
        }

        //判断user是否登录
        if (request.getSession().getAttribute("userId")!=null){
            log.info("id为{}的user已登录",request.getSession().getAttribute("userId"));
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("userId"));//存入user到本次请求的ThreadLocal中,便于元数据自动填充
            filterChain.doFilter(request,response);
            return;//过滤器链还会回来的,必须return
        }

        //给出响应由前端进行拦截调转
        log.info("{}被拦截,未登录",request.getRequestURI());
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
    }
}
