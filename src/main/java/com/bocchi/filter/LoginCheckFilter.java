package com.bocchi.filter;

import com.alibaba.fastjson.JSON;
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
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取本次请求的uri
        String requestURI = request.getRequestURI();

        //创建白名单
        String[] writeUris = new String[]{
                "/employee/login",
                "/employee/logout",
                //"/backend/js/request.js",
                //"/backend/page/login.html",
                "/backend/**",
                //此处是直接放行所有页面,因为每个页面还有部分ajax请求发给controller没有加入此白名单,而且前端也有个拦截器需要能接到这个请求
                //所以需要加入白名单,方便前端过滤
                "/front/**"
        };

        log.info("本次请求的uri：{}", request.getRequestURI());

        //匹配白名单放行
        for (String writeUri : writeUris) {
            boolean match = PATH_MATHER.match(writeUri, requestURI);
            if (match){
                filterChain.doFilter(request,response);
            }
        }

        //session中是否有值
        if (request.getSession().getAttribute("employeeId")!=null){
            filterChain.doFilter(request,response);
        }

        //给出响应由前端进行拦截调转
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
    }
}
