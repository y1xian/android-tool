package com.yyxnb.arch.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class BindViewModel(val isActivity : Boolean = false)
