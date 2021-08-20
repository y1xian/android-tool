package com.yyxnb.what.aop.annotation;

import com.yyxnb.what.aop.aspect.FastClickAspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface FastClick {

    /**
     * 自定义间隔时间
     */
    long value() default FastClickAspect.FAST_CLICK_INTERVAL_GLOBAL;
}