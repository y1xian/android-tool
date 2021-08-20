package com.yyxnb.what.aop.utils;

import androidx.annotation.NonNull;

public class AOPHelper {

    /**
     * 自定义拦截切片的拦截器接口
     */
    private static Interceptor sInterceptor;

    /**
     * 设置自定义拦截切片的拦截器接口
     *
     * @param sInterceptor 自定义拦截切片的拦截器接口
     */
    public static void setInterceptor(@NonNull Interceptor sInterceptor) {
        AOPHelper.sInterceptor = sInterceptor;
    }

    public static Interceptor getInterceptor() {
        return sInterceptor;
    }
}
