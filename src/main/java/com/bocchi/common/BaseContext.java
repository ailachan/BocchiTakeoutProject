package com.bocchi.common;

/**
 * 每次请求对应一个线程,同一个线程id中ThreadLocal中存放的值相同<br><hr>
 * 基于ThreadLocal封装工具类，用户保存到ThreadLocal中和获取当前登录用户id
 */
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
