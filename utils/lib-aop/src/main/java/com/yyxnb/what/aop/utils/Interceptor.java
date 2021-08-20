package com.yyxnb.what.aop.utils;

import org.aspectj.lang.JoinPoint;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/04/10
 * 描    述：自定义拦截切片的拦截器实现接口
 * ================================================
 */
public interface Interceptor {

    /**
     * 执行拦截
     *
     * @param type      拦截的类型
     * @param joinPoint 切片切点
     * @return {@code true}: 拦截切片的执行 <br>{@code false}: 不拦截切片的执行
     * @throws Throwable
     */
    boolean intercept(String type, JoinPoint joinPoint) throws Throwable;

}