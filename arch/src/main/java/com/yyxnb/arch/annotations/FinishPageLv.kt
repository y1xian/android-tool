package com.yyxnb.arch.annotations

/**
 * 关闭页面
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class FinishPageLv(val value: Int = 0)