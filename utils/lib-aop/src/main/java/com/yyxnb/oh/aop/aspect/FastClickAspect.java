package com.yyxnb.oh.aop.aspect;

import com.yyxnb.oh.aop.annotation.FastClick;
import com.yyxnb.oh.aop.constants.AopConstants;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/04/09
 * 描    述：防止快速点击，通过 {@link FastClick#value()} 配置间隔时间
 * ================================================
 */
@Aspect
public class FastClickAspect {

    private static long lastClickTime;

    //500ms内不响应
    public static final int FAST_CLICK_INTERVAL_GLOBAL = 500;


    /**
     * 方法切入点
     * 监听系统的onClick(), lambda会失效
     */
    @Pointcut("execution(* android.view.View.OnClickListener.onClick(..))")
    public void method() {
    }

    /**
     * 定义切点，标记切点为所有被{@link FastClick}注解的方法
     * 这个类的全路径
     */
    @Pointcut("execution(@" + AopConstants.ANNOTATION_PATH + ".FastClick * *(..))")
    public void methodFastClick() {
    }

    /**
     * 在连接点进行方法替换
     */
    @Around("method() || methodFastClick()")
    public void fastClick(ProceedingJoinPoint joinPoint) throws Throwable {

        // 取出JoinPoint的签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 取出JoinPoint的方法
        Method method = methodSignature.getMethod();

        // 1. 全局统一的时间间隔
        long interval = FAST_CLICK_INTERVAL_GLOBAL;

        if (method.isAnnotationPresent(FastClick.class)) {
            // 2. 如果方法使用了@FastClick修饰，取出定制的时间间隔
            FastClick singleClick = method.getAnnotation(FastClick.class);
            if (null != singleClick) {
                interval = singleClick.value();
            }
        }
        // 取出目标对象
//        View target = (View) joinPoint.getArgs()[0];

        if (isFastClick(interval)) {
            joinPoint.proceed();
        }
    }

    /**
     * 判断是否在点击的时间内
     *
     * @param delayMillis 毫秒
     */
    private static boolean isFastClick(long delayMillis) {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= delayMillis) {
            lastClickTime = curClickTime;
            flag = true;
        }
        return flag;
    }
}