package com.yyxnb.oh.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/04/10
 * 描    述：自定义拦截注解
 * ================================================
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface BindJoint {
    /**
     * 拦截类型
     */
    String[] value();
}