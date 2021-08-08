package com.yyxnb.oh.aop.aspect;

import com.yyxnb.oh.aop.annotation.BindJoint;
import com.yyxnb.oh.aop.constants.AopConstants;
import com.yyxnb.oh.aop.utils.AOPHelper;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/04/10
 * 描    述：自定义拦截切片
 * ================================================
 */
@Aspect
public class InterceptAspect {

    @Pointcut("within(@" + AopConstants.ANNOTATION_PATH + ".BindJoint *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {
    }

    /**
     * 方法切入点
     */
    @Pointcut("execution(@" + AopConstants.ANNOTATION_PATH + ".BindJoint * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }

    /**
     * 构造器切入点
     */
    @Pointcut("execution(@" + AopConstants.ANNOTATION_PATH + ".BindJoint *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {
    }

    @Around("(method() || constructor()) && @annotation(intercept)")
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint, BindJoint intercept) throws Throwable {
        if (null == AOPHelper.getInterceptor()) {
            //没有拦截器不执行切片拦截
            return joinPoint.proceed();
        }
        //执行拦截操作
        boolean result = proceedIntercept(intercept.value(), joinPoint);
        return result ? null : joinPoint.proceed();
    }

    /**
     * 执行拦截操作
     *
     * @param types     拦截的类型集合
     * @param joinPoint 切片
     * @return {@code true}: 拦截切片的执行 <br>{@code false}: 不拦截切片的执行
     */
    private boolean proceedIntercept(String[] types, JoinPoint joinPoint) throws Throwable {
        for (String type : types) {
            //拦截执行
            if (AOPHelper.getInterceptor().intercept(type, joinPoint)) {
                return true;
            }
        }
        return false;
    }

}