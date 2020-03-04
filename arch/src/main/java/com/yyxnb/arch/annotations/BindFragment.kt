package com.yyxnb.arch.annotations

import android.support.annotation.ColorInt
import android.support.annotation.LayoutRes

/**
 * Fragment布局配置
 */
/*
@Target 作用域
定义该注解可使用的领域
AnnotationTarget.CLASS 可以给一个类型进行注解，类、接口、对象、甚至注解类本身
AnnotationTarget.ANNOTATION_CLASS 可以给一个注解类进行注解
AnnotationTarget.TYPE_PARAMETER 泛型参数（暂未支持）
AnnotationTarget.VALUE_PARAMETER 方法、构造函数的参数
AnnotationTarget.PROPERTY （计算）属性（该注解Java不可见）
AnnotationTarget.PROPERTY_GETTER 属性getter方法
AnnotationTarget.PROPERTY_SETTER 属性setter方法
AnnotationTarget.FIELD 字段变量，包括PROPERTY的备用字段（backing field）
AnnotationTarget.LOCAL_VARIABLE 局部变量
AnnotationTarget.CONSTRUCTOR 构造函数
AnnotationTarget.FUNCTION 方法、函数（不包括构造函数）
AnnotationTarget.FILE 文件整体
AnnotationTarget.TYPE 泛型支持
AnnotationTarget.TYPEALIAS typealias类型
AnnotationTarget.EXPRESSION 表达式
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
/*
@Retention 保留期
AnnotationRetention.SOURCE 注解只在源码阶段保留，在编译器进行编译时它将被丢弃忽视。 常用于代码检测。
AnnotationRetention.BINARY 注解被编译到二进制文件，但不会被加载到 JVM 中。 反射不可见。
AnnotationRetention.RUNTIME 注解被编译到二进制文件，它会被加载进入到 JVM 中，所以在程序运行时可以获取到它们。默认值。可用于运行时反射获取注解信息。
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
//@MustBeDocumented 将注解中的元素包含到 Javadoc 中去。
@MustBeDocumented
//@Repeatable 该注解允许复数应用到同一目标（通过提供不同注解参数区分）
annotation class BindFragment(
        // 布局资源
        @LayoutRes val layoutRes: Int = 0,
        // 是否子页面（作用vp等）
        val subPage: Boolean = false,
        // 给系统窗口留出空间（状态栏高度，包括水滴屏、刘海屏等的高度）
        val fitsSystemWindows: Boolean = false,
        // 状态栏透明
        val statusBarTranslucent: Boolean = true,
        // 状态栏文字 浅色、深色
        @BarStyle val statusBarStyle: Int = BarStyle.NONE,
        // 状态栏颜色
        @ColorInt val statusBarColor: Int = 0,
        // 侧滑
        @SwipeStyle val swipeBack: Int = SwipeStyle.Edge,
        // 分组关闭页面  -1 不区分
        val group: Int = -1,
        // 需要登录
        val needLogin: Boolean = false
)
