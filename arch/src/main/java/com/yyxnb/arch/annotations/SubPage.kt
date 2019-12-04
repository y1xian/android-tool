package com.yyxnb.arch.annotations

/**
 * 是否子页面，用于懒加载
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class SubPage(val value: Boolean = true)